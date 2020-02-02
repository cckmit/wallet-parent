package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wallet.common.constants.cache.AppChainCache;
import org.wallet.common.constants.field.AppChainField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.application.AppChainDTO;
import org.wallet.common.entity.application.AppChainEntity;
import org.wallet.common.entity.application.AppInfoEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.AppChainService;
import org.wallet.service.application.service.AppInfoService;
import org.wallet.service.common.ResponseCode;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_APP_CHAIN)
public class AppChainDubboService extends BaseDubboService implements IService {

    @Autowired
    Cache cache;

    @Autowired
    AppChainService appChainService;

    @Autowired
    AppInfoService appInfoService;

    public ServiceResponse getAppChainById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(AppChainField.APP_CHAIN_ID);
        }

        return response.setResult(appChainService.findOne(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllAppChain(ServiceRequest request, ServiceResponse response) {
        List<AppChainDTO> dtoList = cache.get(AppChainCache.CACHE_PREFIX, AppChainCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<AppChainEntity> appChainList = appChainService.findAll(new Sort(Sort.Direction.ASC, AppChainField.SORT));

        dtoList = appChainList.stream().map(entity -> {
            AppChainDTO dto = new AppChainDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(AppChainCache.CACHE_PREFIX, AppChainCache.ALL, dtoList, AppChainCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppChain(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findAppChainPage(request, response); }

        search = null == search ? new Search() : search;

        List<AppChainEntity> appChainList = appChainService.findAll(search);

        List<AppChainDTO> dtoList = appChainList.stream().map(entity -> {
            AppChainDTO dto = new AppChainDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppChainPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(AppChainField.PAGE); }

        PageDTO<AppChainEntity> entityPage = appChainService.findPage(search);

        List<AppChainEntity> appChainList = entityPage.getRecords();

        List<AppChainDTO> dtoList = appChainList.stream().map(entity -> {
            AppChainDTO dto = new AppChainDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse findAdminAppChain(ServiceRequest request, ServiceResponse response) {
        return response.setResult(appChainService.findAdminAppChain());
    }

    public ServiceResponse addAppChain(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppChainEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppChainField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppChainField.APP_CHAIN); }

        entity.setId(null);
        entity.setCreator(userId);
        entity.setUpdater(userId);
        if(null == entity.getEnable()){ entity.setEnable(true); }
        if(null == entity.getSort()){ entity.setSort(0); }

        AppChainEntity result = appChainService.save(entity);

        if(null == result){ return Responses.fail(ResponseCode.DATA_DUPLICATE, String.format("数据[%s:%s]已存在，不能重复添加",
                AppChainField.NAME, entity.getName())); }

        return response.setResult(result);
    }

    public ServiceResponse updateAppChain(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppChainEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppChainField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppChainField.APP_CHAIN); }
        if(null == entity.getId()){ return Responses.missingParam(AppChainField.APP_CHAIN_ID); }

        entity.setCoverageUpdate(true);
        entity.setUpdater(userId);

        AppChainEntity result = appChainService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteAppChain(ServiceRequest request, ServiceResponse response) {
        Long chainId = request.getParam();

        if(null == chainId){ return Responses.missingParam(AppChainField.APP_CHAIN_ID); }

        List<AppInfoEntity> infoList = appInfoService.findAll(Searchs.of(SearchFilters.eq(AppChainField.APP_CHAIN_ID, chainId)));

        if(!CollectionUtils.isEmpty(infoList)){
            return Responses.illegalParam("当前主链包含DApp，无法删除");
        }

        appChainService.delete(chainId);

        return response;
    }
}
