package org.wallet.web.application.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.constants.cache.WalletCache;
import org.wallet.common.constants.cache.WalletCoinResourceCache;
import org.wallet.common.constants.field.WalletPayConfigField;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.block.BlockResult;
import org.wallet.common.dto.wallet.WalletCoinOrderDTO;
import org.wallet.common.dto.wallet.WalletCoinResourceDTO;
import org.wallet.common.dto.wallet.WalletTransferLogDTO;
import org.wallet.common.dto.wallet.req.CreateCoinAccountReqDTO;
import org.wallet.common.dto.wallet.req.FindCoinInfoReqDTO;
import org.wallet.common.dto.wallet.req.VisitAppReqDTO;
import org.wallet.common.entity.wallet.WalletTransferLogEntity;
import org.wallet.common.enums.Device;
import org.wallet.common.enums.ResultCode;
import org.wallet.common.enums.application.AppLinkTypeEnum;
import org.wallet.dap.common.bind.Results;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.web.common.crypto.DecryptRequest;
import org.wallet.web.common.crypto.EncryptResponse;
import org.wallet.web.common.mvc.controller.BaseController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author zengfucheng
 **/
@DecryptRequest
@EncryptResponse
@RestController
@RequestMapping("wallet")
public class WalletController extends BaseController {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Reference(group = DubboServiceGroup.SERVICE_APP_LINK)
    private IService appLinkService;

    @Reference(group = DubboServiceGroup.SERVICE_APP_VERSION)
    private IService appVersionService;

    @Reference(group = DubboServiceGroup.SERVICE_APP_VISIT_LOG)
    private IService appVisitLogService;

    @Reference(group = DubboServiceGroup.SERVICE_WALLET_COIN, timeout = 30000)
    private IService walletCoinService;

    @Reference(group = DubboServiceGroup.SERVICE_WALLET_COIN_ORDER)
    private IService walletCoinOrderService;

    @Reference(group = DubboServiceGroup.SERVICE_WALLET_PAY_CONFIG)
    private IService walletPayConfigService;

    @Reference(group = DubboServiceGroup.SERVICE_WALLET_TRANSFER_LOG)
    private IService walletTransferLogService;

