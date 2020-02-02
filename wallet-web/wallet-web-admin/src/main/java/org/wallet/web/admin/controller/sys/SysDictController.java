package org.wallet.web.admin.controller.sys;

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.admin.SysDictDTO;
import org.wallet.common.entity.admin.SysDictEntity;
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
@RequestMapping("sys/dict")
public class SysDictController extends BaseController {

    @Reference(group = DubboServiceGroup.SERVICE_SYS_DICT)
    private IService sysDictService;

    public SysDictController() {
        setServiceGroup(DubboServiceGroup.SERVICE_SYS_DICT);
    }

    @GetMapping("{id:\\d{1,19}}")
    @RequiresPermissions(P.SYS_DICT + P.INFO)
    public SimpleResult findById(@PathVariable Long id){
        ServiceResponse response = sysDictService.invoke(createRequest("getSysDictById", UserUtil.getUserId(), id));

        return Results.by(response);
    }

    @GetMapping
    @RequiresPermissions(P.SYS_DICT + P.INFO)
    public SimpleResult find(SysDictDTO dto, PageDTO page, SortDTO sort){
        Search search = new Search(dto, page, sort);

        ServiceResponse response = sysDictService.invoke(createRequest("findSysDict", UserUtil.getUserId(), search));

        return Results.by(response);
    }

    @PostMapping
    @OperationLog("添加字典")
    @RequiresPermissions(P.SYS_DICT + P.SAVE)
    public ResponseEntity<SimpleResult> post(@RequestBody @Valid SysDictEntity entity){
        ServiceResponse response = sysDictService.invoke(createRequest("addSysDict", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("修改字典")
    @RequiresPermissions(P.SYS_DICT + P.UPDATE)
    @RequestMapping(value = "{id:\\d{1,19}}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<SimpleResult> put(@PathVariable Long id, @RequestBody SysDictEntity entity){
        entity.setId(id);

        ServiceResponse response = sysDictService.invoke(createRequest("updateSysDict", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            handleFailResponse(response);
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("删除字典")
    @RequiresPermissions(P.SYS_DICT + P.DELETE)
    @DeleteMapping("{id:\\d{1,19}}")
    public ResponseEntity<SimpleResult> delete(@PathVariable Long id){
        ServiceResponse response = sysDictService.invoke(createRequest("deleteSysDict", UserUtil.getUserId(), id));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }
}