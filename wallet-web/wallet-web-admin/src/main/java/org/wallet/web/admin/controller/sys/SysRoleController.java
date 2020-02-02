package org.wallet.web.admin.controller.sys;

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.admin.SysRoleDTO;
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
@RequestMapping("sys/role")
public class SysRoleController extends BaseController {

    @Reference(group = DubboServiceGroup.SERVICE_SYS_ROLE)
    private IService sysRoleService;

    public SysRoleController() {
        setServiceGroup(DubboServiceGroup.SERVICE_SYS_ROLE);
    }

    @GetMapping("{id:\\d{1,19}}")
    @RequiresPermissions(P.SYS_ROLE + P.INFO)
    public SimpleResult findById(@PathVariable Long id){
        ServiceResponse response = sysRoleService.invoke(createRequest("getSysRoleById", UserUtil.getUserId(), id));

        return Results.by(response);
    }

    @GetMapping
    @RequiresPermissions(P.SYS_ROLE + P.INFO)
    public SimpleResult find(SysRoleDTO dto, PageDTO page, SortDTO sort){
        Search search = new Search(dto, page, sort);

        ServiceResponse response = sysRoleService.invoke(createRequest("findSysRole", UserUtil.getUserId(), search));

        if(response.success()){
            return Results.success(response.getResult());
        }else{
            return Results.fail(response.getRespMsg());
        }
    }

    @PostMapping
    @OperationLog("添加角色")
    @RequiresPermissions(P.SYS_ROLE + P.SAVE)
    public ResponseEntity<SimpleResult> post(@RequestBody @Valid SysRoleDTO dto){
        ServiceResponse response = sysRoleService.invoke(createRequest("addSysRole", UserUtil.getUserId(), dto));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("修改角色")
    @RequiresPermissions(P.SYS_ROLE + P.UPDATE)
    @RequestMapping(value = "{id:\\d{1,19}}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<SimpleResult> put(@PathVariable Long id, @RequestBody SysRoleDTO dto){
        dto.setId(id);

        ServiceResponse response = sysRoleService.invoke(createRequest("updateSysRole", UserUtil.getUserId(), dto));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            handleFailResponse(response);
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("删除角色")
    @RequiresPermissions(P.SYS_ROLE + P.DELETE)
    @DeleteMapping("{id:\\d{1,19}}")
    public ResponseEntity<SimpleResult> delete(@PathVariable Long id){
        ServiceResponse response = sysRoleService.invoke(createRequest("deleteSysRole", UserUtil.getUserId(), id));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }
}