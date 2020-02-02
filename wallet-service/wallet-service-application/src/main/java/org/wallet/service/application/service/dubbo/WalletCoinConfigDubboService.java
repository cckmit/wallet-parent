package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.cache.WalletCoinConfigCache;
import org.wallet.common.constants.field.WalletCoinConfigField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.wallet.WalletCoinConfigDTO;
import org.wallet.common.dto.wallet.WalletCoinPriceDTO;
import org.wallet.common.entity.wallet.WalletCoinConfigEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.SearchFinal;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.WalletCoinConfigService;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_WALLET_COIN_CONFIG)
public class WalletCoinConfigDubboService extends BaseDubboService implements IService{

    @Autowired
    Cache cache;

    @Autowired
    WalletCoinConfigService walletCoinConfigService;

    public ServiceResponse getWalletCoinConfigById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(WalletCoinConfigField.WALLET_COIN_CONFIG_ID);
        }

        return response.setResult(walletCoinConfigService.findOne(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllWalletCoinConfig(ServiceRequest request, ServiceResponse response) {
        List<WalletCoinConfigDTO> dtoList = cache.get(WalletCoinConfigCache.CACHE_PREFIX, WalletCoinConfigCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<WalletCoinConfigEntity> appChainList = walletCoinConfigService.findAll(new Sort(Sort.Direction.ASC, WalletCoinConfigField.SORT));

        dtoList = appChainList.stream().map(entity -> {
            WalletCoinConfigDTO dto = new WalletCoinConfigDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(WalletCoinConfigCache.CACHE_PREFIX, WalletCoinConfigCache.ALL, dtoList, WalletCoinConfigCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findWalletCoinPrice(ServiceRequest request, ServiceResponse response) {
        Long chainId = request.getParamValue(WalletCoinConfigField.APP_CHAIN_ID);
        PageDTO<WalletCoinPriceDTO> page = request.getParamValue(WalletCoinConfigField.PAGE);

        if(null == chainId){ return Responses.missingParam(WalletCoinConfigField.APP_CHAIN_ID); }
        if(null == page){ page = PageDTO.of(SearchFinal.DEFAULT_PAGE, SearchFinal.DEFAULT_LIMIT); }

        return response.setResult(walletCoinConfigService.findWalletCoinPrice(chainId, page));
    }

    public ServiceResponse findWalletCoinConfig(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findWalletCoinConfigPage(request, response); }

        search = null == search ? new Search() : search;

        List<WalletCoinConfigEntity> appChainList = walletCoinConfigService.findAll(search);

        List<WalletCoinConfigDTO> dtoList = appChainList.stream().map(entity -> {
            WalletCoinConfigDTO dto = new WalletCoinConfigDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findWalletCoinConfigPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(WalletCoinConfigField.PAGE); }

        PageDTO<WalletCoinConfigEntity> entityPage = walletCoinConfigService.findPage(search);

        List<WalletCoinConfigEntity> appVersionList = entityPage.getRecords();

        List<WalletCoinConfigDTO> dtoList = appVersionList.stream().map(entity -> {
            WalletCoinConfigDTO dto = new WalletCoinConfigDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addWalletCoinConfig(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        WalletCoinConfigEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(WalletCoinConfigField.USER_ID); }
        if(null == entity){ return Responses.missingParam(WalletCoinConfigField.WALLET_COIN_CONFIG); }

        entity.setId(null);
        entity.setCreator(userId);
        entity.setUpdater(userId);
        if(null == entity.getSort()){ entity.setSort(0); }
        if(null == entity.getEnable()){ entity.setEnable(true); }

        WalletCoinConfigEntity result = walletCoinConfigService.save(entity);

        return response.setResult(result);
    }

    public ServiceResponse updateWalletCoinConfig(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        WalletCoinConfigEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(WalletCoinConfigField.USER_ID); }
        if(null == entity){ return Responses.missingParam(WalletCoinConfigField.WALLET_COIN_CONFIG); }
        if(null == entity.getId()){ return Responses.missingParam(WalletCoinConfigField.WALLET_COIN_CONFIG_ID); }

        entity.setCoverageUpdate(true);
        entity.setUpdater(userId);

        WalletCoinConfigEntity result = walletCoinConfigService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteWalletCoinConfig(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(WalletCoinConfigField.WALLET_COIN_CONFIG_ID); }

        walletCoinConfigService.delete(id);

        return response;
    }
}
