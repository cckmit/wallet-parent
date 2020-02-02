package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wallet.common.constants.cache.AppTypeCache;
import org.wallet.common.constants.field.AppChainField;
import org.wallet.common.constants.field.AppInfoField;
import org.wallet.common.constants.field.AppTagField;
import org.wallet.common.constants.field.AppTypeField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.application.AppTypeDTO;
import org.wallet.common.entity.application.AppInfoEntity;
import org.wallet.common.entity.application.AppTypeEntity;
import org.wallet.common.enums.application.AppTagEnum;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.AppInfoService;
import org.wallet.service.application.service.AppTagService;
import org.wallet.service.application.service.AppTypeService;
import org.wallet.service.common.ResponseCode;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_APP_TYPE)
public class AppTypeDubboService extends BaseDubboService implements IService{

    @Autowired
    Cache cache;

    @Autowired
    AppTypeService appTypeService;

    @Autowired
    AppTagService appTagService;

    @Autowired
    AppInfoService appInfoService;

    public ServiceResponse getAppTypeById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(AppTypeField.APP_TYPE_ID);
        }

        return response.setResult(appTypeService.findOne(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllAppType(ServiceRequest request, ServiceResponse response) {
        List<AppTypeDTO> dtoList = cache.get(AppTypeCache.CACHE_PREFIX, AppTypeCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<AppTypeEntity> appChainList = appTypeService.findAll(new Sort(Sort.Direction.ASC, AppTypeField.SORT));

        dtoList = appChainList.stream().map(entity -> {
            AppTypeDTO dto = new AppTypeDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(AppTypeCache.CACHE_PREFIX, AppTypeCache.ALL, dtoList, AppTypeCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findRecommendAppType(ServiceRequest request, ServiceResponse response) {
        AppTypeDTO appTypeDTO = request.getParam();

        List<AppTypeDTO> recommendTypeList = appTypeService.findRecommend(appTypeDTO);

        return response.setResult(recommendTypeList);
    }

    public ServiceResponse findAppType(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findAppTypePage(request, response); }

        search = null == search ? new Search() : search;

        List<AppTypeEntity> appChainList = appTypeService.findAll(search);

        List<AppTypeDTO> dtoList = appChainList.stream().map(entity -> {
            AppTypeDTO dto = new AppTypeDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppTypePage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(AppTypeField.PAGE); }

        PageDTO<AppTypeEntity> entityPage = appTypeService.findPage(search);

        List<AppTypeEntity> appTypeList = entityPage.getRecords();

        List<AppTypeDTO> dtoList = appTypeList.stream().map(entity -> {
            AppTypeDTO dto = new AppTypeDTO();
            BeanUtil.copyProperties(entity, dto);

            Long appCount = appInfoService.getCount(Searchs.of(SearchFilters.eq(AppInfoField.APP_TYPE_ID, dto.getId())));

            dto.setAppCount(appCount);

            Long recommendCount = appTagService.getCount(Searchs.of(
                    SearchFilters.eq(AppTagField.APP_TYPE_ID, dto.getId()),
                    SearchFilters.eq(AppTagField.TAG, AppTagEnum.FEATURED)
            ));

            dto.setFeaturedCount(recommendCount);

            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addAppType(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppTypeEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppTypeField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppTypeField.APP_TYPE); }

        entity.setId(null);
        entity.setCreator(userId);
        entity.setUpdater(userId);
        if(null == entity.getEnable()){ entity.setEnable(true); }
        if(null == entity.getSort()){ entity.setSort(0); }
        if(null == entity.getIntro()){ entity.setIntro(AppTypeField.EMPTY); }

        AppTypeEntity result = appTypeService.save(entity);
        if(null == result){ return Responses.fail(ResponseCode.DATA_DUPLICATE, String.format("数据[%s:%s][%s:%s]已存在，不能重复添加",
                AppTypeField.APP_CHAIN_ID, entity.getChainId(), AppTypeField.NAME, entity.getName())); }

        return response.setResult(result);
    }

    public ServiceResponse updateAppType(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppTypeEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppTypeField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppTypeField.APP_TYPE); }
        if(null == entity.getId()){ return Responses.missingParam(AppTypeField.APP_TYPE_ID); }

        entity.setCoverageUpdate(true);
        entity.setUpdater(userId);

        List<AppInfoEntity> infoList = appInfoService.findAll(Searchs.of(SearchFilters.eq(AppChainField.APP_TYPE_ID, entity.getId())));

        // 当前类型包含DApp时，无法修改主链
        if(!CollectionUtils.isEmpty(infoList)){
            entity.setChainId(null);
        }

        AppTypeEntity result = appTypeService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteAppType(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(AppTypeField.APP_TYPE_ID); }

        List<AppInfoEntity> infoList = appInfoService.findAll(Searchs.of(SearchFilters.eq(AppChainField.APP_TYPE_ID, id)));

        if(!CollectionUtils.isEmpty(infoList)){
            return Responses.illegalParam("当前类型包含DApp，无法删除");
        }

        appTypeService.delete(id);

        return response;
    }
}
