package org.wallet.service.application;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.Base64Utils;
import org.wallet.dap.common.crypto.CompositeCrypto;
import org.wallet.dap.common.crypto.Crypto;
import org.wallet.dap.common.crypto.CryptoException;
import org.wallet.dap.common.crypto.KeyGenerator;
import org.wallet.dap.common.crypto.asymmetric.AsymmetricAlgorithm;
import org.wallet.dap.common.crypto.asymmetric.RSA;
import org.wallet.dap.common.crypto.digest.*;
import org.wallet.dap.common.crypto.symmetric.*;
import org.wallet.dap.common.utils.RadixUtil;

import javax.crypto.spec.IvParameterSpec;
import java.security.KeyPair;

public class CryptoTest {
    @Test
    public void testMd5() throws Exception {
        String src = "熊诗言";
        Assert.assertEquals("F0C1CAE3AEF8B4704FE02924D6BDDF36", new Md5().encrypt(src));
    }

    @Test
    public void testDes() {
        String src = "熊诗言";
        Crypto crypto = new DesCrypto("xxssyyyy");
        final String xx = crypto.encrypt(src);
        Assert.assertEquals("384C98CD97F8EA012F02ACD745E90277", xx);
        Assert.assertEquals(src, crypto.decrypt(xx));

        final String yy = crypto.encrypt(src, "UTF-8");
        Assert.assertEquals("384C98CD97F8EA012F02ACD745E90277", yy);
        Assert.assertEquals(src, crypto.decrypt(yy));

    }

    @Test
    public void testBase64() throws Exception {
        String src = "熊诗言";
        Crypto crypto = new Base64Crypto();
        final String xx = crypto.encrypt(src).trim();
        Assert.assertEquals("54aK6K+X6KiA", xx);
        Assert.assertEquals(src, crypto.decrypt(xx));

        String yy = Base64.encode(src);

        Assert.assertEquals("54aK6K+X6KiA", yy);
        Assert.assertEquals(src, crypto.decrypt(yy));
        Assert.assertEquals(src, Base64.decodeStr(yy));
    }

    @Test
    public void testUrl() {
        String src = "熊诗言";
        Crypto crypto = new UrlCrypto();
        final String xx = crypto.encrypt(src);
        Assert.assertEquals("%E7%86%8A%E8%AF%97%E8%A8%80", xx);
        Assert.assertEquals(src, crypto.decrypt(xx));

    }

    @Test
    public void testAes() {
        String src = "熊诗言";
        Crypto crypto = new AesCrypto("xxssyyyyssxx");
        final String xx = crypto.encrypt(src);
        Assert.assertEquals("bdZH+ALmgV+SMl8WDWb5Aw==", xx);
        Assert.assertEquals(src, crypto.decrypt(xx));

    }

    @Test
    public void testDESAS() {
        String src = "熊诗言";
        Crypto crypto = new DESAS("xxssyyyy");
        final String xx = crypto.encrypt(src);
        Assert.assertEquals("ef9133ec4716cc32450946c94e14f74a", xx);
        Assert.assertEquals(src, crypto.decrypt(xx));

    }

    @Test
    public void testComposite() throws Exception {
        String src = "熊诗言";
        Crypto base64Crypto = new Base64Crypto();
        Crypto desCrypto = new DesCrypto("xxssyyyy");

        CompositeCrypto compositeCrypto = new CompositeCrypto();
        //这个就能很好地表达先进行什么加密再进行什么加密，自动逆序解密
        compositeCrypto.add(base64Crypto).add(desCrypto).add(base64Crypto);

        final String xx = compositeCrypto.encrypt(src);
        Assert.assertEquals("ODM3OTgwMjhDMTRDNjdEMzM3QkIzQTI0NjZEMEU0Q0U=", xx);
        Assert.assertEquals(src, compositeCrypto.decrypt(xx));

    }

    /**
     * 测试整数的二进制表示
     */
    @Test
    public void testBinary() {
        int a = -6;
        for (int i = 0; i < 32; i++) {
            int t = (a & 0x80000000 >>> i) >>> (31 - i);
            System.out.print(t);
        }
    }

    @Test
    public void testAesHutool() throws Exception {
        String src = "熊诗言";
        Crypto crypto = new AES();
        String xx = crypto.encrypt(src);
        System.out.println(xx);//因为key是随机的，所以每次不一样
        Assert.assertEquals(src, crypto.decrypt(xx));

        crypto = new AES(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 9, 9, 9, 9, 9, 9});
        String yy = crypto.encrypt(src);
        Assert.assertEquals("B1D1AE9EA29E065121E32A568A9B4465", yy);
        Assert.assertEquals(src, crypto.decrypt(yy));
    }

    @Test
    public void testAesCbc() throws Exception {
        AES crypto = new AES(Mode.CBC, Padding.PKCS5Padding, "dMitHORyqbeYVE0o".getBytes(), "KbMLG85rOFF3Clf5".getBytes());
        String xx = Base64.encode(crypto.encrypt(("result").getBytes()));
        System.out.println(xx);

        String yy = new String(crypto.decrypt(Base64.decode(xx.getBytes())), "UTF-8");
        System.out.println(yy);
    }

    @Test
    public void testDesHutool() throws Exception {
        String src = "熊诗言";
        Crypto crypto = new DES();
        String xx = crypto.encrypt(src);
        System.out.println(xx);
        Assert.assertEquals(src, crypto.decrypt(xx));

        crypto = new DES(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 9, 9, 9, 9, 9, 9});
        String yy = crypto.encrypt(src);
        Assert.assertEquals("23B299907E3A1824EF2B52A7C8AE6C75", yy);
        Assert.assertEquals(src, crypto.decrypt(yy));
    }

    @Test
    public void testRsa() throws Exception {
        String src = "熊诗言";
        //应该是产生之后就保存下来
        KeyPair keyPair = KeyGenerator.generateKeyPair(AsymmetricAlgorithm.RSA.getValue());
        Crypto crypto = new RSA(keyPair.getPrivate(), keyPair.getPublic());
        String encrypt = crypto.encrypt(src);
        System.out.println(encrypt);
        System.out.println(crypto.decrypt(encrypt));
    }

    @Test(expected = CryptoException.class)
    public void testDigester() throws Exception {
        String src = "熊诗言";
        Crypto crypto = new Digester(DigestAlgorithm.SHA256);
        String xx = crypto.encrypt(src);
        Assert.assertEquals("2E110C5A647562F0565FBF2249382AD1DD696D450C6F9223CA92BACA3EFA75B6", xx);
        System.out.println(crypto.decrypt(xx));
    }

    @Test(expected = CryptoException.class)
    public void testHmac() throws Exception {
        String src = "熊诗言";
        Crypto crypto = new HMac(HmacAlgorithm.HmacSHA256, new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        String xx = crypto.encrypt(src);
        System.out.println(xx);
        Assert.assertEquals("3C2554DF76D07DE2B425F2F4DA295C3BA1CAA28EE0F96A79528C67FA093BEE99", xx);
        System.out.println(crypto.decrypt(xx));
    }

    /**
     * 测试整数的二进制表示
     */
    @Test
    public void testHex() throws Exception {
        byte[] bytes = "熊诗言".getBytes("UTF-8");
        Assert.assertEquals("E7868AE8AF97E8A880", RadixUtil.toHex(bytes));
        Assert.assertEquals("e7868ae8af97e8a880", RadixUtil.toHexLower(bytes));
    }
}
