package org.wallet.dap.common.crypto.symmetric;

import cn.hutool.core.util.CharsetUtil;
import org.wallet.dap.common.crypto.CryptoException;
import org.wallet.dap.common.crypto.KeyCrypto;
import org.wallet.dap.common.utils.RadixUtil;

import javax.crypto.Cipher;
import java.security.Key;

/**
 * DESAS
 * @author xiongshiyan at 2018/7/10
 */
public class DESAS implements KeyCrypto {
    private String key;
    public DESAS(String key){
        this.key = key;
    }
    public DESAS(){ }

    @Override
    public KeyCrypto setKey(String key) {
        this.key = key;
        return this;
    }

    @Override
    public byte[] encrypt(byte[] src) {
        try {
            Key key = getKey(this.key.getBytes(CharsetUtil.UTF_8));
            Cipher encryptCipher = Cipher.getInstance("DES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            return encryptCipher.doFinal(src);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] src) {
        try {
            Key key = getKey(this.key.getBytes(CharsetUtil.UTF_8));
            Cipher decryptCipher = Cipher.getInstance("DES");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
            return decryptCipher.doFinal(src);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    @Override
    public String encrypt(String src, String charset) {
        byte[] bytes = src.getBytes(CharsetUtil.charset(charset));
        byte[] encrypted = encrypt(bytes);
        return RadixUtil.toHexLower(encrypted);
    }

    private Key getKey(byte[] arrBTmp) throws Exception{
        byte[] arrB = new byte[8];
        for(int i = 0; i < arrBTmp.length && i < arrB.length; i++){
            arrB[i] = arrBTmp[i];
        }
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;
    }
}
