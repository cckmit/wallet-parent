package org.wallet.service.batch.service.impl;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.MQTopic;
import org.wallet.common.constants.cache.WalletPayConfigCache;
import org.wallet.common.constants.field.EntityField;
import org.wallet.common.dto.wallet.WalletBlockTransDTO;
import org.wallet.common.dto.wallet.WalletPayConfigDTO;
import org.wallet.common.entity.wallet.WalletBlockTransEntity;
import org.wallet.common.entity.wallet.WalletCoinOrderEntity;
import org.wallet.common.enums.wallet.PaymentTypeEnum;
import org.wallet.common.enums.wallet.WalletCoinOrderStatusEnum;
import org.wallet.dap.common.utils.ThreadPool;
import org.wallet.dap.mq.core.RMQSendingTemplate;
import org.wallet.service.batch.dao.WalletBlockTransJpaDao;
import org.wallet.service.batch.service.WalletBlockTransService;
import org.wallet.service.batch.service.WalletCoinOrderService;
import org.wallet.service.common.service.AbstractCrudService;

import java.util.List;

/**
 * @author zengfucheng
 **/
@Service
@Transactional(timeout = 30, rollbackFor = Exception.class)
public class WalletBlockTransServiceImpl extends AbstractCrudService<WalletBlockTransJpaDao, WalletBlockTransEntity> implements WalletBlockTransService {


    @Autowired
    private RMQSendingTemplate sendingTemplate;

    @Autowired
    private WalletCoinOrderService walletCoinOrderService;

    @Override
    public void syncTrans(List<WalletBlockTransDTO> dtoList) {
        long start = System.currentTimeMillis();
        try{
            if(!CollectionUtils.isEmpty(dtoList)){
                dtoList.forEach(dto -> {
                    WalletBlockTransEntity entity = getRepository().findFirstByTrxId(dto.getTrxId());

                    if(null != entity){
                        return;
                    }

                    entity = new WalletBlockTransEntity();

                    BeanUtils.copyProperties(dto, entity);

                    if(StringUtils.isEmpty(entity.getMemo())){ entity.setMemo(EntityField.EMPTY); }

                    entity.setCreator(EntityField.DEFAULT_ADMIN_USER_ID);

                    save(entity);

                    ThreadPool.getInstance().exe(() -> {
                        SendResult result = sendingTemplate.syncSend(MQTopic.WALLET_ACCOUNT_ORDER_PAID, dto);
                        logger.info("EOS 账号邀请支付到账MQ消息[MsgID:{}][SendResult:{}][Memo:{}][TrxID:{}]",
                                result.getMsgId(), result.getSendStatus().name(), dto.getMemo(), dto.getTrxId());
                    });
                });
            }
        } finally {
            logger.info("同步交易完成：{} ms", (System.currentTimeMillis() - start));
        }
    }

    @Override
    public void fixAbnormalTrans() {
        String eosReceiverAccount = getEosReceiverAccount();
        if(StringUtils.isEmpty(eosReceiverAccount)){
            logger.warn("获取EOS收款账号失败");
            return;
        }
        List<WalletCoinOrderEntity> orderList = walletCoinOrderService.findAbnormalOrder(eosReceiverAccount);

        if(!CollectionUtils.isEmpty(orderList)){
            for (WalletCoinOrderEntity order : orderList) {
                ThreadPool.getInstance().exe(() -> {
                    String no = order.getNo();
                    WalletCoinOrderStatusEnum status = order.getStatus();
                    String memo = order.getMemo();
                    try {
                        if (WalletCoinOrderStatusEnum.INIT.equals(status)) {
                            WalletBlockTransEntity transEntity = getRepository().findFirstByMemo(order.getMemo());
                            if(null != transEntity){
                                WalletBlockTransDTO transDTO = new WalletBlockTransDTO();
                                BeanUtils.copyProperties(transEntity, transDTO);
                                walletCoinOrderService.paid(transDTO);
                            }
                        } else if (WalletCoinOrderStatusEnum.GET_CODE_FAIL.equals(status)) {
                            walletCoinOrderService.getInviteCodeAndSendCode(memo, order);
                            walletCoinOrderService.save(order);
                        } else if (WalletCoinOrderStatusEnum.SEND_FAIL.equals(status)) {
                            boolean result = walletCoinOrderService.sendInviteCode(order.getEmail(), order.getInviteCode());
                            if (result) {
                                order.setStatus(WalletCoinOrderStatusEnum.FINISH);
                            } else {
                                order.setStatus(WalletCoinOrderStatusEnum.SEND_FAIL);
                            }
                            walletCoinOrderService.save(order);
                        }
                        logger.info("订单[{}]修复完成: {}", no, JSON.toJSONString(order));
                    }catch (Exception e){
                        logger.error("订单[{}][{}]修复失败：{}", no, JSON.toJSONString(order), e.getMessage(), e);
                    }
                });
            }
        }
    }

    @Override
    public String getEosReceiverAccount(){
        WalletPayConfigDTO dto = cache.get(WalletPayConfigCache.TYPE, PaymentTypeEnum.EOS.name(), WalletPayConfigDTO.class);

        if(null == dto){
            return null;
        }

        return dto.getReceiptAccount();
    }
}
