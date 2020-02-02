package org.wallet.web.admin.controller.wallet;

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.constants.field.WalletCoinConfigField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.wallet.WalletCoinConfigDTO;
import org.wallet.common.entity.wallet.WalletCoinConfigEntity;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author zengfucheng
 */
@RestController
@RequestMapping("wallet/coin-config")
public class WalletCoinConfigController extends BaseController {

    @Reference(group = DubboServiceGroup.SERVICE_WALLET_COIN_CONFIG)
    private IService walletCoinConfigService;

    public WalletCoinConfigController() {
        setServiceGroup(DubboServiceGroup.SERVICE_WALLET_COIN_CONFIG);
    }

    @GetMapping("{id:\\d{1,19}}")
    @RequiresPermissions(P.WALLET_COIN_CONFIG + P.INFO)
    public SimpleResult findById(@PathVariable Long id){
        ServiceResponse response = walletCoinConfigService.invoke(createRequest("getWalletCoinConfigById", UserUtil.getUserId(), id));

        return Results.by(response);
    }

    @GetMapping
    @RequiresPermissions(P.WALLET_COIN_CONFIG + P.INFO)
    public SimpleResult find(WalletCoinConfigDTO dto, PageDTO page, SortDTO sort){
        Search search = new Search(dto, page, sort);

        ServiceResponse response = walletCoinConfigService.invoke(createRequest("findWalletCoinConfig", UserUtil.getUserId(), search));

        return Results.by(response);
    }

    @GetMapping("all")
    @RequiresPermissions(P.WALLET_COIN_CONFIG + P.INFO)
	public SimpleResult all() {
        ServiceResponse response = walletCoinConfigService.invoke(createRequest("findAllWalletCoinConfig"));

        return Results.by(response);
	}

    @GetMapping("price")
    @RequiresPermissions(P.WALLET_COIN_CONFIG + P.INFO)
	public SimpleResult price(Long chainId, PageDTO page) {
        Map<String, Object> param = new HashMap<>(2);

        param.put(WalletCoinConfigField.APP_CHAIN_ID, chainId);
        param.put(WalletCoinConfigField.PAGE, page);

        ServiceResponse response = walletCoinConfigService.invoke(createRequest("findWalletCoinPrice", UserUtil.getUserId(), param));

        return Results.by(response);
	}

    @PostMapping
    @OperationLog("添加币种配置")
    @RequiresPermissions(P.WALLET_COIN_CONFIG + P.SAVE)
    public ResponseEntity<SimpleResult> post(@RequestBody @Valid WalletCoinConfigEntity entity){
        ServiceResponse response = walletCoinConfigService.invoke(createRequest("addWalletCoinConfig", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("修改币种配置")
    @RequiresPermissions(P.WALLET_COIN_CONFIG + P.UPDATE)
    @RequestMapping(value = "{id:\\d{1,19}}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<SimpleResult> put(@PathVariable Long id, @RequestBody WalletCoinConfigEntity entity){
        entity.setId(id);

        ServiceResponse response = walletCoinConfigService.invoke(createRequest("updateWalletCoinConfig", UserUtil.getUserId(), entity));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            handleFailResponse(response);
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("删除币种配置")
    @RequiresPermissions(P.WALLET_COIN_CONFIG + P.DELETE)
    @DeleteMapping("{id:\\d{1,19}}")
    public ResponseEntity<SimpleResult> delete(@PathVariable Long id){
        ServiceResponse response = walletCoinConfigService.invoke(createRequest("deleteWalletCoinConfig", UserUtil.getUserId(), id));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }
}