package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.cache.AppVersionCache;
import org.wallet.common.constants.field.AppVersionField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.application.AppVersionDTO;
import org.wallet.common.entity.application.AppVersionEntity;
import org.wallet.common.enums.Device;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.AppVersionService;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_APP_VERSION)
public class AppVersionDubboService extends BaseDubboService implements IService{

    @Autowired
    Cache cache;

    @Autowired
    AppVersionService appVersionService;

    public ServiceResponse getAppVersionById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(AppVersionField.APP_VERSION_ID);
        }

        return response.setResult(appVersionService.findOne(id));
    }

    public ServiceResponse getCurrentAppVersion(ServiceRequest request, ServiceResponse response) {
        Device device = request.getParam();
        if(null == device){
            return Responses.missingParam(AppVersionField.DEVICE);
        }

        return response.setResult(appVersionService.findOne(Searchs.of(SearchFilters.eq(AppVersionField.DEVICE, device))));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllAppVersion(ServiceRequest request, ServiceResponse response) {
        List<AppVersionDTO> dtoList = cache.get(AppVersionCache.CACHE_PREFIX, AppVersionCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<AppVersionEntity> appChainList = appVersionService.findAll(new Sort(Sort.Direction.ASC, AppVersionField.SORT));

        dtoList = appChainList.stream().map(entity -> {
            AppVersionDTO dto = new AppVersionDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(AppVersionCache.CACHE_PREFIX, AppVersionCache.ALL, dtoList, AppVersionCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppVersion(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findAppVersionPage(request, response); }

        search = null == search ? new Search() : search;

        List<AppVersionEntity> appChainList = appVersionService.findAll(search);

        List<AppVersionDTO> dtoList = appChainList.stream().map(entity -> {
            AppVersionDTO dto = new AppVersionDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppVersionPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(AppVersionField.PAGE); }

        PageDTO<AppVersionEntity> entityPage = appVersionService.findPage(search);

        List<AppVersionEntity> appVersionList = entityPage.getRecords();

        List<AppVersionDTO> dtoList = appVersionList.stream().map(entity -> {
            AppVersionDTO dto = new AppVersionDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addAppVersion(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppVersionEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppVersionField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppVersionField.APP_VERSION); }

        entity.setId(null);
        entity.setCreator(userId);
        entity.setUpdater(userId);

        AppVersionEntity result = appVersionService.save(entity);

        if(null == result){ return Responses.fail(ResponseCode.DATA_DUPLICATE, String.format("数据[%s:%s]已存在，不能重复添加",
                AppVersionField.DEVICE, entity.getDevice())); }

        return response.setResult(result);
    }

    public ServiceResponse updateAppVersion(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppVersionEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppVersionField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppVersionField.APP_VERSION); }
        if(null == entity.getId()){ return Responses.missingParam(AppVersionField.APP_VERSION_ID); }

        entity.setCoverageUpdate(true);
        entity.setUpdater(userId);

        AppVersionEntity result = appVersionService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteAppVersion(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(AppVersionField.APP_VERSION_ID); }

        appVersionService.delete(id);

        return response;
    }
}
