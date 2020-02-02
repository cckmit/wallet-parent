package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.cache.AppAdvertCache;
import org.wallet.common.constants.field.AppAdvertField;
import org.wallet.common.constants.field.AppInfoField;
import org.wallet.common.constants.field.AppTagField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.application.AppAdvertDTO;
import org.wallet.common.entity.application.AppAdvertEntity;
import org.wallet.common.entity.application.AppChainEntity;
import org.wallet.common.entity.application.AppInfoEntity;
import org.wallet.common.enums.application.AppAdvertPointEnum;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.AppAdvertService;
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
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_APP_ADVERT)
public class AppAdvertDubboService extends BaseDubboService implements IService{

    @Autowired
    Cache cache;

    @Autowired
    AppAdvertService appAdvertService;

    @Autowired
    AppChainService appChainService;

    @Autowired
    AppInfoService appInfoService;

    public ServiceResponse getAppAdvertById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(AppAdvertField.APP_ADVERT_ID);
        }

        return response.setResult(appAdvertService.findOne(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllAppAdvert(ServiceRequest request, ServiceResponse response) {
        List<AppAdvertDTO> dtoList = cache.get(AppAdvertCache.CACHE_PREFIX, AppAdvertCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<AppAdvertEntity> appAdvertList = appAdvertService.findAll(new Sort(Sort.Direction.ASC, AppAdvertField.SORT));

        dtoList = appAdvertList.stream().map(entity -> {
            AppAdvertDTO dto = new AppAdvertDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(AppAdvertCache.CACHE_PREFIX, AppAdvertCache.ALL, dtoList, AppAdvertCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppAdvert(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findAppAdvertPage(request, response); }

        search = null == search ? new Search() : search;

        List<AppAdvertEntity> appAdvertList = appAdvertService.findAll(search);

        List<AppAdvertDTO> dtoList = appAdvertList.stream().map(entity -> {
            AppAdvertDTO dto = new AppAdvertDTO();
            BeanUtil.copyProperties(entity, dto);

            AppInfoEntity appInfo = appInfoService.findOne(dto.getAppId());

            if(null != appInfo){
                dto.setAppName(appInfo.getName());
                dto.setAppIcon(appInfo.getIcon());
            }

            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppAdvertPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(AppAdvertField.PAGE); }

        PageDTO<AppAdvertEntity> entityPage = appAdvertService.findPage(search);

        List<AppAdvertEntity> appAdvertList = entityPage.getRecords();

        List<AppAdvertDTO> dtoList = appAdvertList.stream().map(entity -> {
            AppAdvertDTO dto = new AppAdvertDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addAppAdvert(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppAdvertEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppAdvertField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppAdvertField.APP_ADVERT); }
        if(null == entity.getPoint()){ return Responses.missingParam(AppAdvertField.POINT); }
        AppAdvertPointEnum point = entity.getPoint();
        Long count;
        if(point.equals(AppAdvertPointEnum.RECOMMEND_POSTER)
                || point.equals(AppAdvertPointEnum.SPLASH_SCREEN)){
            if(null == entity.getChainId()){ entity.setChainId(0L); }
            if(null == entity.getAppId()){ entity.setAppId(0L); }

            count = appAdvertService.getCount(Searchs.of(SearchFilters.eq(AppAdvertField.POINT, point)));
        }else{
            if(null == entity.getChainId()){ return Responses.missingParam(AppAdvertField.APP_CHAIN_ID); }
            if(null == entity.getAppId()){ return Responses.missingParam(AppAdvertField.APP_INFO_ID); }

            AppInfoEntity appInfo = appInfoService.findOne(entity.getAppId());

            AppChainEntity appChain = appChainService.findOne(entity.getChainId());
            if(null == appChain){ return Responses.notFoundData(AppInfoField.APP_CHAIN_ID, entity.getChainId()); }
            if(!entity.getChainId().equals(appInfo.getChainId())){
                return Responses.illegalParam(String.format("传入的[%s:%s]与[%s:%s][%s:%s]不一致",
                        AppTagField.APP_CHAIN_ID, entity.getChainId(), AppTagField.APP_INFO, entity.getAppId(),
                        AppTagField.APP_CHAIN_ID, appInfo.getChainId()));
            }

            count = appAdvertService.getCount(Searchs.of(
                    SearchFilters.eq(AppAdvertField.APP_CHAIN_ID, entity.getChainId()),
                    SearchFilters.eq(AppAdvertField.POINT, point)
            ));
        }

        Integer limit = point.getLimit();

        if(count != null && count >= limit){
            return Responses.fail(ResponseCode.OVER_LIMIT, String.format("当前位置[%s]广告数量[%s]超过限制[%s]",
                    point.name(), limit + 1, limit));
        }

        if(null != entity.getAppId() && StringUtils.isEmpty(entity.getUrl())){
            AppInfoEntity appInfo = appInfoService.findOne(entity.getAppId());
            if(null != appInfo){
                entity.setUrl(appInfo.getUrl());
            }
        }

        entity.setId(null);
        entity.setCreator(userId);
        entity.setUpdater(userId);
        if(null == entity.getEnable()){ entity.setEnable(true); }
        if(null == entity.getSort()){ entity.setSort(0); }
        if(null == entity.getUrl()){ entity.setUrl(AppAdvertField.EMPTY); }

        AppAdvertEntity result = appAdvertService.save(entity);

        return response.setResult(result);
    }

    public ServiceResponse updateAppAdvert(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppAdvertEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppAdvertField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppAdvertField.APP_ADVERT); }
        if(null == entity.getId()){ return Responses.missingParam(AppAdvertField.APP_ADVERT_ID); }

        AppAdvertEntity oldAdvert = appAdvertService.findOne(entity.getId());
        AppAdvertPointEnum point = entity.getPoint();

        if(null != entity.getChainId() && oldAdvert.getChainId().compareTo(entity.getChainId()) != 0){ return Responses.illegalParam(String.format("不支持修改该字段[%s]", AppTagField.APP_CHAIN_ID)); }

        if(null == oldAdvert){
            return Responses.notFoundData(entity.getId());
        }
        if(null == point){
            point = oldAdvert.getPoint();
        }

        Boolean updatePoint = !oldAdvert.getPoint().equals(entity.getPoint());
        Boolean simpleAdvert = point.equals(AppAdvertPointEnum.RECOMMEND_POSTER)
                || point.equals(AppAdvertPointEnum.SPLASH_SCREEN);

        Long chainId = null != entity.getChainId() ? entity.getChainId() : oldAdvert.getChainId();
        Long appId = null != entity.getAppId() ? entity.getAppId() : oldAdvert.getAppId();
        AppInfoEntity appInfo = appInfoService.findOne(appId);

        if(updatePoint && !simpleAdvert){
            AppChainEntity appChain = appChainService.findOne(chainId);
            if(null == appChain){ return Responses.notFoundData(AppInfoField.APP_CHAIN_ID, chainId); }
            if(!chainId.equals(appInfo.getChainId())){
                return Responses.illegalParam(String.format("传入的[%s:%s]与[%s:%s][%s:%s]不一致",
                        AppTagField.APP_CHAIN_ID, chainId, AppTagField.APP_INFO, appId,
                        AppTagField.APP_CHAIN_ID, appInfo.getChainId()));
            }
        }

        if(updatePoint){
            Long count;
            if(simpleAdvert){
                count = appAdvertService.getCount(Searchs.of(SearchFilters.eq(AppAdvertField.POINT, point)));
            }else{
                count = appAdvertService.getCount(Searchs.of(
                        SearchFilters.eq(AppAdvertField.APP_CHAIN_ID, entity.getChainId()),
                        SearchFilters.eq(AppAdvertField.POINT, point)
                ));
            }

            Integer limit = point.getLimit();

            if(count != null && count >= limit){
                return Responses.fail(ResponseCode.OVER_LIMIT, String.format("当前位置[%s]广告数量[%s]超过限制[%s]",
                        point.name(), limit + 1, limit));
            }
        }

        if(null != appId && StringUtils.isEmpty(entity.getUrl())){
            if(null != appInfo){
                entity.setUrl(appInfo.getUrl());
            }
        }

        entity.setCoverageUpdate(true);
        entity.setUpdater(userId);
        // 禁止修改主链
        entity.setChainId(null);

        AppAdvertEntity result = appAdvertService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteAppAdvert(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(AppAdvertField.APP_ADVERT_ID); }

        appAdvertService.delete(id);

        return response;
    }
}
