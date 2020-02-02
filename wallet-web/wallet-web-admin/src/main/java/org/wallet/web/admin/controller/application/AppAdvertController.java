package org.wallet.web.admin.controller.application;

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.application.AppAdvertDTO;
import org.wallet.common.entity.application.AppAdvertEntity;
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
@RequestMapping("app/advert")
public class AppAdvertController extends BaseController {

    @Reference(group = DubboServiceGroup.SERVICE_APP_ADVERT)
    private IService appAdvertService;

    public AppAdvertController() {
        setServiceGroup(DubboServiceGroup.SERVICE_APP_ADVERT);
    }

    @GetMapping("{id:\\d{1,19}}")
    @RequiresPermissions(P.APP_ADVERT + P.INFO)
    public SimpleResult findById(@PathVariable Long id){
        ServiceResponse response = appAdvertService.invoke(createRequest("getAppAdvertById", UserUtil.getUserId(), id));

        return Results.by(response);
    }

    @GetMapping
    @RequiresPermissions(P.APP_ADVERT + P.INFO)
    public SimpleResult find(AppAdvertDTO dto, PageDTO page, SortDTO sort){
        Search search = new Search(dto, page, sort);

        ServiceResponse response = appAdvertService.invoke(createRequest("findAppAdvert", UserUtil.getUserId(), search));

        return Results.by(response);
    }

    @GetMapping("all")
    @RequiresPermissions(P.APP_ADVERT + P.INFO)
	public SimpleResult all() {
        ServiceResponse response = appAdvertService.invoke(createRequest("findAdminAppAdvert"));

        return Results.by(response);
	}

    @PostMapping
    @OperationLog("添加DApp广告位")
    @RequiresPermissions(P.APP_ADVERT + P.SAVE)
    public ResponseEntity<SimpleResult> post(@RequestBody @Valid AppAdvertEntity entity){
        ServiceResponse response = appAdvertService.invoke(createRequest("addAppAdvert", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("修改DApp广告位")
    @RequiresPermissions(P.APP_ADVERT + P.UPDATE)
    @RequestMapping(value = "{id:\\d{1,19}}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<SimpleResult> put(@PathVariable Long id, @RequestBody AppAdvertEntity entity){
        entity.setId(id);

        ServiceResponse response = appAdvertService.invoke(createRequest("updateAppAdvert", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            handleFailResponse(response);
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("删除DApp广告位")
    @RequiresPermissions(P.APP_ADVERT + P.DELETE)
    @DeleteMapping("{id:\\d{1,19}}")
    public ResponseEntity<SimpleResult> delete(@PathVariable Long id){
        ServiceResponse response = appAdvertService.invoke(createRequest("deleteAppAdvert", UserUtil.getUserId(), id));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }
}