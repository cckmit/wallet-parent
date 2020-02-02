package org.wallet.web.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wallet.common.constants.CoinConstants;
import org.wallet.common.constants.cache.WalletCache;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.application.req.FindAppVisitLogReqDTO;
import org.wallet.common.dto.wallet.req.TransferStatisticsReqDTO;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.Results;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.web.admin.utils.UserUtil;
import org.wallet.web.common.mvc.controller.BaseController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 统计
 * 
 * @author zengfucheng
 */
@RestController
@RequestMapping("statistics")
public class StatisticsController extends BaseController {

    @Autowired
    private Cache cache;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Reference(group = DubboServiceGroup.SERVICE_APP_VISIT_LOG, timeout = 10000)
    private IService appVisitLogService;

    @Reference(group = DubboServiceGroup.SERVICE_STATISTICS, timeout = 10000)
    private IService statisticsService;

    @Reference(group = DubboServiceGroup.SERVICE_WALLET_TRANSFER_LOG, timeout = 10000)
    private IService walletTransferLogService;

    @Reference(group = DubboServiceGroup.SERVICE_WALLET_COIN_ORDER, timeout = 10000)
    private IService walletCoinOrderService;

    public StatisticsController() {
        setServiceGroup(DubboServiceGroup.SERVICE_STATISTICS);
    }

	@GetMapping("assets")
	public SimpleResult assets(@RequestParam("chainId") Long chainId) {
        ServiceResponse response = statisticsService.invoke(createRequest("getAssetsStatisticsByChainId", UserUtil.getUserId(), chainId));

	    return Results.by(response);
	}

	@GetMapping("transfer")
	public SimpleResult transfer(TransferStatisticsReqDTO reqDTO) {
        ServiceResponse response = walletTransferLogService.invoke(createRequest("transferStatistics", UserUtil.getUserId(), reqDTO));

	    return Results.by(response);
	}

	@GetMapping("account-buy-frequency")
	public SimpleResult accountBuyFrequency(TransferStatisticsReqDTO reqDTO) {
        ServiceResponse response = walletCoinOrderService.invoke(createRequest("accountBuyFrequency", UserUtil.getUserId(), reqDTO));

	    return Results.by(response);
	}

	@GetMapping("account-buy-percent")
	public SimpleResult accountBuyPercent(TransferStatisticsReqDTO reqDTO) {
        ServiceResponse response = walletCoinOrderService.invoke(createRequest("accountBuyPercent", UserUtil.getUserId(), reqDTO));

	    return Results.by(response);
	}

	@GetMapping("wallet-percent")
	public SimpleResult walletPercent(@RequestParam("chainId") Long chainId) {
        Integer importCount = cache.get(WalletCache.IMPORT, chainId.toString(), Integer.class);
        Integer createCount = cache.get(WalletCache.CREATE, chainId.toString(), Integer.class);
        Integer total = importCount + createCount;
        BigDecimal importPercent = new BigDecimal(importCount).divide(new BigDecimal(total), CoinConstants.SCALE_PERCENT, BigDecimal.ROUND_DOWN);
        BigDecimal createPercent = new BigDecimal(createCount).divide(new BigDecimal(total), CoinConstants.SCALE_PERCENT, BigDecimal.ROUND_DOWN);
        Map<String, BigDecimal> result = new HashMap<>(2);
        result.put("importPercent", importPercent);
        result.put("createPercent", createPercent);
	    return Results.success(result);
	}

	@GetMapping("app-visit-count")
	public SimpleResult appVisitCount(FindAppVisitLogReqDTO reqDTO) {
        ServiceResponse response = appVisitLogService.invoke(createRequest("appVisitCount", UserUtil.getUserId(), reqDTO));

        return Results.by(response);
	}

	@GetMapping("app-visit-rank")
	public SimpleResult appVisitRank(FindAppVisitLogReqDTO reqDTO) {
        ServiceResponse response = appVisitLogService.invoke(createRequest("appVisitRank", UserUtil.getUserId(), reqDTO));

        return Results.by(response);
	}
	
}