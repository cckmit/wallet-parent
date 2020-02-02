package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.cache.AppMessageCache;
import org.wallet.common.constants.field.AppMessageField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.application.AppMessageDTO;
import org.wallet.common.entity.application.AppMessageEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.AppMessageService;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_APP_MESSAGE)
public class AppMessageDubboService extends BaseDubboService implements IService{

    @Autowired
    Cache cache;

    @Autowired
    AppMessageService appMessageService;

    public ServiceResponse getAppMessageById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(AppMessageField.APP_MESSAGE_ID);
        }

        return response.setResult(appMessageService.findOne(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllAppMessage(ServiceRequest request, ServiceResponse response) {
        List<AppMessageDTO> dtoList = cache.get(AppMessageCache.CACHE_PREFIX, AppMessageCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<AppMessageEntity> appChainList = appMessageService.findAll(new Sort(Sort.Direction.ASC, AppMessageField.SORT));

        dtoList = appChainList.stream().map(entity -> {
            AppMessageDTO dto = new AppMessageDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(AppMessageCache.CACHE_PREFIX, AppMessageCache.ALL, dtoList, AppMessageCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppMessage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findAppMessagePage(request, response); }

        search = null == search ? new Search() : search;

        List<AppMessageEntity> appChainList = appMessageService.findAll(search);

        List<AppMessageDTO> dtoList = appChainList.stream().map(entity -> {
            AppMessageDTO dto = new AppMessageDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppMessagePage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(AppMessageField.PAGE); }

        PageDTO<AppMessageEntity> entityPage = appMessageService.findPage(search);

        List<AppMessageEntity> appMessageList = entityPage.getRecords();

        List<AppMessageDTO> dtoList = appMessageList.stream().map(entity -> {
            AppMessageDTO dto = new AppMessageDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addAppMessage(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppMessageEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppMessageField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppMessageField.APP_MESSAGE); }
        if(null == entity.getUrl()){ entity.setUrl(AppMessageField.EMPTY);}

        entity.setId(null);
        entity.setCreator(userId);

        AppMessageEntity result = appMessageService.save(entity);

        return response.setResult(result);
    }

    public ServiceResponse updateAppMessage(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppMessageEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppMessageField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppMessageField.APP_MESSAGE); }
        if(null == entity.getId()){ return Responses.missingParam(AppMessageField.APP_MESSAGE_ID); }

        entity.setCoverageUpdate(true);

        AppMessageEntity result = appMessageService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteAppMessage(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(AppMessageField.APP_MESSAGE_ID); }

        appMessageService.delete(id);

        return response;
    }
}
