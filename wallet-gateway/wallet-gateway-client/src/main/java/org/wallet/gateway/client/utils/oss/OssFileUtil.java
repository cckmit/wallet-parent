package org.wallet.gateway.client.utils.oss;

import org.wallet.dap.common.utils.EnvironmentUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * OSS 上传业务工具类，
 * @author zengfucheng
 */
public class OssFileUtil {

    public static String SEPARATOR = EnvironmentUtil.getProperty("oss.systemEnvironment");

    /**
     * @param fileName 文件原名
     * @return 返回一个新(当前时间 + 4位随机数)，如：
     */
    public static String newFileName(String fileName) {
        // 生成16位的UUID,重定生成(大写+小写+数pb ) + 后缀名
        return generateShortUuid() + getFileSuffix(fileName);
    }

    /**
     * 获取时间文件夹目录
     *
     * @return
     */
    public static String getTimeFolder() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 跟据文件的类型，图片、视频、文件，存到不同的目录。
     *
     * @return fileName
     */
    public static String getFileCatalogue(String fileName) {
        StringBuilder fileCatalogue = new StringBuilder();
        if(null == fileName){
            return null;
        }
        String fileSuffix = getFileSuffix(fileName);
        if(null == fileSuffix){
            return null;
        }
        if (EnvironmentUtil.getProperty("oss.fileSuffixImg").toLowerCase().contains(fileSuffix.toLowerCase())) {
            fileCatalogue.append("img").append(SEPARATOR).append(getTimeFolder()).append(SEPARATOR);
        } else if (EnvironmentUtil.getProperty("oss.fileSuffixVideo").toLowerCase().contains(fileSuffix.toLowerCase())) {
            fileCatalogue.append("video").append(SEPARATOR).append(getTimeFolder()).append(SEPARATOR);
        } else {
            fileCatalogue.append("file").append(SEPARATOR).append(getTimeFolder()).append(SEPARATOR);
        }
        return fileCatalogue.toString();
    }

    /**
     * 获取文件名的后缀
     *
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        return fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".", fileName.length())) : null;
    }


    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
            "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4",
            "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public static String generateShortUuid() {
        StringBuilder shortBuffer = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        Integer integer=16;
        for (int i = 0; i < integer; i++) {
            String str = uuid.substring(i * 2, i * 2 + 2);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }
}
