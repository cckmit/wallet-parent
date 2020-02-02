package org.wallet.web.common.crypto;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.wallet.common.constants.WebConstants;
import org.wallet.common.dto.SimpleResult;
import org.wallet.dap.common.bind.Results;
import org.wallet.dap.common.crypto.Crypto;
import org.wallet.dap.common.utils.ThreadLocalUtil;

/**
 * 请求响应处理类<br>
 * 对加了{@code @EncryptResponse}的方法的数据进行加密操作
 *
 * @author zengfucheng
 */
@ControllerAdvice
@ConditionalOnProperty(value = "spring.crypto.encrypt-response", havingValue = "true", matchIfMissing = true)
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Value("${spring.crypto.charset:UTF-8}")
    private String charset = "UTF-8";

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                        Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        boolean encrypt = CryptoChecker.needEncrypt(returnType);

        if (!encrypt) {
            return body;
        }

        SimpleResult<String> result = Results.success();
        Object data = body;
        if(body instanceof SimpleResult){
            result = (SimpleResult) body;
            data = result.getData();
        }

        if (null == data) {
            return result;
        }

        String content;
        Class<?> dataClass = data.getClass();
        if (dataClass.isPrimitive() || (data instanceof String)) {
            content = String.valueOf(data);
        } else {
            content = JSON.toJSONString(data);
        }

        Crypto aes = ThreadLocalUtil.get(WebConstants.PARAM_CRYPTO);

        result.setData(Base64.encode(aes.encrypt(content.getBytes())));

        return result;
    }

}
