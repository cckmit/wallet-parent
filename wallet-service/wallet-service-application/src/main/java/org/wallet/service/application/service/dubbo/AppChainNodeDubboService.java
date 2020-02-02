package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.cache.AppChainNodeCache;
import org.wallet.common.constants.field.AppChainNodeField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.application.AppChainNodeDTO;
import org.wallet.common.entity.application.AppChainNodeEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.AppChainNodeService;
import org.wallet.service.common.ResponseCode;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_APP_CHAIN_NODE)
public class AppChainNodeDubboService extends BaseDubboService implements IService{

    @Autowired
    Cache cache;

    @Autowired
    AppChainNodeService appChainNodeService;

    public ServiceResponse getAppChainNodeById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(AppChainNodeField.APP_CHAIN_NODE_ID);
        }

        return response.setResult(appChainNodeService.findOne(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllAppChainNode(ServiceRequest request, ServiceResponse response) {
        List<AppChainNodeDTO> dtoList = cache.get(AppChainNodeCache.CACHE_PREFIX, AppChainNodeCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<AppChainNodeEntity> appChainList = appChainNodeService.findAll(new Sort(Sort.Direction.ASC, AppChainNodeField.SORT));

        dtoList = appChainList.stream().map(entity -> {
            AppChainNodeDTO dto = new AppChainNodeDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(AppChainNodeCache.CACHE_PREFIX, AppChainNodeCache.ALL, dtoList, AppChainNodeCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppChainNode(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findAppChainNodePage(request, response); }

        search = null == search ? new Search() : search;

        List<AppChainNodeEntity> appChainList = appChainNodeService.findAll(search);

        List<AppChainNodeDTO> dtoList = appChainList.stream().map(entity -> {
            AppChainNodeDTO dto = new AppChainNodeDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppChainNodePage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(AppChainNodeField.PAGE); }

        PageDTO<AppChainNodeEntity> entityPage = appChainNodeService.findPage(search);

        List<AppChainNodeEntity> appChainNodeList = entityPage.getRecords();

        List<AppChainNodeDTO> dtoList = appChainNodeList.stream().map(entity -> {
            AppChainNodeDTO dto = new AppChainNodeDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addAppChainNode(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppChainNodeEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppChainNodeField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppChainNodeField.APP_CHAIN_NODE); }

        entity.setId(null);
        entity.setCreator(userId);
        entity.setUpdater(userId);
        if(null == entity.getEnable()){ entity.setEnable(true); }
        if(null == entity.getSort()){ entity.setSort(0); }
        if(null == entity.getDef()){ entity.setDef(Boolean.FALSE); }

        AppChainNodeEntity result = appChainNodeService.save(entity);
        if(null == result){ return Responses.fail(ResponseCode.DATA_DUPLICATE, String.format("数据[%s:%s][%s:%s]已存在，不能重复添加",
                AppChainNodeField.APP_CHAIN_ID, entity.getChainId(), AppChainNodeField.NAME, entity.getName())); }

        return response.setResult(result);
    }

    public ServiceResponse updateAppChainNode(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppChainNodeEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppChainNodeField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppChainNodeField.APP_CHAIN_NODE); }
        if(null == entity.getId()){ return Responses.missingParam(AppChainNodeField.APP_CHAIN_NODE_ID); }

        entity.setCoverageUpdate(true);
        entity.setUpdater(userId);

        if(Boolean.TRUE.equals(entity.getDef())){
            AppChainNodeEntity old = appChainNodeService.findOne(entity.getId());
            appChainNodeService.removeDefault(old.getChainId());
        }else{
            entity.setDef(null);
        }

        AppChainNodeEntity result = appChainNodeService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteAppChainNode(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(AppChainNodeField.APP_CHAIN_NODE_ID); }

        appChainNodeService.delete(id);

        return response;
    }
}