    @GetMapping("app/upgrade")
    public SimpleResult upgrade(Device device){
        if(null == device){
            return Results.paramInvalid("[device]不能为空");
        }
        ServiceResponse response = appVersionService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_VERSION, "getCurrentAppVersion", device));

        return Results.by(response);
    }

    /**
     * 创建钱包-隐私政策
     */
    @GetMapping("privacy_policy")
    public SimpleResult privacyPolicy(){
        ServiceResponse response = appLinkService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_LINK,
                "getAppLinkByType", AppLinkTypeEnum.PROTOCOL));

        return Results.by(response);
    }

    /**
     * 查询币种信息
     */
    @GetMapping("coin/info")
    public SimpleResult findCoinInfo(FindCoinInfoReqDTO dto){
        ServiceResponse response = walletCoinService.invoke(createRequest(DubboServiceGroup.SERVICE_WALLET_COIN,
                "findCoinInfo", dto));

        return Results.by(response);
    }

    /**
     * 查询币种基本信息
     */
    @GetMapping("coin/base-info")
    public SimpleResult findCoinInfos(FindCoinInfoReqDTO dto){
        if(!StringUtils.isEmpty(dto.getCoinNameString())){
            List<String> names = dto.getCoinNames();

            if(CollectionUtils.isEmpty(names)){ names = new ArrayList<>(); }

            names.addAll(Arrays.asList(dto.getCoinNameString().split(",")));
        }

        ServiceResponse response = walletCoinService.invoke(createRequest(DubboServiceGroup.SERVICE_WALLET_COIN,
                "findCoinBaseInfo", dto));

        return Results.by(response);
    }

    /**
     * EOS账号激活码-获取支付配置
     */
    @GetMapping("coin/account/pay-config")
    public SimpleResult findPayConfig(){
        Search search = Searchs.of(SortDTO.asc(WalletPayConfigField.SORT), SearchFilters.eq(WalletPayConfigField.ENABLE, Boolean.TRUE));

        ServiceResponse response = walletPayConfigService.invoke(createRequest(DubboServiceGroup.SERVICE_WALLET_PAY_CONFIG,
                "findWalletPayConfig", search));

        return Results.by(response);
    }

    /**
     * EOS账号激活码-EOS资源属性
     */
    @GetMapping("coin/account/eos-resource")
    private SimpleResult getResource(){
        List<WalletCoinResourceDTO> list = cache.get(WalletCoinResourceCache.CACHE_PREFIX, WalletCoinResourceCache.ALL, List.class);

        if(CollectionUtils.isEmpty(list)){ list = WalletCoinResourceDTO.getDefault(); }

        list.sort(Comparator.comparing(WalletCoinResourceDTO::getSort));

        return Results.success(list);
    }

    /**
     * EOS账号激活码-查询订单
     */
    @GetMapping("coin/account/order")
    public SimpleResult findCoinAccountOrder(String no){
        ServiceResponse response = walletCoinOrderService.invoke(createRequest(DubboServiceGroup.SERVICE_WALLET_COIN_ORDER,
                "findOrderByNo", no));

        return Results.by(response);
    }

    /**
     * EOS账号激活码-创建订单
     */
    @PostMapping("coin/account/order")
    public SimpleResult createCoinAccountOrder(@RequestBody WalletCoinOrderDTO dto){
        ServiceResponse response = walletCoinOrderService.invoke(createRequest(DubboServiceGroup.SERVICE_WALLET_COIN_ORDER,
                "createOrder", dto));

        return Results.by(response);
    }

    /**
     * EOS账号激活码-使用激活码创建EOS账号
     */
    @PostMapping("coin/account")
    public SimpleResult createCoinAccount(@RequestBody @Valid CreateCoinAccountReqDTO dto){
        ServiceResponse response = walletCoinService.invoke(createRequest(DubboServiceGroup.SERVICE_WALLET_COIN,
                "createCoinAccount", dto));

        if(response.success()){
            BlockResult result = response.getResult();
            if(!result.success()){
                return Results.of(ResultCode.BusinessFail.getCode(), result.getMsg());
            }else{
                return Results.success(result.getResult());
            }
        }

        return Results.by(response);
    }

    /**
     * 转账日志
     */
    @PostMapping("log/transfer")
    public SimpleResult addWalletTransferLog(@RequestBody @Valid WalletTransferLogDTO dto){
        WalletTransferLogEntity entity = new WalletTransferLogEntity();

        BeanUtils.copyProperties(dto, entity);

        ServiceResponse response = walletTransferLogService.invoke(createRequest(DubboServiceGroup.SERVICE_WALLET_TRANSFER_LOG,
                "addWalletTransferLog", entity));

        return Results.by(response);
    }

    /**
     * 导入钱包
     */
    @GetMapping("log/import-wallet")
    public SimpleResult importWallet(@RequestParam Long chainId){
        RedisAtomicLong counter = new RedisAtomicLong(WalletCache.IMPORT + WalletCache.SEP + chainId, redisTemplate.getConnectionFactory());
        Long num = counter.incrementAndGet();
        return Results.success(num);
    }

    /**
     * 创建钱包
     */
    @GetMapping("log/create-wallet")
    public SimpleResult createWallet(@RequestParam Long chainId){
        RedisAtomicLong counter = new RedisAtomicLong(WalletCache.CREATE + WalletCache.SEP + chainId, redisTemplate.getConnectionFactory());
        Long num = counter.incrementAndGet();
        return Results.success(num);
    }

    /**
     * 访问App日志
     */
    @GetMapping("log/visit-dapp")
    public SimpleResult visitDapp(@Valid VisitAppReqDTO reqDTO){
        ServiceResponse response = appVisitLogService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_VISIT_LOG,
                "addVisitAppLog", reqDTO));

        return Results.success();
    }
}
