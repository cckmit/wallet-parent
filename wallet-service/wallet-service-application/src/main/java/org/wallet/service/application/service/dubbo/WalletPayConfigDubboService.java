package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.cache.WalletPayConfigCache;
import org.wallet.common.constants.field.WalletPayConfigField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.wallet.WalletPayConfigDTO;
import org.wallet.common.entity.wallet.WalletPayConfigEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.WalletPayConfigService;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_WALLET_PAY_CONFIG)
public class WalletPayConfigDubboService extends BaseDubboService implements IService{

    @Autowired
    Cache cache;

    @Autowired
    WalletPayConfigService walletPayConfigService;

    public ServiceResponse getWalletPayConfigById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(WalletPayConfigField.WALLET_PAY_CONFIG_ID);
        }

        return response.setResult(walletPayConfigService.findOne(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllWalletPayConfig(ServiceRequest request, ServiceResponse response) {
        List<WalletPayConfigDTO> dtoList = cache.get(WalletPayConfigCache.CACHE_PREFIX, WalletPayConfigCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<WalletPayConfigEntity> appChainList = walletPayConfigService.findAll(new Sort(Sort.Direction.ASC, WalletPayConfigField.SORT));

        dtoList = appChainList.stream().map(entity -> {
            WalletPayConfigDTO dto = new WalletPayConfigDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(WalletPayConfigCache.CACHE_PREFIX, WalletPayConfigCache.ALL, dtoList, WalletPayConfigCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findWalletPayConfig(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findWalletPayConfigPage(request, response); }

        search = null == search ? new Search() : search;

        List<WalletPayConfigEntity> appChainList = walletPayConfigService.findAll(search);

        List<WalletPayConfigDTO> dtoList = appChainList.stream().map(entity -> {
            WalletPayConfigDTO dto = new WalletPayConfigDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findWalletPayConfigPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(WalletPayConfigField.PAGE); }

        PageDTO<WalletPayConfigEntity> entityPage = walletPayConfigService.findPage(search);

        List<WalletPayConfigEntity> appVersionList = entityPage.getRecords();

        List<WalletPayConfigDTO> dtoList = appVersionList.stream().map(entity -> {
            WalletPayConfigDTO dto = new WalletPayConfigDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addWalletPayConfig(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        WalletPayConfigEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(WalletPayConfigField.USER_ID); }
        if(null == entity){ return Responses.missingParam(WalletPayConfigField.WALLET_PAY_CONFIG); }

        entity.setId(null);
        entity.setCreator(userId);
        entity.setUpdater(userId);
        if(null == entity.getIcon()){ entity.setIcon(WalletPayConfigField.EMPTY); }
        if(null == entity.getSort()){ entity.setSort(0); }
        if(null == entity.getEnable()){ entity.setEnable(true); }

        WalletPayConfigEntity result = walletPayConfigService.save(entity);
        if(null == result){ return Responses.fail(ResponseCode.DATA_DUPLICATE, String.format("数据[%s:%s]已存在，不能重复添加",
                WalletPayConfigField.TYPE, entity.getType().name())); }

        return response.setResult(result);
    }

    public ServiceResponse updateWalletPayConfig(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        WalletPayConfigEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(WalletPayConfigField.USER_ID); }
        if(null == entity){ return Responses.missingParam(WalletPayConfigField.WALLET_PAY_CONFIG); }
        if(null == entity.getId()){ return Responses.missingParam(WalletPayConfigField.WALLET_PAY_CONFIG_ID); }

        entity.setCoverageUpdate(true);
        entity.setUpdater(userId);

        WalletPayConfigEntity result = walletPayConfigService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteWalletPayConfig(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(WalletPayConfigField.WALLET_PAY_CONFIG_ID); }

        walletPayConfigService.delete(id);

        return response;
    }
}
