package org.wallet.web.admin.controller.sys;

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.admin.SysMenuDTO;
import org.wallet.common.entity.admin.SysMenuEntity;
import org.wallet.dap.common.bind.Results;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.web.admin.annotation.OperationLog;
import org.wallet.web.admin.shiro.P;
import org.wallet.web.admin.utils.UserUtil;
import org.wallet.web.common.mvc.controller.BaseController;

import javax.validation.Valid;

/**
 * @author zengfucheng
 */
@RestController
@RequestMapping("sys/menu")
public class SysMenuController extends BaseController {

    @Reference(group = DubboServiceGroup.SERVICE_SYS_MENU)
    private IService sysMenuService;

    public SysMenuController() {
        setServiceGroup(DubboServiceGroup.SERVICE_SYS_MENU);
    }

    @GetMapping("{id:\\d{1,19}}")
    @RequiresPermissions(P.SYS_MENU + P.INFO)
    public SimpleResult findById(@PathVariable Long id){
        ServiceResponse response = sysMenuService.invoke(createRequest("getSysMenuById", UserUtil.getUserId(), id));

        return Results.by(response);
    }

    @GetMapping("{id:\\d{1,19}}/tree")
    @RequiresPermissions(P.SYS_MENU + P.INFO)
    public SimpleResult treeById(@PathVariable Long id){
        ServiceResponse response = sysMenuService.invoke(createRequest("getSysMenuTreeById", UserUtil.getUserId(), id));

        return Results.by(response);
    }

    @GetMapping("{id:\\d{1,19}}/children")
    @RequiresPermissions(P.SYS_MENU + P.INFO)
    public SimpleResult children(@PathVariable Long id){
        ServiceResponse response = sysMenuService.invoke(createRequest("findSysMenusByParentId", UserUtil.getUserId(), id));

        if(response.success()){
            return Results.success(response.getResult());
        }else{
            return Results.fail(response.getRespMsg());
        }
    }

    @GetMapping("all/tree")
    @RequiresPermissions(P.SYS_MENU + P.INFO)
    public SimpleResult tree(){
        ServiceResponse response = sysMenuService.invoke(createRequest("getSysMenuTree", UserUtil.getUserId()));

        return Results.by(response);
    }

    @GetMapping
    @RequiresPermissions(P.SYS_MENU + P.INFO)
    public SimpleResult find(SysMenuDTO dto, PageDTO page, SortDTO sort){
        Search search = new Search(dto, page, sort);

        ServiceResponse response = sysMenuService.invoke(createRequest("findSysMenu", UserUtil.getUserId(), search));

        return Results.by(response);
    }

    @PostMapping
    @OperationLog("添加菜单")
    @RequiresPermissions(P.SYS_MENU + P.SAVE)
    public ResponseEntity<SimpleResult> post(@RequestBody @Valid SysMenuEntity entity){
        ServiceResponse response = sysMenuService.invoke(createRequest("addSysMenu", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("修改菜单")
    @RequiresPermissions(P.SYS_MENU + P.UPDATE)
    @RequestMapping(value = "{id:\\d{1,19}}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<SimpleResult> put(@PathVariable Long id, @RequestBody SysMenuEntity entity){
        entity.setId(id);

        ServiceResponse response = sysMenuService.invoke(createRequest("updateSysMenu", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            handleFailResponse(response);
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("删除菜单")
    @RequiresPermissions(P.SYS_MENU + P.DELETE)
    @DeleteMapping("{id:\\d{1,19}}")
    public ResponseEntity<SimpleResult> delete(@PathVariable Long id){
        ServiceResponse response = sysMenuService.invoke(createRequest("deleteSysMenu", UserUtil.getUserId(), id));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }
}