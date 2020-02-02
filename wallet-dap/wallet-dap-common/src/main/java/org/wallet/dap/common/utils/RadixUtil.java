package org.wallet.dap.common.utils;

/**
 * @author zengfucheng
 **/
public class RadixUtil {
    /**
     * 字节转十六进制
     * @param b 需要进行转换的byte字节
     * @return 转换后的Hex字符串
     */
    public static String toHex(byte b){
        String hex = Integer.toHexString(b & 0xFF);
        if(hex.length() < 2){
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 字节数组转16进制
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    public static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 字节数组转16进制小写
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    public static String toHexLower(byte[] bytes) {
        return toHex(bytes).toLowerCase();
    }

    /**
     * Hex字符串转byte
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte toByte(String inHex){
        return (byte)Integer.parseInt(inHex,16);
    }

    /**
     * hex字符串转byte数组
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] toBytes(String inHex){
        int hexLen = inHex.length();
        byte[] result;
        if (hexLen % 2 == 1){
            //奇数
            hexLen++;
            result = new byte[(hexLen/2)];
            inHex="0"+inHex;
        }else {
            //偶数
            result = new byte[(hexLen/2)];
        }
        int j=0;
        for (int i = 0; i < hexLen; i+=2){
            result[j]= toByte(inHex.substring(i,i+2));
            j++;
        }
        return result;
    }
}
