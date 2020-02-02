package org.wallet.service.batch.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.cache.AppID;
import org.wallet.common.dto.wallet.WalletBlockTransDTO;
import org.wallet.common.entity.application.AppInviteCodeEntity;
import org.wallet.common.entity.wallet.WalletCoinOrderEntity;
import org.wallet.common.enums.application.AppInviteCodeTypeEnum;
import org.wallet.common.enums.wallet.WalletCoinOrderStatusEnum;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.service.batch.config.MailProperties;
import org.wallet.service.batch.dao.WalletCoinOrderJpaDao;
import org.wallet.service.batch.service.AppInviteCodeService;
import org.wallet.service.batch.service.WalletCoinOrderService;
import org.wallet.service.common.service.AbstractCrudService;

import java.util.List;

/**
 * @author zengfucheng
 **/
@Service
public class WalletCoinOrderServiceImpl extends AbstractCrudService<WalletCoinOrderJpaDao, WalletCoinOrderEntity> implements WalletCoinOrderService {

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private AppInviteCodeService appInviteCodeService;

    @Reference(group = DubboServiceGroup.CLIENT_EMAIL, timeout = 30000)
    private IService emailService;

    @Override
    public void paid(WalletBlockTransDTO dto) {
        String memo = dto.getMemo();
        if(StringUtils.isEmpty(memo)){
            return;
        }

        WalletCoinOrderEntity order = getRepository().findFirstByMemo(memo);
        if(null == order){
            return;
        }

        order.setSender(dto.getSender());
        order.setTrxId(dto.getTrxId());

        if(dto.getQuantity().compareTo(order.getAmount()) < 0){
            logger.warn("用户[{}]交易金额不足[{} < {}]", order.getEmail(), dto.getQuantity().toPlainString(), order.getAmount().toPlainString());
            order.setStatus(WalletCoinOrderStatusEnum.INSUFFICIENT_QUANTITY);
        }else{
            getInviteCodeAndSendCode(memo, order);
        }

        save(order);
    }

    @Override
    public void getInviteCodeAndSendCode(String memo, WalletCoinOrderEntity order) {
        String inviteCode = getInviteCode();
        if (null == inviteCode) {
            logger.warn("获取[{}]验证码失败", memo);
            order.setStatus(WalletCoinOrderStatusEnum.GET_CODE_FAIL);
        } else {
            order.setInviteCode(inviteCode);

            boolean result = sendInviteCode(order.getEmail(), inviteCode);
            if (result) {
                order.setStatus(WalletCoinOrderStatusEnum.FINISH);
            } else {
                order.setStatus(WalletCoinOrderStatusEnum.SEND_FAIL);
            }
        }
    }

    /**
     * 获取邀请码
     * @return 邀请码
     */
    private String getInviteCode(){
        ServiceResponse response = appInviteCodeService.createNewCode(AppInviteCodeTypeEnum.EOS_ACCOUNT);
        AppInviteCodeEntity inviteCodeEntity = response.getResult();
        if (!response.success()
                || null == inviteCodeEntity) {
            return null;
        } else {
            return inviteCodeEntity.getCode();
        }
    }

    @Override
    public boolean sendInviteCode(String email, String inviteCode){
        if(StringUtils.isEmpty(email) || StringUtils.isEmpty(inviteCode)){
            return false;
        }

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailProperties.getFrom());
        message.setTo(email);
        message.setSubject(mailProperties.getInviteCodeSubject());
        message.setText(mailProperties.getInviteCodeContent() + inviteCode);

        ServiceResponse response = emailService.invoke(ServiceRequest.newInstance(
                AppID.WALLET_SERVICE_BATCH, DubboServiceGroup.CLIENT_EMAIL, "sendMail", null, message));

        return response.success();
    }

    @Override
    public List<WalletCoinOrderEntity> findAbnormalOrder(String receiveAccount) {
        return getRepository().findAbnormalOrder(receiveAccount);
    }
}
