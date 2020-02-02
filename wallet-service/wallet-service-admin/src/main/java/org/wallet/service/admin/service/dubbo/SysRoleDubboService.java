package org.wallet.service.admin.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.cache.SysRoleCache;
import org.wallet.common.constants.field.SysRoleField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.admin.SysRoleDTO;
import org.wallet.common.entity.admin.SysRoleEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.admin.service.SysRoleService;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_SYS_ROLE)
public class SysRoleDubboService extends BaseDubboService implements IService {

    @Autowired
    Cache cache;

    @Autowired
    SysRoleService sysRoleService;

    public ServiceResponse getSysRoleById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(SysRoleField.ID);
        }

        return response.setResult(sysRoleService.findDTOById(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllSysRole(ServiceRequest request, ServiceResponse response) {
        List<SysRoleDTO> dtoList = cache.get(SysRoleCache.CACHE_PREFIX, SysRoleCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<SysRoleEntity> appChainList = sysRoleService.findAll(new Sort(Sort.Direction.ASC, SysRoleField.SORT));

        dtoList = appChainList.stream().map(entity -> {
            SysRoleDTO dto = new SysRoleDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(SysRoleCache.CACHE_PREFIX, SysRoleCache.ALL, dtoList, SysRoleCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findSysRole(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findSysRolePage(request, response); }

        search = null == search ? new Search() : search;

        List<SysRoleEntity> appChainList = sysRoleService.findAll(search);

        List<SysRoleDTO> dtoList = appChainList.stream().map(entity -> {
            SysRoleDTO dto = new SysRoleDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findSysRolePage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(SysRoleField.PAGE); }

        PageDTO<SysRoleEntity> entityPage = sysRoleService.findPage(search);

        List<SysRoleEntity> appChainList = entityPage.getRecords();

        List<SysRoleDTO> dtoList = appChainList.stream().map(entity -> {
            SysRoleDTO dto = new SysRoleDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addSysRole(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        SysRoleDTO dto = request.getParam();

        if(null == userId){ return Responses.missingParam(SysRoleField.USER_ID); }
        if(null == dto){ return Responses.missingParam(SysRoleField.SYS_ROLE); }

        SysRoleEntity result = sysRoleService.save(dto, userId);

        return response.setResult(result);
    }

    public ServiceResponse updateSysRole(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        SysRoleDTO dto = request.getParam();

        if(null == userId){ return Responses.missingParam(SysRoleField.USER_ID); }
        if(null == dto){ return Responses.missingParam(SysRoleField.SYS_ROLE); }
        if(null == dto.getId()){ return Responses.missingParam(SysRoleField.SYS_ROLE_ID); }

        SysRoleEntity result = sysRoleService.save(dto, userId);

        if(null == result){ return Responses.notFoundData(dto.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteSysRole(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(SysRoleField.SYS_ROLE_ID); }

        sysRoleService.delete(id);

        return response;
    }
}
