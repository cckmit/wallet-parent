package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.CoinConstants;
import org.wallet.common.constants.cache.QuotesCache;
import org.wallet.common.constants.cache.WalletTransferLogCache;
import org.wallet.common.constants.field.WalletTransferLogField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.wallet.CoinAvgPriceDTO;
import org.wallet.common.dto.wallet.WalletTransferLogDTO;
import org.wallet.common.dto.wallet.req.TransferStatisticsReqDTO;
import org.wallet.common.entity.wallet.WalletTransferLogEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.WalletTransferLogService;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_WALLET_TRANSFER_LOG)
public class WalletTransferLogDubboService extends BaseDubboService implements IService{

    @Autowired
    Cache cache;

    @Autowired
    WalletTransferLogService walletTransferLogService;

    public ServiceResponse transferStatistics(ServiceRequest request, ServiceResponse response) {
        TransferStatisticsReqDTO reqDTO = request.getParam();
        if(null == reqDTO){
            return Responses.missingParam("req");
        }

        return response.setResult(walletTransferLogService.transferStatistics(reqDTO));
    }

    public ServiceResponse getWalletTransferLogById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(WalletTransferLogField.WALLET_TRANSFER_LOG_ID);
        }

        return response.setResult(walletTransferLogService.findOne(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllWalletTransferLog(ServiceRequest request, ServiceResponse response) {
        List<WalletTransferLogDTO> dtoList = cache.get(WalletTransferLogCache.CACHE_PREFIX, WalletTransferLogCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<WalletTransferLogEntity> appChainList = walletTransferLogService.findAll(new Sort(Sort.Direction.ASC, WalletTransferLogField.ID));

        dtoList = appChainList.stream().map(entity -> {
            WalletTransferLogDTO dto = new WalletTransferLogDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(WalletTransferLogCache.CACHE_PREFIX, WalletTransferLogCache.ALL, dtoList, WalletTransferLogCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findWalletTransferLog(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findWalletTransferLogPage(request, response); }

        search = null == search ? new Search() : search;

        List<WalletTransferLogEntity> appChainList = walletTransferLogService.findAll(search);

        List<WalletTransferLogDTO> dtoList = appChainList.stream().map(entity -> {
            WalletTransferLogDTO dto = new WalletTransferLogDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findWalletTransferLogPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(WalletTransferLogField.PAGE); }

        PageDTO<WalletTransferLogEntity> entityPage = walletTransferLogService.findPage(search);

        List<WalletTransferLogEntity> appVersionList = entityPage.getRecords();

        List<WalletTransferLogDTO> dtoList = appVersionList.stream().map(entity -> {
            WalletTransferLogDTO dto = new WalletTransferLogDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addWalletTransferLog(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        WalletTransferLogEntity entity = request.getParam();

        if(null == userId){ userId = WalletTransferLogField.DEFAULT_ADMIN_USER_ID; }
        if(null == entity){ return Responses.missingParam(WalletTransferLogField.WALLET_TRANSFER_LOG); }

        entity.setId(null);
        entity.setCreator(userId);

        String token = entity.getToken();
        String coinName = entity.getCoinName();

        String avgKey = QuotesCache.AVG + QuotesCache.SEP + token + QuotesCache.SEP + entity.getContract();

        CoinAvgPriceDTO coinAvgPriceDTO = cache.get(avgKey, coinName, CoinAvgPriceDTO.class);

        log.info("均价[{}:{}]缓存：{}", avgKey, coinName, JSON.toJSONString(coinAvgPriceDTO));

        BigDecimal usdRate = BigDecimal.ZERO;
        BigDecimal usdAmount = BigDecimal.ZERO;

        if(null != coinAvgPriceDTO){
            // 如果是子链币种转账，则获取该子链币种美元汇率
            usdRate = coinAvgPriceDTO.getAvgUSDRate();
            log.info("获取子链币种[{}]美元汇率[{}]", coinName, usdRate);
        }else if(coinName.equals(token)){
            // 如果是主链币种转账，直接获取主链美元汇率
            usdRate = cache.get(QuotesCache.PRICE_USD, coinName, BigDecimal.class);
            log.info("获取主链币种[{}]美元汇率[{}]", coinName, usdRate);
        }

        if(null != usdRate){
            usdAmount = usdRate.multiply(entity.getAmount()).setScale(CoinConstants.SCALE_PRICE, BigDecimal.ROUND_DOWN);
        }

        usdRate = null == usdRate ? BigDecimal.ZERO : usdRate;
        usdAmount = null == usdAmount ? BigDecimal.ZERO : usdAmount;

        entity.setUsdRate(usdRate);
        entity.setUsdAmount(usdAmount);

        WalletTransferLogEntity result = walletTransferLogService.save(entity);

        return response.setResult(result);
    }

    public ServiceResponse updateWalletTransferLog(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        WalletTransferLogEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(WalletTransferLogField.USER_ID); }
        if(null == entity){ return Responses.missingParam(WalletTransferLogField.WALLET_TRANSFER_LOG); }
        if(null == entity.getId()){ return Responses.missingParam(WalletTransferLogField.WALLET_TRANSFER_LOG_ID); }

        entity.setCoverageUpdate(true);

        WalletTransferLogEntity result = walletTransferLogService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteWalletTransferLog(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(WalletTransferLogField.WALLET_TRANSFER_LOG_ID); }

        walletTransferLogService.delete(id);

        return response;
    }
}
