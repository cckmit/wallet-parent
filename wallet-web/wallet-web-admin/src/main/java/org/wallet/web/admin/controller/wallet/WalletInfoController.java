package org.wallet.web.admin.controller.wallet;

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.wallet.WalletInfoDTO;
import org.wallet.common.entity.wallet.WalletInfoEntity;
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
@RequestMapping("wallet/info")
public class WalletInfoController extends BaseController {

    @Reference(group = DubboServiceGroup.SERVICE_WALLET_INFO)
    private IService walletInfoService;

    public WalletInfoController() {
        setServiceGroup(DubboServiceGroup.SERVICE_WALLET_INFO);
    }

    @GetMapping("{id:\\d{1,19}}")
    @RequiresPermissions(P.WALLET_INFO + P.INFO)
    public SimpleResult findById(@PathVariable Long id){
        ServiceResponse response = walletInfoService.invoke(createRequest("getWalletInfoById", UserUtil.getUserId(), id));

        return Results.by(response);
    }

    @GetMapping
    @RequiresPermissions(P.WALLET_INFO + P.INFO)
    public SimpleResult find(WalletInfoDTO dto, PageDTO page, SortDTO sort){
        Search search = new Search(dto, page, sort);

        ServiceResponse response = walletInfoService.invoke(createRequest("findWalletInfo", UserUtil.getUserId(), search));

        return Results.by(response);
    }

    @GetMapping("all")
    @RequiresPermissions(P.WALLET_INFO + P.INFO)
	public SimpleResult all() {
        ServiceResponse response = walletInfoService.invoke(createRequest("findAllWalletInfo"));

        return Results.by(response);
	}

    @PostMapping
    @OperationLog("添加钱包信息")
    @RequiresPermissions(P.WALLET_INFO + P.SAVE)
    public ResponseEntity<SimpleResult> post(@RequestBody @Valid WalletInfoEntity entity){
        ServiceResponse response = walletInfoService.invoke(createRequest("addWalletInfo", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("修改钱包信息")
    @RequiresPermissions(P.WALLET_INFO + P.UPDATE)
    @RequestMapping(value = "{id:\\d{1,19}}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<SimpleResult> put(@PathVariable Long id, @RequestBody WalletInfoEntity entity){
        entity.setId(id);

        ServiceResponse response = walletInfoService.invoke(createRequest("updateWalletInfo", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            handleFailResponse(response);
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("删除钱包信息")
    @RequiresPermissions(P.WALLET_INFO + P.DELETE)
    @DeleteMapping("{id:\\d{1,19}}")
    public ResponseEntity<SimpleResult> delete(@PathVariable Long id){
        ServiceResponse response = walletInfoService.invoke(createRequest("deleteWalletInfo", UserUtil.getUserId(), id));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }
}