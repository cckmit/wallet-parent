package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.cache.WalletCoinOrderCache;
import org.wallet.common.constants.field.WalletCoinOrderField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.wallet.WalletCoinOrderDTO;
import org.wallet.common.dto.wallet.req.TransferStatisticsReqDTO;
import org.wallet.common.entity.wallet.WalletCoinOrderEntity;
import org.wallet.common.enums.wallet.PaymentTypeEnum;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.WalletCoinOrderService;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_WALLET_COIN_ORDER)
public class WalletCoinOrderDubboService extends BaseDubboService implements IService{

    @Autowired
    Cache cache;

    @Autowired
    WalletCoinOrderService walletCoinConfigService;

    public ServiceResponse findOrderByNo(ServiceRequest request, ServiceResponse response) {
        String no = request.getParam();
        if(StringUtils.isEmpty(no)){
            return Responses.missingParam(WalletCoinOrderField.NO);
        }

        return response.setResult(walletCoinConfigService.findOne(Searchs.of(SearchFilters.eq(WalletCoinOrderField.NO, no))));
    }

    public ServiceResponse accountBuyFrequency(ServiceRequest request, ServiceResponse response) {
        TransferStatisticsReqDTO req = request.getParam();
        if(null == req){
            return Responses.missingParam("req");
        }

        return response.setResult(walletCoinConfigService.accountBuyFrequency(req));
    }

    public ServiceResponse accountBuyPercent(ServiceRequest request, ServiceResponse response) {
        TransferStatisticsReqDTO req = request.getParam();
        if(null == req){
            return Responses.missingParam("req");
        }

        return response.setResult(walletCoinConfigService.accountBuyPercent(req));
    }

    public ServiceResponse getWalletCoinOrderById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(WalletCoinOrderField.WALLET_COIN_ORDER_ID);
        }

        return response.setResult(walletCoinConfigService.findOne(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllWalletCoinOrder(ServiceRequest request, ServiceResponse response) {
        List<WalletCoinOrderDTO> dtoList = cache.get(WalletCoinOrderCache.CACHE_PREFIX, WalletCoinOrderCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<WalletCoinOrderEntity> appChainList = walletCoinConfigService.findAll(new Sort(Sort.Direction.ASC, WalletCoinOrderField.SORT));

        dtoList = appChainList.stream().map(entity -> {
            WalletCoinOrderDTO dto = new WalletCoinOrderDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(WalletCoinOrderCache.CACHE_PREFIX, WalletCoinOrderCache.ALL, dtoList, WalletCoinOrderCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findWalletCoinOrder(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findWalletCoinOrderPage(request, response); }

        search = null == search ? new Search() : search;

        List<WalletCoinOrderEntity> appChainList = walletCoinConfigService.findAll(search);

        List<WalletCoinOrderDTO> dtoList = appChainList.stream().map(entity -> {
            WalletCoinOrderDTO dto = new WalletCoinOrderDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findWalletCoinOrderPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(WalletCoinOrderField.PAGE); }

        PageDTO<WalletCoinOrderEntity> entityPage = walletCoinConfigService.findPage(search);

        List<WalletCoinOrderEntity> appVersionList = entityPage.getRecords();

        List<WalletCoinOrderDTO> dtoList = appVersionList.stream().map(entity -> {
            WalletCoinOrderDTO dto = new WalletCoinOrderDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse createOrder(ServiceRequest request, ServiceResponse response) {
        WalletCoinOrderDTO dto = request.getParam();
        String coinName = dto.getCoinName();
        PaymentTypeEnum paymentType = dto.getPaymentType();
        String email = dto.getEmail();

        if(StringUtils.isEmpty(coinName)){ return Responses.missingParam(WalletCoinOrderField.COIN_NAME); }
        if(null == paymentType){ return Responses.missingParam(WalletCoinOrderField.PAYMENT_TYPE); }
        if(StringUtils.isEmpty(email)){ return Responses.missingParam(WalletCoinOrderField.EMAIL); }

        return walletCoinConfigService.createOrder(coinName, paymentType, email);
    }

    public ServiceResponse addWalletCoinOrder(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        WalletCoinOrderEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(WalletCoinOrderField.USER_ID); }
        if(null == entity){ return Responses.missingParam(WalletCoinOrderField.WALLET_COIN_ORDER); }

        entity.setId(null);
        entity.setCreator(userId);

        WalletCoinOrderEntity result = walletCoinConfigService.save(entity);

        return response.setResult(result);
    }

    public ServiceResponse updateWalletCoinOrder(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        WalletCoinOrderEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(WalletCoinOrderField.USER_ID); }
        if(null == entity){ return Responses.missingParam(WalletCoinOrderField.WALLET_COIN_ORDER); }
        if(null == entity.getId()){ return Responses.missingParam(WalletCoinOrderField.WALLET_COIN_ORDER_ID); }

        entity.setCoverageUpdate(true);

        WalletCoinOrderEntity result = walletCoinConfigService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteWalletCoinOrder(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(WalletCoinOrderField.WALLET_COIN_ORDER_ID); }

        walletCoinConfigService.delete(id);

        return response;
    }
}
