package org.wallet.web.admin.controller.application;

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.application.AppTagDTO;
import org.wallet.common.entity.application.AppTagEntity;
import org.wallet.dap.common.bind.Results;
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
@RequestMapping("app/tag")
public class AppTagController extends BaseController {

    @Reference(group = DubboServiceGroup.SERVICE_APP_TAG)
    private IService appTagService;

    public AppTagController() {
        setServiceGroup(DubboServiceGroup.SERVICE_APP_TAG);
    }

    @GetMapping
    @RequiresPermissions(P.APP_TAG + P.INFO)
    public SimpleResult find(AppTagDTO dto){
        ServiceResponse response = appTagService.invoke(createRequest("findTagApp", UserUtil.getUserId(), dto));

        return Results.by(response);
    }

    @PostMapping
    @OperationLog("添加DApp标签")
    @RequiresPermissions(P.APP_TAG + P.SAVE)
    public ResponseEntity<SimpleResult> post(@RequestBody @Valid AppTagEntity entity){
        ServiceResponse response = appTagService.invoke(createRequest("addAppTag", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("修改DApp标签")
    @RequiresPermissions(P.APP_TAG + P.UPDATE)
    @RequestMapping(value = "{id:\\d{1,19}}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<SimpleResult> put(@PathVariable Long id, @RequestBody AppTagEntity entity){
        entity.setId(id);

        ServiceResponse response = appTagService.invoke(createRequest("updateAppTag", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            handleFailResponse(response);
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("删除DApp标签")
    @RequiresPermissions(P.APP_TAG + P.DELETE)
    @DeleteMapping("{id:\\d{1,19}}")
    public ResponseEntity<SimpleResult> delete(@PathVariable Long id){
        ServiceResponse response = appTagService.invoke(createRequest("deleteAppTag", UserUtil.getUserId(), id));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }
}