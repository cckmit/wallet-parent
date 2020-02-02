package org.wallet.web.application.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wallet.common.constants.WebConstants;
import org.wallet.common.dto.ClientData;
import org.wallet.common.dto.SimpleToken;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.utils.StringGenerator;
import org.wallet.web.common.utils.TokenUtil;

import java.time.Duration;

/**
 * @author zengfucheng
 **/
@Slf4j
@Component
public class TokenService {

    /** Token过期时间 */
    @Value("${api.token.timeout:2H}")
    Duration tokenExpire;

    @Autowired
    Cache cache;

    /**
     * 根据客户端数据创建Token
     * @param data 客户端数据
     * @return Token
     */
    public SimpleToken createToken(ClientData data){
        SimpleToken simpleToken = new SimpleToken();

        String key = StringGenerator.newInstance().generate(16);
        String aesIv = StringGenerator.newInstance().generate(16);

        simpleToken.setClientId(data.getClientId());
        simpleToken.setCryptoKey(key);
        simpleToken.setAesIv(aesIv);

        Long expire = (System.currentTimeMillis() + tokenExpire.toMillis()) / 1000;

        simpleToken.setExpire(expire.toString());

        String token = TokenUtil.createToken(simpleToken, tokenExpire);

        simpleToken.setToken(token);

        log.info("客户端{}获取Token:{}", JSON.toJSONString(data), token);

        cache.put(WebConstants.CACHE_APP_TOKEN, data.getClientId(), simpleToken, tokenExpire.getSeconds());

        return simpleToken;
    }
}
