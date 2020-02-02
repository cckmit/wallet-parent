package org.wallet.gateway.client.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.CoinConstants;
import org.wallet.common.dto.block.BlockResult;
import org.wallet.common.dto.block.BlockResultCode;
import org.wallet.common.dto.block.req.CreateAccountReqDTO;
import org.wallet.gateway.client.config.BlockProperties;
import org.wallet.gateway.client.service.BlockService;
import org.wallet.gateway.client.utils.httpclient.HttpConnectionPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
public class EOSBlockService implements BlockService {

    @Autowired
    private BlockProperties blockProperties;

    @Override
    public String getCoinName() {
        return CoinConstants.EOS;
    }

    @Override
    public BlockResult createAccount(CreateAccountReqDTO dto) {
        if(StringUtils.isEmpty(dto.getName())){
            return BlockResult.paramInvalid("[name]不能为空");
        }else if(StringUtils.isEmpty(dto.getPublicKey())){
            return BlockResult.paramInvalid("[publicKey]不能为空");
        }

        Map<String, String> param = new HashMap<>(5);

        param.put("name", dto.getName());
        param.put("public_key", dto.getPublicKey());

        String host = blockProperties.getCoinHost().get(getCoinName());

        if(StringUtils.isEmpty(host)){
            return BlockResult.paramInvalid("未配置[" + getCoinName() + "->host]属性");
        }

        try {
            return HttpConnectionPool.postJSON(host + "/api/v1/account", JSON.toJSONString(param), BlockResult.class);
        } catch (IOException e) {
            String msg = String.format("创建[%s:%s]账号失败：%s", getCoinName(), dto.getName(), e.getMessage());
            log.error(msg);
            return BlockResult.of(BlockResultCode.ERROR, msg);
        }

    }
}
