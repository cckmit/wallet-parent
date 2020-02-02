package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.cache.WalletExchangeCache;
import org.wallet.common.constants.field.AppChainNodeField;
import org.wallet.common.constants.field.WalletExchangeField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.wallet.WalletExchangeDTO;
import org.wallet.common.entity.wallet.WalletExchangeEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.WalletExchangeService;
import org.wallet.service.common.ResponseCode;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_WALLET_EXCHANGE)
public class WalletExchangeDubboService extends BaseDubboService implements IService{

    @Autowired
    Cache cache;

    @Autowired
    WalletExchangeService walletExchangeService;

    public ServiceResponse getWalletExchangeById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(WalletExchangeField.WALLET_EXCHANGE_ID);
        }

        return response.setResult(walletExchangeService.findOne(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllWalletExchange(ServiceRequest request, ServiceResponse response) {
        List<WalletExchangeDTO> dtoList = cache.get(WalletExchangeCache.CACHE_PREFIX, WalletExchangeCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<WalletExchangeEntity> appChainList = walletExchangeService.findAll(new Sort(Sort.Direction.ASC, WalletExchangeField.SORT));

        dtoList = appChainList.stream().map(entity -> {
            WalletExchangeDTO dto = new WalletExchangeDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(WalletExchangeCache.CACHE_PREFIX, WalletExchangeCache.ALL, dtoList, WalletExchangeCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findWalletExchange(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findWalletExchangePage(request, response); }

        search = null == search ? new Search() : search;

        List<WalletExchangeEntity> appChainList = walletExchangeService.findAll(search);

        List<WalletExchangeDTO> dtoList = appChainList.stream().map(entity -> {
            WalletExchangeDTO dto = new WalletExchangeDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findWalletExchangePage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(WalletExchangeField.PAGE); }

        PageDTO<WalletExchangeEntity> entityPage = walletExchangeService.findPage(search);

        List<WalletExchangeEntity> appVersionList = entityPage.getRecords();

        List<WalletExchangeDTO> dtoList = appVersionList.stream().map(entity -> {
            WalletExchangeDTO dto = new WalletExchangeDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addWalletExchange(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        WalletExchangeEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(WalletExchangeField.USER_ID); }
        if(null == entity){ return Responses.missingParam(WalletExchangeField.WALLET_EXCHANGE); }

        entity.setId(null);
        entity.setCreator(userId);
        entity.setUpdater(userId);
        if(null == entity.getEnable()){ entity.setEnable(true); }
        if(null == entity.getSort()){ entity.setSort(0); }

        WalletExchangeEntity result = walletExchangeService.save(entity);

        if(null == result){ return Responses.fail(ResponseCode.DATA_DUPLICATE, String.format("数据[%s:%s]已存在，不能重复添加",
                AppChainNodeField.NAME, entity.getName())); }

        return response.setResult(result);
    }

    public ServiceResponse updateWalletExchange(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        WalletExchangeEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(WalletExchangeField.USER_ID); }
        if(null == entity){ return Responses.missingParam(WalletExchangeField.WALLET_EXCHANGE); }
        if(null == entity.getId()){ return Responses.missingParam(WalletExchangeField.WALLET_EXCHANGE_ID); }

        entity.setCoverageUpdate(true);
        entity.setUpdater(userId);

        WalletExchangeEntity result = walletExchangeService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteWalletExchange(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(WalletExchangeField.WALLET_EXCHANGE_ID); }

        walletExchangeService.delete(id);

        return response;
    }
}
