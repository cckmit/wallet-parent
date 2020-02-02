package org.wallet.web.common.crypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.wallet.common.constants.WebConstants;
import org.wallet.dap.common.utils.ThreadLocalUtil;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 请求数据接收处理类<br>
 * 对加了{@code @DecryptRequest}的方法的数据进行解密操作<br>
 * 只对 {@code @RequestBody} 参数有效
 *
 * @author zengfucheng
 */
@ControllerAdvice
@ConditionalOnProperty(value = "spring.crypto.decrypt-request", havingValue = "true", matchIfMissing = true)
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    @Value("${spring.crypto.charset:UTF-8}")
    private String charset = "UTF-8";

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if (CryptoChecker.needDecrypt(parameter)) {
            return new DecryptHttpInputMessage(inputMessage, charset, ThreadLocalUtil.get(WebConstants.PARAM_CRYPTO));
        }
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
