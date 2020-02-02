package org.wallet.gateway.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.wallet.common.enums.wallet.QuotesSourceEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zengfucheng
 **/
@Data
@ConfigurationProperties(prefix = "quotes")
public class QuotesProperties {
    private Map<QuotesSourceEnum, String> hosts = new HashMap<QuotesSourceEnum, String>(){{
        put(QuotesSourceEnum.Newdex, QuotesSourceEnum.Newdex.getHost());
        put(QuotesSourceEnum.FINDEX, QuotesSourceEnum.FINDEX.getHost());
        put(QuotesSourceEnum.WhaleEx, QuotesSourceEnum.WhaleEx.getHost());
    }};

}
