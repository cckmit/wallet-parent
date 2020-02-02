package org.wallet.service.admin.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.cache.SysUserCache;
import org.wallet.common.constants.field.SysUserField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.admin.SysUserDTO;
import org.wallet.common.entity.admin.SysUserEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.admin.service.SysRoleService;
import org.wallet.service.admin.service.SysUserService;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_SYS_USER)
public class SysUserDubboService extends BaseDubboService implements IService {

    @Autowired
    Cache cache;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysUserService sysUserService;

    public ServiceResponse findSysUserByUsername(ServiceRequest request, ServiceResponse response) {
        String username = request.getParam();
        if(StringUtils.isEmpty(username)){
            return Responses.missingParam(SysUserField.SYS_USER_USERNAME);
        }
        response.setResult(sysUserService.findOne(Searchs.of(SearchFilters.eq(SysUserField.SYS_USER_USERNAME, username))));
        return response;
    }

    public ServiceResponse getSysUserById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(SysUserField.ID);
        }

        SysUserDTO dto = sysUserService.findDTOById(id);

        return response.setResult(dto);
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllSysUser(ServiceRequest request, ServiceResponse response) {
        List<SysUserDTO> dtoList = cache.get(SysUserCache.CACHE_PREFIX, SysUserCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<SysUserEntity> appChainList = sysUserService.findAll(new Sort(Sort.Direction.ASC, SysUserField.SYS_USER_USERNAME));

        dtoList = appChainList.stream().map(entity -> {
            SysUserDTO dto = new SysUserDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(SysUserCache.CACHE_PREFIX, SysUserCache.ALL, dtoList, SysUserCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findSysUser(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findSysUserPage(request, response); }

        search = null == search ? new Search() : search;

        List<SysUserEntity> appChainList = sysUserService.findAll(search);

        List<SysUserDTO> dtoList = appChainList.stream().map(entity -> {
            SysUserDTO dto = new SysUserDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findSysUserPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(SysUserField.PAGE); }

        PageDTO<SysUserEntity> entityPage = sysUserService.findPage(search);

        List<SysUserEntity> appChainList = entityPage.getRecords();

        List<SysUserDTO> dtoList = appChainList.stream().map(entity -> {
            SysUserDTO dto = new SysUserDTO();
            BeanUtil.copyProperties(entity, dto);
            dto.setRoleId(sysRoleService.findRoleIdByUserId(dto.getId()));
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addSysUser(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        SysUserDTO dto = request.getParam();

        if(null == userId){ return Responses.missingParam(SysUserField.USER_ID); }
        if(null == dto){ return Responses.missingParam(SysUserField.SYS_USER); }

        if(StringUtils.isEmpty(dto.getMobile())){
            dto.setMobile(SysUserField.EMPTY);
        }

        SysUserEntity result = sysUserService.save(dto, userId);

        if(null == result){ return Responses.fail(org.wallet.service.common.ResponseCode.DATA_DUPLICATE, String.format("数据[%s:%s]已存在，不能重复添加",
                SysUserField.SYS_USER_USERNAME, dto.getUsername())); }

        result.setPassword(null);

        return response.setResult(result);
    }

    public ServiceResponse updateSysUser(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        SysUserDTO dto = request.getParam();

        if(null == userId){ return Responses.missingParam(SysUserField.USER_ID); }
        if(null == dto){ return Responses.missingParam(SysUserField.SYS_USER); }
        if(null == dto.getId()){ return Responses.missingParam(SysUserField.SYS_USER_ID); }

        SysUserEntity result = sysUserService.save(dto, userId);

        if(null == result){ return Responses.notFoundData(dto.getId()); }

        result.setPassword(null);

        return response.setResult(result);
    }

    public ServiceResponse deleteSysUser(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(SysUserField.SYS_USER_ID); }

        SysUserEntity user = sysUserService.findOne(id);

        if(null == user){
            return Responses.notFoundData("id", id);
        }else if(user.getSuperAdmin()){
            return Responses.illegalParam("超级管理员不可删除");
        }

        sysUserService.delete(id);

        return response;
    }
}
