package org.wallet.web.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.TableExtAttrDTO;
import org.wallet.common.entity.TableExtAttrEntity;
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
@RequestMapping("table/ext/attr")
public class TableExtAttrController extends BaseController {

    @Reference(group = DubboServiceGroup.SERVICE_TABLE_EXT_ATTR)
    private IService tableExtAttrService;

    public TableExtAttrController() {
        setServiceGroup(DubboServiceGroup.SERVICE_TABLE_EXT_ATTR);
    }

    @GetMapping("{id:\\d{1,19}}")
    @RequiresPermissions(P.TABLE_EXT_ATTR + P.INFO)
    public SimpleResult findById(@PathVariable Long id){
        ServiceResponse response = tableExtAttrService.invoke(createRequest("getTableExtAttrById", UserUtil.getUserId(), id));

        return Results.by(response);
    }

    @GetMapping
    @RequiresPermissions(P.TABLE_EXT_ATTR + P.INFO)
    public SimpleResult find(TableExtAttrDTO dto, PageDTO page, SortDTO sort){
        Search search = new Search(dto, page, sort);

        ServiceResponse response = tableExtAttrService.invoke(createRequest("findTableExtAttr", UserUtil.getUserId(), search));

        return Results.by(response);
    }

    @GetMapping("all")
    @RequiresPermissions(P.TABLE_EXT_ATTR + P.INFO)
	public SimpleResult all() {
        ServiceResponse response = tableExtAttrService.invoke(createRequest("findAllTableExtAttr"));

        return Results.by(response);
	}

    @PostMapping
    @OperationLog("添加业务扩展属性")
    @RequiresPermissions(P.TABLE_EXT_ATTR + P.SAVE)
    public ResponseEntity<SimpleResult> post(@RequestBody @Valid TableExtAttrEntity entity){
        ServiceResponse response = tableExtAttrService.invoke(createRequest("addTableExtAttr", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("修改业务扩展属性")
    @RequiresPermissions(P.TABLE_EXT_ATTR + P.UPDATE)
    @RequestMapping(value = "{id:\\d{1,19}}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<SimpleResult> put(@PathVariable Long id, @RequestBody TableExtAttrEntity entity){
        entity.setId(id);

        ServiceResponse response = tableExtAttrService.invoke(createRequest("updateTableExtAttr", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            handleFailResponse(response);
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("删除业务扩展属性")
    @RequiresPermissions(P.TABLE_EXT_ATTR + P.DELETE)
    @DeleteMapping("{id:\\d{1,19}}")
    public ResponseEntity<SimpleResult> delete(@PathVariable Long id){
        ServiceResponse response = tableExtAttrService.invoke(createRequest("deleteTableExtAttr", UserUtil.getUserId(), id));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }
}