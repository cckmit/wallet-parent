package org.wallet.dap.common.crypto.symmetric;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import org.wallet.dap.common.crypto.Crypto;
import org.wallet.dap.common.crypto.CryptoException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *  Base64加解密
 *  @author 熊诗言
 */
public class Base64Crypto implements Crypto {
    @Override
    public byte[] encrypt(byte[] src) {
        try {
            return Base64.encode(src, true);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] src) {
        try {
            return Base64.decode(src);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    @Override
    public void encrypt(InputStream in, OutputStream out) {
        try {
            IoUtil.write(out, true, Base64.encode(in).getBytes());
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    @Override
    public void decrypt(InputStream in, OutputStream out) {
        try {
            Base64.decodeToStream(IoUtil.read(in, CharsetUtil.UTF_8), out, true);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    @Override
    public String encrypt(String src, String charset) {
        return Base64.encode(src, charset);
    }

    @Override
    public String encrypt(String src){
        return encrypt(src, CharsetUtil.UTF_8);
    }

    @Override
    public String decrypt(String src, String charset) {
        try {
            return Base64.decodeStr(src, charset);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    @Override
    public String decrypt(String src){
        return decrypt(src, CharsetUtil.UTF_8);
    }
}
