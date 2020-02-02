package org.wallet.service.admin.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.cache.SysMenuCache;
import org.wallet.common.constants.field.SysMenuField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.admin.SysMenuDTO;
import org.wallet.common.entity.admin.SysMenuEntity;
import org.wallet.common.enums.admin.MenuTypeEnum;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.admin.service.SysMenuService;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_SYS_MENU)
public class SysMenuDubboService extends BaseDubboService implements IService {

    @Autowired
    Cache cache;

    @Autowired
    SysMenuService sysMenuService;

    public ServiceResponse getUserPermissions(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        if(null == userId){ return Responses.missingParam(SysMenuField.USER_ID); }
        Boolean superAdmin = request.getParam();
        superAdmin = null == superAdmin ? false : superAdmin;
        response.setResult(sysMenuService.getUserPermissions(userId, superAdmin));
        return response;
    }

    public ServiceResponse getUserMenus(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        if(null == userId){ return Responses.missingParam(SysMenuField.USER_ID); }
        Boolean superAdmin = request.getParam();
        superAdmin = null == superAdmin ? false : superAdmin;
        response.setResult(sysMenuService.getUserMenus(userId, superAdmin));
        return response;
    }

    public ServiceResponse getSysMenuById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(SysMenuField.ID);
        }

        return response.setResult(sysMenuService.findOne(id));
    }

    public ServiceResponse getSysMenuTree(ServiceRequest request, ServiceResponse response) {
        return response.setResult(sysMenuService.getSysMenuTree());
    }

    public ServiceResponse getSysMenuTreeById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(SysMenuField.ID);
        }

        return response.setResult(sysMenuService.findSysMenuTreeById(id));
    }

    public ServiceResponse findSysMenusByParentId(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(SysMenuField.ID);
        }

        return response.setResult(sysMenuService.findSysMenusByParentId(id, false));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllSysMenu(ServiceRequest request, ServiceResponse response) {
        List<SysMenuDTO> dtoList = cache.get(SysMenuCache.CACHE_PREFIX, SysMenuCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<SysMenuEntity> appChainList = sysMenuService.findAll(new Sort(Sort.Direction.ASC, SysMenuField.SORT));

        dtoList = appChainList.stream().map(entity -> {
            SysMenuDTO dto = new SysMenuDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(SysMenuCache.CACHE_PREFIX, SysMenuCache.ALL, dtoList, SysMenuCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findSysMenu(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findSysMenuPage(request, response); }

        search = null == search ? new Search() : search;

        List<SysMenuEntity> appChainList = sysMenuService.findAll(search);

        List<SysMenuDTO> dtoList = appChainList.stream().map(entity -> {
            SysMenuDTO dto = new SysMenuDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findSysMenuPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(SysMenuField.PAGE); }

        PageDTO<SysMenuEntity> entityPage = sysMenuService.findPage(search);

        List<SysMenuEntity> appChainList = entityPage.getRecords();

        List<SysMenuDTO> dtoList = appChainList.stream().map(entity -> {
            SysMenuDTO dto = new SysMenuDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addSysMenu(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        SysMenuEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(SysMenuField.USER_ID); }
        if(null == entity){ return Responses.missingParam(SysMenuField.SYS_MENU); }

        entity.setId(null);
        entity.setCreator(userId);
        entity.setUpdater(userId);
        if(null == entity.getSort()){ entity.setSort(0); }
        if(null == entity.getEnable()){ entity.setEnable(true); }
        if(entity.getType().equals(MenuTypeEnum.FUNCTION.value()) && StringUtils.isEmpty(entity.getUrl())){
            entity.setUrl(SysMenuField.EMPTY);
        }

        SysMenuEntity result = sysMenuService.save(entity);

        return response.setResult(result);
    }

    public ServiceResponse updateSysMenu(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        SysMenuEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(SysMenuField.USER_ID); }
        if(null == entity){ return Responses.missingParam(SysMenuField.SYS_MENU); }
        if(null == entity.getId()){ return Responses.missingParam(SysMenuField.SYS_MENU_ID); }

        entity.setCoverageUpdate(true);
        entity.setUpdater(userId);

        SysMenuEntity result = sysMenuService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteSysMenu(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(SysMenuField.SYS_MENU_ID); }

        List<SysMenuEntity> menuList = sysMenuService.findAll(Searchs.of(SearchFilters.eq("pid", id)));
        if(!CollectionUtils.isEmpty(menuList)){ return Responses.fail(ResponseCode.EXISTS_KEY_DATA, "该菜单包含子菜单，无法删除"); }

        sysMenuService.delete(id);

        return response;
    }
}
