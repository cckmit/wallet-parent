package org.wallet.web.admin.controller.application;

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.application.AppChainDTO;
import org.wallet.common.entity.application.AppChainEntity;
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
@RequestMapping("app/chain")
public class AppChainController extends BaseController {

    @Reference(group = DubboServiceGroup.SERVICE_APP_CHAIN)
    private IService appChainService;

    public AppChainController() {
        setServiceGroup(DubboServiceGroup.SERVICE_APP_CHAIN);
    }

    @GetMapping("{id:\\d{1,19}}")
    @RequiresPermissions(P.APP_CHAIN + P.INFO)
    public SimpleResult findById(@PathVariable Long id){
        ServiceResponse response = appChainService.invoke(createRequest("getAppChainById", UserUtil.getUserId(), id));

        return Results.by(response);
    }

    @GetMapping
    @RequiresPermissions(P.APP_CHAIN + P.INFO)
    public SimpleResult find(AppChainDTO dto, PageDTO page, SortDTO sort){
        Search search = new Search(dto, page, sort);

        ServiceResponse response = appChainService.invoke(createRequest("findAppChain", UserUtil.getUserId(), search));

        return Results.by(response);
    }

    @GetMapping("all")
    @RequiresPermissions(P.APP_CHAIN + P.INFO)
	public SimpleResult all() {
        ServiceResponse response = appChainService.invoke(createRequest("findAdminAppChain"));

        return Results.by(response);
	}

    @PostMapping
    @OperationLog("添加DApp主链")
    @RequiresPermissions(P.APP_CHAIN + P.SAVE)
    public ResponseEntity<SimpleResult> post(@RequestBody @Valid AppChainEntity entity){
        ServiceResponse response = appChainService.invoke(createRequest("addAppChain", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("修改DApp主链")
    @RequiresPermissions(P.APP_CHAIN + P.UPDATE)
    @RequestMapping(value = "{id:\\d{1,19}}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<SimpleResult> put(@PathVariable Long id, @RequestBody AppChainEntity entity){
        entity.setId(id);

        ServiceResponse response = appChainService.invoke(createRequest("updateAppChain", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            handleFailResponse(response);
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("删除DApp主链")
    @RequiresPermissions(P.APP_CHAIN + P.DELETE)
    @DeleteMapping("{id:\\d{1,19}}")
    public ResponseEntity<SimpleResult> delete(@PathVariable Long id){
        ServiceResponse response = appChainService.invoke(createRequest("deleteAppChain", UserUtil.getUserId(), id));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }
}