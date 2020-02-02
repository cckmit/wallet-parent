package org.wallet.web.admin.controller.wallet;

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.wallet.WalletCoinDTO;
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
@RequestMapping("wallet/coin")
public class WalletCoinController extends BaseController {

    @Reference(group = DubboServiceGroup.SERVICE_WALLET_COIN)
    private IService walletCoinService;

    public WalletCoinController() {
        setServiceGroup(DubboServiceGroup.SERVICE_WALLET_COIN);
    }

    @GetMapping("{id:\\d{1,19}}")
    @RequiresPermissions(P.WALLET_COIN + P.INFO)
    public SimpleResult findById(@PathVariable Long id){
        ServiceResponse response = walletCoinService.invoke(createRequest("getWalletCoinById", UserUtil.getUserId(), id));

        return Results.by(response);
    }

    @GetMapping
    @RequiresPermissions(P.WALLET_COIN + P.INFO)
    public SimpleResult find(WalletCoinDTO dto, PageDTO page, SortDTO sort){
        Search search = new Search(dto, page, sort);

        ServiceResponse response = walletCoinService.invoke(createRequest("findWalletCoin", UserUtil.getUserId(), search));

        return Results.by(response);
    }

    @GetMapping("all")
    @RequiresPermissions(P.WALLET_COIN + P.INFO)
	public SimpleResult all() {
        ServiceResponse response = walletCoinService.invoke(createRequest("findAllWalletCoin"));

        return Results.by(response);
	}

    @PostMapping
    @OperationLog("添加币种信息")
    @RequiresPermissions(P.WALLET_COIN + P.SAVE)
    public ResponseEntity<SimpleResult> post(@RequestBody @Valid WalletCoinDTO dto){
        ServiceResponse response = walletCoinService.invoke(createRequest("addWalletCoin", UserUtil.getUserId(), dto));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("修改币种信息")
    @RequiresPermissions(P.WALLET_COIN + P.UPDATE)
    @RequestMapping(value = "{id:\\d{1,19}}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<SimpleResult> put(@PathVariable Long id, @RequestBody WalletCoinDTO dto){
        dto.setId(id);

        ServiceResponse response = walletCoinService.invoke(createRequest("updateWalletCoin", UserUtil.getUserId(), dto));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            handleFailResponse(response);
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("删除币种信息")
    @RequiresPermissions(P.WALLET_COIN + P.DELETE)
    @DeleteMapping("{id:\\d{1,19}}")
    public ResponseEntity<SimpleResult> delete(@PathVariable Long id){
        ServiceResponse response = walletCoinService.invoke(createRequest("deleteWalletCoin", UserUtil.getUserId(), id));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }
}