package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.cache.AppLinkCache;
import org.wallet.common.constants.field.AppLinkField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.application.AppLinkDTO;
import org.wallet.common.entity.application.AppLinkEntity;
import org.wallet.common.enums.application.AppLinkTypeEnum;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.AppLinkService;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_APP_LINK)
public class AppLinkDubboService extends BaseDubboService implements IService{

    @Autowired
    Cache cache;

    @Autowired
    AppLinkService appLinkService;

    public ServiceResponse getAppLinkById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(AppLinkField.APP_LINK_ID);
        }

        return response.setResult(appLinkService.findOne(id));
    }

    public ServiceResponse getAppLinkByType(ServiceRequest request, ServiceResponse response) {
        AppLinkTypeEnum type = request.getParam();
        if(null == type){
            return Responses.missingParam(AppLinkField.TYPE);
        }

        return response.setResult(appLinkService.findOne(Searchs.of(SearchFilters.eq(AppLinkField.TYPE, type))));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllAppLink(ServiceRequest request, ServiceResponse response) {
        List<AppLinkDTO> dtoList = cache.get(AppLinkCache.CACHE_PREFIX, AppLinkCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<AppLinkEntity> appChainList = appLinkService.findAll(new Sort(Sort.Direction.ASC, AppLinkField.SORT));

        dtoList = appChainList.stream().map(entity -> {
            AppLinkDTO dto = new AppLinkDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(AppLinkCache.CACHE_PREFIX, AppLinkCache.ALL, dtoList, AppLinkCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppLink(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findAppLinkPage(request, response); }

        search = null == search ? new Search() : search;

        List<AppLinkEntity> appChainList = appLinkService.findAll(search);

        List<AppLinkDTO> dtoList = appChainList.stream().map(entity -> {
            AppLinkDTO dto = new AppLinkDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppLinkPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(AppLinkField.PAGE); }

        PageDTO<AppLinkEntity> entityPage = appLinkService.findPage(search);

        List<AppLinkEntity> appLinkList = entityPage.getRecords();

        List<AppLinkDTO> dtoList = appLinkList.stream().map(entity -> {
            AppLinkDTO dto = new AppLinkDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addAppLink(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppLinkEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppLinkField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppLinkField.APP_LINK); }

        if(AppLinkTypeEnum.PROTOCOL.equals(entity.getType())){
            Long count = appLinkService.getCount(Searchs.of(SearchFilters.eq(AppLinkField.TYPE, AppLinkTypeEnum.PROTOCOL)));
            if(count > 0){
                return Responses.fail(ResponseCode.OVER_LIMIT, String.format("类型[%s]链接数量[%s]超过限制[%s]",
                        AppLinkTypeEnum.PROTOCOL.name(), count + 1, 1));
            }
        }

        entity.setId(null);
        entity.setCreator(userId);
        entity.setUpdater(userId);
        if(null == entity.getDeletable()){ entity.setDeletable(true); }
        if(null == entity.getEnable()){ entity.setEnable(true); }
        if(null == entity.getSort()){ entity.setSort(0); }

        AppLinkEntity result = appLinkService.save(entity);

        return response.setResult(result);
    }

    public ServiceResponse updateAppLink(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppLinkEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppLinkField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppLinkField.APP_LINK); }
        if(null == entity.getId()){ return Responses.missingParam(AppLinkField.APP_LINK_ID); }

        entity.setCoverageUpdate(true);
        entity.setUpdater(userId);
        if(Boolean.TRUE.equals(entity.getDeletable())){ entity.setDeletable(null); }

        AppLinkEntity result = appLinkService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteAppLink(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(AppLinkField.APP_LINK_ID); }

        AppLinkEntity entity = appLinkService.findOne(id);

        if(null != entity && !entity.getDeletable()){
            return Responses.fail(ResponseCode.UNAUTHORIZED, "此数据无法删除");
        }

        appLinkService.delete(id);

        return response;
    }
}
