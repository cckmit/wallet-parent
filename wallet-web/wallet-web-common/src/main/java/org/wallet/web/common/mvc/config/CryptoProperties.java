package org.wallet.web.common.mvc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zengfucheng
 **/
@Data
@ConfigurationProperties(prefix = "spring.crypto")
public class CryptoProperties {

    /**
     * Crypto Charset
     */
    private String charset = "UTF-8";

    /**
     * Decrypt Request
     */
    private Boolean decryptRequest = true;

    /**
     * Encrypt Response
     */
    private Boolean encryptResponse = true;
}
