package org.wallet.web.common.crypto;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.wallet.dap.common.crypto.Crypto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 解密Http请求体
 *
 * @author zengfucheng
 */
public class DecryptHttpInputMessage implements HttpInputMessage {
    private HttpInputMessage inputMessage;
    private String charset;
    private Crypto crypto;

    public DecryptHttpInputMessage(HttpInputMessage inputMessage, String charset, Crypto crypto) {
        this.inputMessage = inputMessage;
        this.charset = charset;
        this.crypto = crypto;
    }

    @Override
    public InputStream getBody() throws IOException {
        String content = IoUtil.read(inputMessage.getBody(), charset);
        byte[] decryptBytes = crypto.decrypt(Base64.decode(content));
        String decryptBody = new String(decryptBytes, charset);
        return new ByteArrayInputStream(decryptBody.getBytes(charset));
    }

    @Override
    public HttpHeaders getHeaders() {
        return inputMessage.getHeaders();
    }
}
