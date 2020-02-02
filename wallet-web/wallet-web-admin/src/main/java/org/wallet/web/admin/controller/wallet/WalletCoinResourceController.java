package org.wallet.web.admin.controller.wallet;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.constants.cache.WalletCoinResourceCache;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.wallet.WalletCoinResourceDTO;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.Results;
import org.wallet.web.admin.annotation.OperationLog;
import org.wallet.web.admin.shiro.P;
import org.wallet.web.common.mvc.controller.BaseController;

import java.util.Comparator;
import java.util.List;

/**
 * @author zengfucheng
 */
@RestController
@RequestMapping("wallet/coin/resource")
public class WalletCoinResourceController extends BaseController {

    @Autowired
    private Cache cache;

    @GetMapping("all")
    @RequiresPermissions(P.WALLET_COIN + P.INFO)
	public SimpleResult all() {
        return Results.success(getResource());
	}

    @OperationLog("修改币种信息")
    @RequiresPermissions(P.WALLET_COIN + P.UPDATE)
    @RequestMapping(value = "{name}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public SimpleResult put(@PathVariable String name, @RequestBody WalletCoinResourceDTO dto){
        List<WalletCoinResourceDTO> list = getResource();

        list.forEach(resource -> {
            if(name.equals(resource.getName())){
                if(!StringUtils.isEmpty(dto.getLabel())){ resource.setLabel(dto.getLabel()); }
                if(!StringUtils.isEmpty(dto.getValue())){ resource.setValue(dto.getValue()); }
                if(!StringUtils.isEmpty(dto.getSort())){ resource.setSort(dto.getSort()); }
            }
        });

        cache.put(WalletCoinResourceCache.CACHE_PREFIX, WalletCoinResourceCache.ALL, list);

        return Results.success(list);
    }

    private List<WalletCoinResourceDTO> getResource(){
        List<WalletCoinResourceDTO> list = cache.get(WalletCoinResourceCache.CACHE_PREFIX, WalletCoinResourceCache.ALL, List.class);

        if(CollectionUtils.isEmpty(list)){ list = WalletCoinResourceDTO.getDefault(); }

        list.sort(Comparator.comparing(WalletCoinResourceDTO::getSort));

        return list;
    }

}