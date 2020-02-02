package org.wallet.gateway.client.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wallet.gateway.client.utils.oss.AvatarDispose;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取网络地址图片信息
 * @author zengfucheng
 */
public class CatchPic {
    private static Logger logger = LoggerFactory.getLogger(CatchPic.class);

    /**
     * 获取所有网络链接的图片流信息。
     *
     * @param listUrl
     * @return
     */
    public static List<InputStream> headUrlToInputStream(List<String> listUrl) {
        if (listUrl == null) {
            return null;
        }
        List<InputStream> inputStreamList = new ArrayList<>();
        for (String url : listUrl) {
            String urlDispose=AvatarDispose.getUrlHeadPathDispose(url);
            try {
                byte[] bytes = CatchPic.getNetUrlImg(urlDispose);
                InputStream inputStream = CatchPic.byte2Input(bytes);
                inputStreamList.add(inputStream);
            }catch (Exception e){
                logger.error("NetWork not get inputStream. urlDispose fileName:{}, Error message:{}",urlDispose,e.getMessage(),e);
            }
        }
        return inputStreamList;
    }

    /**
     * 将byte[] 转化为InputStream
     *
     * @param buf
     * @return
     */
    public static final InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }


    /**
     * fileUrl网络资源地址,并且返回相应的[]byte
     *
     * @param fileUrl
     * @return
     */
    private static byte[] getNetUrlImg(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            /* 此为联系获得网络资源的固定格式用法，以便后面的in变量获得url截取网络资源的输入流 */
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(30000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            //将流转化为[]byte
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int rc = 0;
            while ((rc = in.read(buffer, 0, 4096)) > 0) {
                swapStream.write(buffer, 0, rc);
            }
            byte[] in2b = swapStream.toByteArray();
            swapStream.close();
            in.close();
            connection.disconnect();
            return in2b;
        } catch (Exception e) {
            logger.error("getNetUrlImg error:{}" , e.getMessage(), e);
            return null;
        }
    }


    /**
     * fileUrl网络资源地址,并且将资料保存到指定目录。
     *
     * @param fileUrl
     * @param savePath
     * @return
     */
    public static String saveUrlAs(String fileUrl, String savePath) {
        try {
            /* 将网络资源地址传给,即赋值给url */
            URL url = new URL(fileUrl);
            /* 此为联系获得网络资源的固定格式用法，以便后面的in变量获得url截取网络资源的输入流 */
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            DataInputStream in = new DataInputStream(connection.getInputStream());
            /* 此处也可用BufferedInputStream与BufferedOutputStream  需要保存的路径*/
            DataOutputStream out = new DataOutputStream(new FileOutputStream(savePath));
            /* 将参数savePath，即将截取的图片的存储在本地地址赋值给out输出流所指定的地址 */
            byte[] buffer = new byte[4096];
            int count = 0;
            /* 将输入流以字节的形式读取并写入buffer中 */
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.close();/* 后面三行为关闭输入输出流以及网络资源的固定格式 */
            in.close();
            connection.disconnect();
            /* 网络资源截取并存储本地成功返回 地址 */
            return savePath;
        } catch (Exception e) {
            logger.error("saveUrlAs e:{},fileUrl:{}",e.getMessage() ,fileUrl + savePath,e);
            return null;
        }
    }


}
