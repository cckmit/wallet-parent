package org.wallet.gateway.client.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.cache.DFuseCache;
import org.wallet.common.dto.dfuse.*;
import org.wallet.common.dto.wallet.WalletBlockTransDTO;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.dubbo.*;
import org.wallet.gateway.client.config.DFuseProperties;
import org.wallet.gateway.client.utils.httpclient.HttpConnectionPool;
import org.wallet.gateway.client.utils.httpclient.HttpPoint;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zengfucheng
 **/
@Service(group = DubboServiceGroup.CLIENT_DFUSE, timeout = 30000)
@org.springframework.stereotype.Service
public class DFuseService extends BaseDubboService implements IService {
    private final static String TIME_FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss";
    private final static String TIME_FORMAT_2 = "yyyy-MM-dd'T'HH:mm:ss.FFF";

    @Autowired
    private Cache cache;

    @Autowired
    private DFuseProperties dFuseProperties;

    @PostConstruct
    public void clearCursor(){
        if(Boolean.TRUE.equals(dFuseProperties.getClearCursor())){
            cache.evict(DFuseCache.CURSOR);
        }
    }

    public SearchTransResult queryTransResult(String account) {
        String searchUrl = dFuseProperties.getHost() + "v0/search/transactions";
        searchUrl += "?";
        searchUrl += "sort=ASC";
        searchUrl += "&limit=10";

        String dFuseCursor = cache.get(DFuseCache.CURSOR, account, String.class);
        if(!StringUtils.isEmpty(dFuseCursor)){
            searchUrl += "&cursor=" + dFuseCursor;
        }

        if(null != dFuseProperties.getStartBlock()){
            searchUrl += "&start_block=" + dFuseProperties.getStartBlock();
        }

        String qValue = "action:transfer account:" + dFuseProperties.getContract() + " ";

        qValue += "receiver:" + account;
        qValue += " data.to:" + account;

        try {
            qValue = URLEncoder.encode(qValue, "UTF-8");
        } catch (UnsupportedEncodingException ignored) { }

        searchUrl += "&q=" + qValue;

        try {
            SearchTransResult result = HttpConnectionPool.get(searchUrl, SearchTransResult.class, new HttpPoint() {
                @Override
                public Object beforeExecute(HttpClient client, HttpUriRequest httpUriRequest) {
                    TokenDTO tokenDTO = getToken();
                    if(null != tokenDTO){
                        httpUriRequest.setHeader("Authorization", "Bearer " + tokenDTO.getToken());
                    }
                    return null;
                }

                @Override
                public void afterExecute(Object beforeReturnValue, HttpResponse response, String responseString) {

                }

                @Override
                public void onException(Throwable throwable) {

                }
            });
            if(null != result){
                if(!StringUtils.isEmpty(result.getCursor())){
                    cache.put(DFuseCache.CURSOR, account, result.getCursor());
                }
                return result;
            }
        } catch (IOException e) {
            log.error("查询DFuse 交易失败：" + e.getMessage(), e);
        }
        return null;
    }


    public ServiceResponse queryTrans(ServiceRequest request, ServiceResponse response) {
        String account = request.getParam();
        if(StringUtils.isEmpty(account)){ Responses.missingParam("account"); }
        SearchTransResult result = queryTransResult(account);

        List<WalletBlockTransDTO> dtoList = new ArrayList<>();

        if(null != result && !CollectionUtils.isEmpty(result.getTransactions())){
            result.getTransactions().forEach(row -> {
                TransactionLifecycle lifecycle = row.getLifecycle();
                String status = lifecycle.getTransactionStatus();
                TransactionTrace transactionTrace = lifecycle.getExecutionTrace();
                String trxId = transactionTrace.getId();

                TransferData data = transactionTrace.getTransferData(dFuseProperties.getContract(), account);

                if(null == data || org.apache.commons.lang3.StringUtils.isAnyEmpty(data.getFrom(),
                        data.getTo(), data.getQuantity())){
                    log.warn("EOS[{}][{}]交易数据不完整[{}]", account, trxId, JSON.toJSONString(data));
                    return;
                }

                String quantityString = data.getQuantity();

                String quantity = quantityString.split(" ")[0];
                String symbol = quantityString.split(" ")[1];

                WalletBlockTransDTO dto = new WalletBlockTransDTO();

                dto.setTrxId(trxId);
                dto.setBlockNum(transactionTrace.getBlockNum());
                dto.setContract(dFuseProperties.getContract());
                dto.setMemo(data.getMemo());
                dto.setQuantity(new BigDecimal(quantity));
                dto.setReceiver(data.getTo());
                dto.setSender(data.getFrom());
                dto.setStatus(status);
                dto.setSymbol(symbol);

                try {
                    dto.setTimestamp(DateUtils.parseDate(transactionTrace.getBlockTime(), TIME_FORMAT_1, TIME_FORMAT_2));
                } catch (ParseException e) {
                    log.warn("格式化交易时间[{}]失败：{}", transactionTrace.getBlockTime(), e.getMessage(), e);
                }

                dtoList.add(dto);
            });
        }else{
            log.info("当前无结果");
        }

        return Responses.success(dtoList);
    }

    public TokenDTO getToken() {
        TokenDTO tokenDTO = cache.get(DFuseCache.CACHE_PREFIX, DFuseCache.TOKEN, TokenDTO.class);
        if(null != tokenDTO){
            return tokenDTO;
        }
        Map<String, String> param = new HashMap<>(1);
        param.put("api_key", dFuseProperties.getApiKey());
        try {
            tokenDTO = HttpConnectionPool.postJSON("https://auth.dfuse.io/v1/auth/issue", JSON.toJSONString(param), TokenDTO.class);
            if(null != tokenDTO && tokenDTO.success()){
                // 提前5分钟过期，及时刷新Token
                Long expire = tokenDTO.getExpire() - System.currentTimeMillis() / 1000 - (60 * 5);
                cache.put(DFuseCache.CACHE_PREFIX, DFuseCache.TOKEN, tokenDTO, expire);
                return tokenDTO;
            }else if(null != tokenDTO && tokenDTO.error()){
                log.warn("获取DFuse Token失败[{}][{}]", tokenDTO.getCode(), tokenDTO.getMessage());
            }
        } catch (IOException e) {
            log.error("获取DFuse Token失败：" + e.getMessage(), e);
        }
        return null;
    }
}
