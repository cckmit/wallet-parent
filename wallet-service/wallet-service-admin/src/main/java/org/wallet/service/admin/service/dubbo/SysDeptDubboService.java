package org.wallet.service.admin.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wallet.common.constants.cache.SysDeptCache;
import org.wallet.common.constants.field.SysDeptField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.admin.SysDeptDTO;
import org.wallet.common.entity.admin.SysDeptEntity;
import org.wallet.common.entity.admin.SysRoleEntity;
import org.wallet.common.entity.admin.SysUserEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.admin.service.SysDeptService;
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
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_SYS_DEPT)
public class SysDeptDubboService extends BaseDubboService implements IService {

    @Autowired
    Cache cache;

    @Autowired
    SysDeptService sysDeptService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysRoleService sysRoleService;

    public ServiceResponse findDataScopeList(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        if(null == userId){
            return Responses.missingParam(SysDeptField.USER_ID);
        }
        Search search = new Search();
        response.setResult(sysDeptService.findDataScopeList(userId));
        return response;
    }

    public ServiceResponse getSysDeptById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(SysDeptField.ID);
        }

        return response.setResult(sysDeptService.findOne(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllSysDept(ServiceRequest request, ServiceResponse response) {
        List<SysDeptDTO> dtoList = cache.get(SysDeptCache.CACHE_PREFIX, SysDeptCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<SysDeptEntity> appChainList = sysDeptService.findAll(new Sort(Sort.Direction.ASC, SysDeptField.SORT));

        dtoList = appChainList.stream().map(entity -> {
            SysDeptDTO dto = new SysDeptDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(SysDeptCache.CACHE_PREFIX, SysDeptCache.ALL, dtoList, SysDeptCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findSysDept(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findSysDeptPage(request, response); }

        search = null == search ? new Search() : search;

        List<SysDeptEntity> appChainList = sysDeptService.findAll(search);

        List<SysDeptDTO> dtoList = appChainList.stream().map(entity -> {
            SysDeptDTO dto = new SysDeptDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findSysDeptPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(SysDeptField.PAGE); }

        PageDTO<SysDeptEntity> entityPage = sysDeptService.findPage(search);

        List<SysDeptEntity> appChainList = entityPage.getRecords();

        List<SysDeptDTO> dtoList = appChainList.stream().map(entity -> {
            SysDeptDTO dto = new SysDeptDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addSysDept(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        SysDeptEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(SysDeptField.USER_ID); }
        if(null == entity){ return Responses.missingParam(SysDeptField.SYS_DEPT); }

        entity.setId(null);
        entity.setCreator(userId);
        entity.setUpdater(userId);
        if(null == entity.getSort()){ entity.setSort(0); }

        SysDeptEntity result = sysDeptService.save(entity);

        return response.setResult(result);
    }

    public ServiceResponse updateSysDept(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        SysDeptEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(SysDeptField.USER_ID); }
        if(null == entity){ return Responses.missingParam(SysDeptField.SYS_DEPT); }
        if(null == entity.getId()){ return Responses.missingParam(SysDeptField.SYS_DEPT_ID); }

        entity.setCoverageUpdate(true);
        entity.setUpdater(userId);

        SysDeptEntity result = sysDeptService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteSysDept(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(SysDeptField.SYS_DEPT_ID); }

        List<SysUserEntity> userList = sysUserService.findAll(Searchs.of(SearchFilters.eq("deptId", id)));
        if(!CollectionUtils.isEmpty(userList)){ return Responses.fail(ResponseCode.EXISTS_KEY_DATA, "该部门包含用户信息，无法删除"); }

        List<SysRoleEntity> roleList = sysRoleService.findAll(Searchs.of(SearchFilters.eq("deptId", id)));
        if(!CollectionUtils.isEmpty(roleList)){ return Responses.fail(ResponseCode.EXISTS_KEY_DATA, "该部门包含角色信息，无法删除"); }

        sysDeptService.delete(id);

        return response;
    }
}
