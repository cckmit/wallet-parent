package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.cache.AppInviteCodeCache;
import org.wallet.common.constants.field.AppInviteCodeField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.application.AppInviteCodeDTO;
import org.wallet.common.entity.application.AppInviteCodeEntity;
import org.wallet.common.enums.application.AppInviteCodeStatusEnum;
import org.wallet.common.enums.application.AppInviteCodeTypeEnum;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.dubbo.*;
import org.wallet.dap.common.utils.StringGenerator;
import org.wallet.service.application.service.AppInviteCodeService;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_APP_INVITE_CODE)
public class AppInviteCodeDubboService extends BaseDubboService implements IService{

    @Autowired
    Cache cache;

    @Autowired
    AppInviteCodeService appInviteCodeService;

    public ServiceResponse getAppInviteCodeById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(AppInviteCodeField.APP_INVITE_CODE_ID);
        }

        return response.setResult(appInviteCodeService.findOne(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllAppInviteCode(ServiceRequest request, ServiceResponse response) {
        List<AppInviteCodeDTO> dtoList = cache.get(AppInviteCodeCache.CACHE_PREFIX, AppInviteCodeCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<AppInviteCodeEntity> appChainList = appInviteCodeService.findAll(new Sort(Sort.Direction.ASC, AppInviteCodeField.ID));

        dtoList = appChainList.stream().map(entity -> {
            AppInviteCodeDTO dto = new AppInviteCodeDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(AppInviteCodeCache.CACHE_PREFIX, AppInviteCodeCache.ALL, dtoList, AppInviteCodeCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppInviteCode(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findAppInviteCodePage(request, response); }

        search = null == search ? new Search() : search;

        List<AppInviteCodeEntity> appChainList = appInviteCodeService.findAll(search);

        List<AppInviteCodeDTO> dtoList = appChainList.stream().map(entity -> {
            AppInviteCodeDTO dto = new AppInviteCodeDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppInviteCodePage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(AppInviteCodeField.PAGE); }

        PageDTO<AppInviteCodeEntity> entityPage = appInviteCodeService.findPage(search);

        List<AppInviteCodeEntity> appInviteCodeList = entityPage.getRecords();

        List<AppInviteCodeDTO> dtoList = appInviteCodeList.stream().map(entity -> {
            AppInviteCodeDTO dto = new AppInviteCodeDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addAppInviteCode(ServiceRequest request, ServiceResponse response) {
        AppInviteCodeTypeEnum type = request.getParam();

        if(null == type){ return Responses.missingParam(AppInviteCodeField.TYPE); }

        AppInviteCodeEntity entity = new AppInviteCodeEntity();

        entity.setId(null);
        entity.setCreator(AppInviteCodeField.DEFAULT_ADMIN_USER_ID);

        String code = StringGenerator.newInstance().generate(8);
        entity.setCode(code);
        entity.setStatus(AppInviteCodeStatusEnum.UNUSED);
        entity.setType(type);

        AppInviteCodeEntity result = appInviteCodeService.save(entity);

        return response.setResult(result);
    }

    public ServiceResponse updateAppInviteCode(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppInviteCodeEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppInviteCodeField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppInviteCodeField.APP_INVITE_CODE); }
        if(null == entity.getId()){ return Responses.missingParam(AppInviteCodeField.APP_INVITE_CODE_ID); }

        AppInviteCodeEntity result = appInviteCodeService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }
}
