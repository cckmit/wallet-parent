package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.cache.AppInfoCache;
import org.wallet.common.constants.field.AppInfoField;
import org.wallet.common.constants.field.AppTagField;
import org.wallet.common.constants.field.AppTypeField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.application.AppInfoDTO;
import org.wallet.common.dto.application.AppTagDTO;
import org.wallet.common.dto.application.RecommendTypeDTO;
import org.wallet.common.entity.application.AppChainEntity;
import org.wallet.common.entity.application.AppInfoEntity;
import org.wallet.common.entity.application.AppTypeEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.*;
import org.wallet.service.common.ResponseCode;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_APP_INFO)
public class AppInfoDubboService extends BaseDubboService implements IService {

    @Autowired
    Cache cache;

    @Autowired
    AppAdvertService appAdvertService;

    @Autowired
    AppChainService appChainService;

    @Autowired
    AppInfoService appInfoService;

    @Autowired
    AppTagService appTagService;

    @Autowired
    AppTypeService appTypeService;

    public ServiceResponse getAppInfoById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(AppInfoField.APP_INFO_ID);
        }

        return response.setResult(appInfoService.findOne(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllAppInfo(ServiceRequest request, ServiceResponse response) {
        List<AppInfoDTO> dtoList = cache.get(AppInfoCache.CACHE_PREFIX, AppInfoCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<AppInfoEntity> appInfoList = appInfoService.findAll(new Sort(Sort.Direction.ASC, AppInfoField.SORT));

        dtoList = appInfoList.stream().map(entity -> {
            AppInfoDTO dto = new AppInfoDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(AppInfoCache.CACHE_PREFIX, AppInfoCache.ALL, dtoList, AppInfoCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppInfo(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findAppInfoPage(request, response); }

        search = null == search ? new Search() : search;

        List<AppInfoEntity> appInfoList = appInfoService.findAll(search);

        List<AppInfoDTO> dtoList = appInfoList.stream().map(entity -> {
            AppInfoDTO dto = new AppInfoDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findTypeRecommendApp(ServiceRequest request, ServiceResponse response) {
        Long chainId = request.getParam();

        if(null == chainId){ return Responses.missingParam(AppInfoField.APP_CHAIN_ID); }

        List<RecommendTypeDTO> tagAppList = appInfoService.findTypeRecommendApp(chainId);

        response.setResult(tagAppList);

        return response;
    }

    public ServiceResponse findAppByTag(ServiceRequest request, ServiceResponse response) {
        AppTagDTO tag = request.getParam();

        if(null == tag){ return Responses.missingParam(AppTagField.APP_TAG); }

        List<AppInfoDTO> appList = appInfoService.findAppByTag(tag);

        response.setResult(appList);

        return response;
    }

    public ServiceResponse findAppInfoPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(AppInfoField.PAGE); }

        PageDTO<AppInfoEntity> entityPage = appInfoService.findPage(search);

        List<AppInfoEntity> appInfoList = entityPage.getRecords();

        List<AppInfoDTO> dtoList = appInfoList.stream().map(entity -> {
            AppInfoDTO dto = new AppInfoDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addAppInfo(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppInfoEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppInfoField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppInfoField.APP_INFO); }

        entity.setId(null);
        entity.setCreator(userId);
        entity.setUpdater(userId);
        if(null == entity.getEnable()){ entity.setEnable(true); }
        if(null == entity.getSort()){ entity.setSort(0); }

        Long chainId = entity.getChainId();
        AppChainEntity appChain = appChainService.findOne(chainId);
        if(null == appChain){ return Responses.notFoundData(AppInfoField.APP_CHAIN_ID, chainId); }

        Long typeId = entity.getTypeId();

        AppTypeEntity appType = appTypeService.findOne(typeId);

        if(null == appType){ return Responses.notFoundData(AppInfoField.APP_TYPE_ID, typeId); }
        if(!appType.getChainId().equals(entity.getChainId())){
            return Responses.illegalParam(String.format("传入的[%s:%s]与[%s:%s][%s:%s]不一致",
                    AppTagField.APP_CHAIN_ID, entity.getChainId(), AppTagField.APP_TYPE, entity.getTypeId(),
                    AppTagField.APP_CHAIN_ID, appType.getChainId()));
        }

        AppInfoEntity result = appInfoService.save(entity);

        if(null == result){ return Responses.fail(ResponseCode.DATA_DUPLICATE,
                String.format("数据[%s:%s][%s:%s][%s:%s]已存在，不能重复添加",
                    AppTypeField.APP_CHAIN_ID, entity.getChainId(),
                    AppTypeField.APP_TYPE_ID, entity.getTypeId(),
                    AppTypeField.NAME, entity.getName())); }

        return response.setResult(result);
    }

    public ServiceResponse updateAppInfo(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppInfoEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppInfoField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppInfoField.APP_INFO); }
        if(null == entity.getId()){ return Responses.missingParam(AppInfoField.APP_INFO_ID); }

        entity.setCoverageUpdate(true);
        entity.setUpdater(userId);

        Long chainId = entity.getChainId();
        if(null != chainId){
            AppChainEntity appChain = appChainService.findOne(chainId);
            if(null == appChain){ return Responses.notFoundData(AppInfoField.APP_CHAIN_ID, chainId); }

            Long typeId = entity.getTypeId();

            if(null != typeId){
                AppTypeEntity appType = appTypeService.findOne(typeId);

                if(null == appType){ return Responses.notFoundData(AppInfoField.APP_TYPE_ID, typeId); }
                if(!appType.getChainId().equals(chainId)){
                    return Responses.illegalParam(String.format("传入的[%s:%s]与[%s:%s][%s:%s]不一致",
                            AppTagField.APP_CHAIN_ID, chainId, AppTagField.APP_TYPE, entity.getTypeId(),
                            AppTagField.APP_CHAIN_ID, appType.getChainId()));
                }
            }
        }

        AppInfoEntity result = appInfoService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteAppInfo(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(AppInfoField.APP_INFO_ID); }

        Long tagCount = appTagService.getCount(Searchs.of(SearchFilters.eq(AppInfoField.APP_INFO_ID, id)));
        if(null != tagCount && tagCount > 0){
            return Responses.illegalParam("该DApp包含标签[" + tagCount + "]个，无法删除");
        }
        Long advertCount = appAdvertService.getCount(Searchs.of(SearchFilters.eq(AppInfoField.APP_INFO_ID, id)));
        if(null != advertCount && advertCount > 0){
            return Responses.illegalParam("该DApp包含广告位[" + advertCount + "]个，无法删除");
        }

        appInfoService.delete(id);

        return response;
    }
}
