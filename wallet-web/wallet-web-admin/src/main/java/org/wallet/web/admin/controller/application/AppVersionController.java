package org.wallet.web.admin.controller.application;

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.application.AppVersionDTO;
import org.wallet.common.entity.application.AppVersionEntity;
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
@RequestMapping("app/version")
public class AppVersionController extends BaseController {

    @Reference(group = DubboServiceGroup.SERVICE_APP_VERSION)
    private IService appVersionService;

    public AppVersionController() {
        setServiceGroup(DubboServiceGroup.SERVICE_APP_VERSION);
    }

    @GetMapping("{id:\\d{1,19}}")
    @RequiresPermissions(P.APP_VERSION + P.INFO)
    public SimpleResult findById(@PathVariable Long id){
        ServiceResponse response = appVersionService.invoke(createRequest("getAppVersionById", UserUtil.getUserId(), id));

        return Results.by(response);
    }

    @GetMapping
    @RequiresPermissions(P.APP_VERSION + P.INFO)
    public SimpleResult find(AppVersionDTO dto, PageDTO page, SortDTO sort){
        Search search = new Search(dto, page, sort);

        ServiceResponse response = appVersionService.invoke(createRequest("findAppVersion", UserUtil.getUserId(), search));

        return Results.by(response);
    }

    @GetMapping("all")
    @RequiresPermissions(P.APP_VERSION + P.INFO)
	public SimpleResult all() {
        ServiceResponse response = appVersionService.invoke(createRequest("findAllAppVersion"));

        return Results.by(response);
	}

    @PostMapping
    @OperationLog("添加App版本")
    @RequiresPermissions(P.APP_VERSION + P.SAVE)
    public ResponseEntity<SimpleResult> post(@RequestBody @Valid AppVersionEntity entity){
        ServiceResponse response = appVersionService.invoke(createRequest("addAppVersion", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("修改App版本")
    @RequiresPermissions(P.APP_VERSION + P.UPDATE)
    @RequestMapping(value = "{id:\\d{1,19}}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<SimpleResult> put(@PathVariable Long id, @RequestBody AppVersionEntity entity){
        entity.setId(id);

        ServiceResponse response = appVersionService.invoke(createRequest("updateAppVersion", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            handleFailResponse(response);
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("删除App版本")
    @RequiresPermissions(P.APP_VERSION + P.DELETE)
    @DeleteMapping("{id:\\d{1,19}}")
    public ResponseEntity<SimpleResult> delete(@PathVariable Long id){
        ServiceResponse response = appVersionService.invoke(createRequest("deleteAppVersion", UserUtil.getUserId(), id));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }
}