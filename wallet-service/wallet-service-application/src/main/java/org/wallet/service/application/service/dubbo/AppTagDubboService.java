package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.field.AppTagField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.application.AppTagDTO;
import org.wallet.common.dto.application.TagAppDTO;
import org.wallet.common.entity.application.AppInfoEntity;
import org.wallet.common.entity.application.AppTagEntity;
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
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_APP_TAG)
public class AppTagDubboService extends BaseDubboService implements IService{

    @Autowired
    Cache cache;

    @Autowired
    AppInfoService appInfoService;

    @Autowired
    AppTagService appTagService;

    @Autowired
    AppTypeService appTypeService;

    public ServiceResponse findTagApp(ServiceRequest request, ServiceResponse response) {
        AppTagDTO tag = request.getParam();

        if(null == tag){ return Responses.missingParam(AppTagField.TAG); }
        if(null == tag.getChainId()){ return Responses.missingParam(AppTagField.APP_CHAIN_ID); }
        if(null == tag.getTag()){ return Responses.missingParam(AppTagField.TAG); }

        AppTagEnum tagEnum = tag.getTag();
        Boolean typeTag = (tagEnum.equals(AppTagEnum.TYPE_RECOMMEND) || tagEnum.equals(AppTagEnum.FEATURED));
        if(typeTag && null == tag.getTypeId()){
            return Responses.missingParam(AppTagField.APP_TYPE_ID);
        }

        List<TagAppDTO> tagAppList = appTagService.findTagApp(tag);

        response.setResult(tagAppList);

        return response;
    }

    public ServiceResponse findAppTagPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(AppTagField.PAGE); }

        PageDTO<AppTagEntity> entityPage = appTagService.findPage(search);

        List<AppTagEntity> appTagList = entityPage.getRecords();

        List<AppTagDTO> dtoList = appTagList.stream().map(entity -> {
            AppTagDTO dto = new AppTagDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addAppTag(ServiceRequest request, ServiceResponse response) {
        AppTagEntity entity = request.getParam();
        Long chainId = entity.getChainId();
        Long typeId = entity.getTypeId();
        Long appId = entity.getAppId();
        AppTagEnum tag = entity.getTag();
        Long userId = request.getUserId();
        String img = entity.getImg();

        if(null == userId){ return Responses.missingParam(AppTagField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppTagField.APP_TAG); }
        if(null == appId){ return Responses.missingParam(AppTagField.APP_INFO_ID); }

        AppInfoEntity appInfoEntity = appInfoService.findOne(appId);

        if(null == appInfoEntity){ return Responses.notFoundData(AppTagField.APP_INFO_ID, appId); }

        if(null == chainId){
            entity.setChainId(appInfoEntity.getChainId());
        }else if(!chainId.equals(appInfoEntity.getChainId())){
            return Responses.illegalParam(String.format("传入的[%s:%s]与[%s:%s][%s:%s]不一致",
                    AppTagField.APP_CHAIN_ID, chainId, AppTagField.APP_INFO, appId, AppTagField.APP_CHAIN_ID, appInfoEntity.getChainId()));
        }

        if(null == typeId){
            entity.setTypeId(appInfoEntity.getTypeId());
            typeId = appInfoEntity.getTypeId();
        }else{
            AppTypeEntity appTypeEntity = appTypeService.findOne(typeId);
            if(null == appTypeEntity){
                return Responses.notFoundData(AppTagField.APP_TYPE_ID, typeId);
            }else if(!chainId.equals(appTypeEntity.getChainId())){
                return Responses.illegalParam(String.format("传入的[%s:%s]与[%s:%s][%s:%s]不一致",
                        AppTagField.APP_CHAIN_ID, chainId, AppTagField.APP_TYPE, typeId, AppTagField.APP_CHAIN_ID, appTypeEntity.getChainId()));
            }
        }

        if(null == tag){ return Responses.missingParam(AppTagField.TAG); }

        if(tag.equals(AppTagEnum.HOT) && StringUtils.isEmpty(entity.getImg())){
            return Responses.missingParam(AppTagField.IMG);
        }

        Long count;

        if(tag.equals(AppTagEnum.TYPE_RECOMMEND)
                || tag.equals(AppTagEnum.FEATURED)){
            count = appTagService.getCount(Searchs.of(
                    SearchFilters.eq(AppTagField.APP_CHAIN_ID, chainId),
                    SearchFilters.eq(AppTagField.APP_TYPE_ID, typeId),
                    SearchFilters.eq(AppTagField.TAG, tag)
            ));
        }else{
            count = appTagService.getCount(Searchs.of(
                    SearchFilters.eq(AppTagField.APP_CHAIN_ID, chainId),
                    SearchFilters.eq(AppTagField.TAG, tag)
            ));
        }

        if(count != null && count >= tag.getLimit()){
            return Responses.fail(ResponseCode.OVER_LIMIT, String.format("当前标签[%s]DApp数量[%s]超过限制[%s]",
                    tag.name(), tag.getLimit() + 1, tag.getLimit()));
        }

        entity.setId(null);
        entity.setCreator(userId);
        if(null == entity.getSort()){ entity.setSort(0); }

        AppTagEntity result = appTagService.save(entity);

        if(null == result){ return Responses.fail(ResponseCode.DATA_DUPLICATE, String.format("数据[%s:%s][%s:%s][%s:%s]已存在，不能重复添加",
                AppTagField.APP_CHAIN_ID, chainId,
                AppTagField.APP_INFO_ID, appId,
                AppTagField.TAG, tag.name())); }

        return response.setResult(result);
    }

    public ServiceResponse updateAppTag(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppTagEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppTagField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppTagField.APP_TAG); }
        if(null == entity.getId()){ return Responses.missingParam(AppTagField.APP_TAG_ID); }

        AppTagEntity oldEntity = appTagService.findOne(entity.getId());

        if(null != entity.getChainId() && oldEntity.getChainId().compareTo(entity.getChainId()) != 0){
            return Responses.illegalParam(String.format("不支持修改该字段[%s]", AppTagField.APP_CHAIN_ID));
        }
        if(null != entity.getTypeId() && oldEntity.getTypeId().compareTo(entity.getTypeId()) != 0){
            return Responses.illegalParam(String.format("不支持修改该字段[%s]", AppTagField.APP_TYPE_ID));
        }
        if(null != entity.getAppId() && oldEntity.getAppId().compareTo(entity.getAppId()) != 0){
            return Responses.illegalParam(String.format("不支持修改该字段[%s]", AppTagField.APP_INFO_ID));
        }

        entity.setCoverageUpdate(true);
        entity.setChainId(null);
        entity.setTypeId(null);
        entity.setAppId(null);
        entity.setCreator(null);
        entity.setCreateDate(null);

        if(null == oldEntity){
            return Responses.notFoundData(entity.getId());
        }

        if(null == entity.getTag()){
            entity.setTag(oldEntity.getTag());
        }

        if(StringUtils.isEmpty(entity.getImg())){
            entity.setImg(oldEntity.getImg());
        }

        if(entity.getTag().equals(AppTagEnum.HOT) && StringUtils.isEmpty(entity.getImg())){
            return Responses.missingParam(AppTagField.IMG);
        }

        AppTagEntity result = appTagService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteAppTag(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(AppTagField.APP_TAG_ID); }

        appTagService.delete(id);

        return response;
    }
}
