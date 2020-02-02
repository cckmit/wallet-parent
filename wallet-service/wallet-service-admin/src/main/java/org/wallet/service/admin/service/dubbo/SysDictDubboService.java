package org.wallet.service.admin.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.cache.SysDictCache;
import org.wallet.common.constants.field.SysDictField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.admin.SysDictDTO;
import org.wallet.common.entity.admin.SysDictEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.admin.service.SysDictService;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_SYS_DICT)
public class SysDictDubboService extends BaseDubboService implements IService {

    @Autowired
    Cache cache;

    @Autowired
    SysDictService sysDictService;

    public ServiceResponse getSysDictById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(SysDictField.ID);
        }

        return response.setResult(sysDictService.findOne(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllSysDict(ServiceRequest request, ServiceResponse response) {
        List<SysDictDTO> dtoList = cache.get(SysDictCache.CACHE_PREFIX, SysDictCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<SysDictEntity> appChainList = sysDictService.findAll(new Sort(Sort.Direction.ASC, SysDictField.SORT));

        dtoList = appChainList.stream().map(entity -> {
            SysDictDTO dto = new SysDictDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(SysDictCache.CACHE_PREFIX, SysDictCache.ALL, dtoList, SysDictCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findSysDict(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findSysDictPage(request, response); }

        search = null == search ? new Search() : search;

        List<SysDictEntity> appChainList = sysDictService.findAll(search);

        List<SysDictDTO> dtoList = appChainList.stream().map(entity -> {
            SysDictDTO dto = new SysDictDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findSysDictPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(SysDictField.PAGE); }

        PageDTO<SysDictEntity> entityPage = sysDictService.findPage(search);

        List<SysDictEntity> appChainList = entityPage.getRecords();

        List<SysDictDTO> dtoList = appChainList.stream().map(entity -> {
            SysDictDTO dto = new SysDictDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addSysDict(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        SysDictEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(SysDictField.USER_ID); }
        if(null == entity){ return Responses.missingParam(SysDictField.SYS_DICT); }

        entity.setId(null);
        entity.setCreator(userId);
        entity.setUpdater(userId);
        if(null == entity.getSort()){ entity.setSort(0); }
        if(null == entity.getEnable()){ entity.setEnable(true); }

        SysDictEntity result = sysDictService.save(entity);

        return response.setResult(result);
    }

    public ServiceResponse updateSysDict(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        SysDictEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(SysDictField.USER_ID); }
        if(null == entity){ return Responses.missingParam(SysDictField.SYS_DICT); }
        if(null == entity.getId()){ return Responses.missingParam(SysDictField.SYS_DICT_ID); }

        entity.setCoverageUpdate(true);
        entity.setUpdater(userId);

        SysDictEntity result = sysDictService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteSysDict(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(SysDictField.SYS_DICT_ID); }

        sysDictService.delete(id);

        return response;
    }
}
