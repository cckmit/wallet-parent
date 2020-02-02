package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wallet.common.constants.cache.WalletInfoCache;
import org.wallet.common.constants.field.WalletInfoField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.wallet.WalletInfoDTO;
import org.wallet.common.entity.wallet.WalletInfoEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.WalletInfoService;
import org.wallet.service.common.ResponseCode;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_WALLET_INFO)
public class WalletInfoDubboService extends BaseDubboService implements IService{

    @Autowired
    Cache cache;

    @Autowired
    WalletInfoService walletInfoService;

    public ServiceResponse getCurrentWalletInfo(ServiceRequest request, ServiceResponse response) {
        WalletInfoDTO info = cache.get(WalletInfoCache.CACHE_PREFIX, WalletInfoCache.CURRENT, WalletInfoDTO.class);

        if(null != info){ return response.setResult(info); }

        List<WalletInfoEntity> appChainList = walletInfoService.findAll(new Sort(Sort.Direction.ASC, WalletInfoField.ID));

        List<WalletInfoDTO> dtoList = appChainList.stream().map(entity -> {
            WalletInfoDTO dto = new WalletInfoDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        if(!CollectionUtils.isEmpty(dtoList)){
            cache.put(WalletInfoCache.CACHE_PREFIX, WalletInfoCache.CURRENT, dtoList.get(0), WalletInfoCache.EXPIRE);

            response.setResult(dtoList.get(0));
        }

        return response;
    }

    public ServiceResponse getWalletInfoById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(WalletInfoField.WALLET_INFO_ID);
        }

        return response.setResult(walletInfoService.findOne(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllWalletInfo(ServiceRequest request, ServiceResponse response) {
        List<WalletInfoDTO> dtoList = cache.get(WalletInfoCache.CACHE_PREFIX, WalletInfoCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<WalletInfoEntity> appChainList = walletInfoService.findAll(new Sort(Sort.Direction.ASC, WalletInfoField.ID));

        dtoList = appChainList.stream().map(entity -> {
            WalletInfoDTO dto = new WalletInfoDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(WalletInfoCache.CACHE_PREFIX, WalletInfoCache.ALL, dtoList, WalletInfoCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findWalletInfo(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findWalletInfoPage(request, response); }

        search = null == search ? new Search() : search;

        List<WalletInfoEntity> appChainList = walletInfoService.findAll(search);

        List<WalletInfoDTO> dtoList = appChainList.stream().map(entity -> {
            WalletInfoDTO dto = new WalletInfoDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findWalletInfoPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(WalletInfoField.PAGE); }

        PageDTO<WalletInfoEntity> entityPage = walletInfoService.findPage(search);

        List<WalletInfoEntity> appVersionList = entityPage.getRecords();

        List<WalletInfoDTO> dtoList = appVersionList.stream().map(entity -> {
            WalletInfoDTO dto = new WalletInfoDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addWalletInfo(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        WalletInfoEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(WalletInfoField.USER_ID); }
        if(null == entity){ return Responses.missingParam(WalletInfoField.WALLET_INFO); }

        entity.setId(null);
        entity.setCreator(userId);
        entity.setUpdater(userId);

        List<WalletInfoEntity> entityList = walletInfoService.findAll();

        if(!CollectionUtils.isEmpty(entityList)){ return Responses.fail(ResponseCode.DATA_DUPLICATE, "无法添加多个钱包");}

        WalletInfoEntity result = walletInfoService.save(entity);

        return response.setResult(result);
    }

    public ServiceResponse updateWalletInfo(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        WalletInfoEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(WalletInfoField.USER_ID); }
        if(null == entity){ return Responses.missingParam(WalletInfoField.WALLET_INFO); }
        if(null == entity.getId()){ return Responses.missingParam(WalletInfoField.WALLET_INFO_ID); }

        entity.setCoverageUpdate(true);
        entity.setUpdater(userId);

        WalletInfoEntity result = walletInfoService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteWalletInfo(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(WalletInfoField.WALLET_INFO_ID); }

        walletInfoService.delete(id);

        return response;
    }
}
