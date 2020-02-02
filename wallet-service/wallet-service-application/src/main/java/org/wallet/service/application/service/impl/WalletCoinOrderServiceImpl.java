package org.wallet.service.application.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wallet.common.constants.CoinConstants;
import org.wallet.common.constants.cache.QuotesCache;
import org.wallet.common.constants.field.EntityField;
import org.wallet.common.constants.field.WalletCoinOrderField;
import org.wallet.common.dto.chart.ChartDataDTO;
import org.wallet.common.dto.chart.PercentDataDTO;
import org.wallet.common.dto.wallet.WalletCoinOrderDTO;
import org.wallet.common.dto.wallet.req.TransferStatisticsReqDTO;
import org.wallet.common.entity.application.AppChainEntity;
import org.wallet.common.entity.wallet.WalletCoinOrderEntity;
import org.wallet.common.entity.wallet.WalletPayConfigEntity;
import org.wallet.common.enums.wallet.PaymentTypeEnum;
import org.wallet.common.enums.wallet.WalletCoinOrderStatusEnum;
import org.wallet.dap.common.dubbo.Responses;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.dap.common.utils.StringGenerator;
import org.wallet.service.application.dao.AppChainJpaDao;
import org.wallet.service.application.dao.WalletCoinOrderJpaDao;
import org.wallet.service.application.dao.WalletPayConfigJpaDao;
import org.wallet.service.application.service.WalletCoinOrderService;
import org.wallet.service.common.service.AbstractCrudService;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zengfucheng
 **/
@Service
public class WalletCoinOrderServiceImpl extends AbstractCrudService<WalletCoinOrderJpaDao, WalletCoinOrderEntity> implements WalletCoinOrderService {

    @Autowired
    private AppChainJpaDao appChainJpaDao;

    @Autowired
    private WalletPayConfigJpaDao walletPayConfigJpaDao;

    @Override
    public ServiceResponse createOrder(String coinName, PaymentTypeEnum type, String email) {
        WalletPayConfigEntity payConfig = walletPayConfigJpaDao.getFirstByType(type);

        if(null == payConfig){
            return Responses.illegalParam(String.format("不支持该支付方式[%s]", type.name()));
        }

        WalletCoinOrderEntity order = new WalletCoinOrderEntity();
        WalletCoinOrderDTO dto = new WalletCoinOrderDTO();

        String memo = RandomUtil.getRandom().nextLong(10000000, 99999999) + "";

        order.setNo(StringGenerator.newInstance().generate(12));
        order.setCoinName(coinName);
        order.setPaymentType(type);
        order.setEmail(email);
        order.setMemo(memo);
        order.setAmount(payConfig.getAmount());
        order.setSymbol(payConfig.getUnit());
        order.setReceiver(payConfig.getReceiptAccount());
        order.setStatus(WalletCoinOrderStatusEnum.INIT);
        order.setSender(EntityField.EMPTY);
        order.setInviteCode(EntityField.EMPTY);
        order.setTrxId(EntityField.EMPTY);
        order.setCreator(EntityField.DEFAULT_ADMIN_USER_ID);

        save(order);

        BeanUtils.copyProperties(order, dto);

        return Responses.success(dto);
    }

    @Override
    public List<ChartDataDTO> accountBuyFrequency(TransferStatisticsReqDTO reqDTO) {
        Date now = new Date(System.currentTimeMillis() + 100000);
        if(null == reqDTO.getStartDate()){
            reqDTO.setStartDate(new Date(0));
        }
        if(null == reqDTO.getEndDate()){
            reqDTO.setEndDate(now);
        }else{
            reqDTO.setEndDate(new Date(reqDTO.getEndDate().getTime() + Duration.ofDays(1).toMillis()));
        }

        AppChainEntity chainEntity = appChainJpaDao.getOne(reqDTO.getChainId());

        if(null == chainEntity){
            return null;
        }
        String chainCoinName = chainEntity.getCoinName();

        List<Map<String, Object>> mapList = getRepository().accountBuyFrequency(chainCoinName, reqDTO.getStartDate(), reqDTO.getEndDate());

        List<ChartDataDTO> resultList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(mapList)){
            BigDecimal usdRate = cache.get(QuotesCache.PRICE_USD, chainCoinName, BigDecimal.class);

            usdRate = null == usdRate ? BigDecimal.ZERO : usdRate;

            BigDecimal finalUsdRate = usdRate;

            mapList.forEach(map -> {
                BigDecimal amount = (BigDecimal) map.get(WalletCoinOrderField.AMOUNT);

                ChartDataDTO res = JSON.parseObject(JSON.toJSONString(map), ChartDataDTO.class);

                if(null != amount){
                    BigDecimal usdAmount = amount.multiply(finalUsdRate).setScale(CoinConstants.SCALE_PRICE, BigDecimal.ROUND_DOWN);

                    res.setAmount(usdAmount);
                }

                resultList.add(res);
            });
        }

        return resultList;
    }

    @Override
    public List<PercentDataDTO> accountBuyPercent(TransferStatisticsReqDTO reqDTO) {
        Date now = new Date(System.currentTimeMillis() + 100000);
        if(null == reqDTO.getStartDate()){
            reqDTO.setStartDate(new Date(0));
        }
        if(null == reqDTO.getEndDate()){
            reqDTO.setEndDate(now);
        }else{
            reqDTO.setEndDate(new Date(reqDTO.getEndDate().getTime() + Duration.ofDays(1).toMillis()));
        }

        AppChainEntity chainEntity = appChainJpaDao.getOne(reqDTO.getChainId());

        if(null == chainEntity){
            return null;
        }

        String chainCoinName = chainEntity.getCoinName();

        List<Map<String, Object>> mapList = getRepository().accountBuyPercent(chainCoinName, reqDTO.getStartDate(), reqDTO.getEndDate());

        List<PercentDataDTO> resultList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(mapList)){
            BigDecimal totalNum = BigDecimal.ZERO;
            for(Map<String, Object> map : mapList){
                String name = (String) map.get(WalletCoinOrderField.NAME);

                PaymentTypeEnum paymentType = PaymentTypeEnum.valueOf(name);

                PercentDataDTO res = JSON.parseObject(JSON.toJSONString(map), PercentDataDTO.class);

                resultList.add(res);

                totalNum = totalNum.add(res.getNum());
            }

            boolean hasNum = totalNum.compareTo(BigDecimal.ZERO) > 0;

            BigDecimal finalTotalNum = totalNum;

            resultList.forEach(result -> {
                if(hasNum){
                    BigDecimal percent = result.getNum().divide(finalTotalNum, CoinConstants.SCALE_PERCENT, BigDecimal.ROUND_HALF_UP);
                    result.setPercent(percent);
                }else{
                    result.setPercent(BigDecimal.ZERO);
                }
            });
        }

        return resultList;
    }
}
