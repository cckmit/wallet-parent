package org.wallet.web.application.controller;

import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.dto.SimpleResult;
import org.wallet.dap.common.bind.Results;
import org.wallet.dap.common.crypto.Crypto;
import org.wallet.dap.common.crypto.KeyGenerator;
import org.wallet.dap.common.crypto.asymmetric.AsymmetricAlgorithm;
import org.wallet.dap.common.crypto.symmetric.AES;
import org.wallet.dap.common.crypto.symmetric.Base64;
import org.wallet.dap.common.crypto.symmetric.Mode;
import org.wallet.dap.common.crypto.symmetric.Padding;
import org.wallet.web.common.mvc.controller.BaseController;
import org.wallet.web.common.mvc.token.PassToken;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zengfucheng
 **/
@Slf4j
@PassToken
@RestController
@RequestMapping("common")
public class CommonController extends BaseController {

    /**
     * 加密
     */
    @PostMapping("crypto")
    public SimpleResult crypto(@RequestHeader("Crypto-Key") String key,
                               @RequestHeader("Crypto-Iv") String iv,
                               @RequestBody Object content) {
        Crypto aes = new AES(Mode.CBC, Padding.PKCS5Padding, iv.getBytes(), key.getBytes());

        String data;
        if (content instanceof String) {
            data = (String) content;
        } else {
            data = JSON.toJSONString(content);
        }
        return Results.success(Base64.encode(aes.encrypt(data.getBytes())));
    }

    /**
     * 解密
     */
    @PostMapping("decrypt")
    public SimpleResult decryptPost(@RequestHeader("Crypto-Key") String key,
                                    @RequestHeader("Crypto-Iv") String iv,
                                    @RequestBody String content) {
        Crypto aes = new AES(Mode.CBC, Padding.PKCS5Padding, iv.getBytes(), key.getBytes());
        byte[] decryptBytes = aes.decrypt(Base64.decode(content));
        String decryptBody = new String(decryptBytes, CharsetUtil.CHARSET_UTF_8);
        return Results.success(JSON.parse(decryptBody));
    }

    /**
     * 生成公私钥对
     */
    @GetMapping("rsa")
    public SimpleResult rsa() {
        KeyPair keyPair = KeyGenerator.generateKeyPair(AsymmetricAlgorithm.RSA.getValue());

        Map<String, String> result = new HashMap<>(2);

        String publicKey = Base64.encode(keyPair.getPublic().getEncoded());
        String privateKey = Base64.encode(keyPair.getPrivate().getEncoded());

        result.put("publicKey", publicKey);
        result.put("privateKey", privateKey);

        return Results.success(result);
    }
}
