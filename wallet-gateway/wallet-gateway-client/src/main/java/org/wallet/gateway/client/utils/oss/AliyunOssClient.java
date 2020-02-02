package org.wallet.gateway.client.utils.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wallet.common.entity.oss.OssResult;
import org.wallet.dap.common.utils.EnvironmentUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 使用阿里云OSS存储对象上传图片
 *
 * @author zengfucheng
 */
public class AliyunOssClient {
    private static Logger logger = LoggerFactory.getLogger(AliyunOssClient.class);

    /**
     * 获取阿里云OSS客户端对象
     *
     * @return ossClient
     */
    public static OSSClient getOSSClient() {
        return new OSSClient(EnvironmentUtil.getProperty("oss.name"),
                EnvironmentUtil.getProperty("oss.accessKeyId"),
                EnvironmentUtil.getProperty("oss.accessKeySecret"));
    }

    /**
     * 创建存储空间
     *
     * @param ossClient  OSS连接
     * @param bucketName 存储空间
     * @return
     */
    public static String createBucketName(OSSClient ossClient, String bucketName) {
        //存储空间
        if (!ossClient.doesBucketExist(bucketName)) {
            //创建存储空间
            Bucket bucket = ossClient.createBucket(bucketName);
            return bucket.getName();
        }
        return bucketName;
    }

    /**
     * 删除存储空间buckName
     *
     * @param ossClient  oss对象
     * @param bucketName 存储空间
     */
    public static void deleteBucket(OSSClient ossClient, String bucketName) {
        ossClient.deleteBucket(bucketName);
    }

    /**
     * 创建模拟文件夹
     *
     * @param ossClient  oss连接
     * @param bucketName 存储空间
     * @param folder     模拟文件夹名如"qj_nanjing/"
     * @return 文件夹名
     */
    public static String createFolder(OSSClient ossClient, String bucketName, String folder) {
        //文件夹名
        //判断文件夹是否存在，不存在则创建
        if (!ossClient.doesObjectExist(bucketName, folder)) {
            //创建文件夹
            ossClient.putObject(bucketName, folder, new ByteArrayInputStream(new byte[0]));
            //得到文件夹名
            OSSObject object = ossClient.getObject(bucketName, folder);
            return object.getKey();
        }
        return folder;
    }

    /**
     * 根据key删除OSS服务器上的文件
     *
     * @param ossClient  oss连接
     * @param bucketName 存储空间
     * @param folder     模拟文件夹名 如"qj_nanjing/"
     * @param key        Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     */
    public static void deleteFile(OSSClient ossClient, String bucketName, String folder, String key) {
        ossClient.deleteObject(bucketName, folder + key);
    }

    /**
     * 上传图片至OSS
     *
     * @param ossClient  oss连接
     * @param bucketName 存储空间
     * @param filePath   文件路径
     * @param fileName   文件名
     * @param file       上传文件（文件全路径如：D:\\image\\cake.jpg）
     * @return String 返回的唯一MD5数字签名
     */
    public static OssResult uploadObject2OSS(OSSClient ossClient, String bucketName,
                                             String filePath, String fileName, File file) {
        OssResult ossResult = new OssResult();
        String resultStr = null;
        try {
            //以输入流的形式上传文件
            InputStream is = new FileInputStream(file);
            //文件大小
            Long fileSize = file.length();
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            //上传的文件的长度
            metadata.setContentLength(is.available());
            //指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            //如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(fileName));
            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            //上传文件   (上传文件流的形式),bucketName,filepath
            PutObjectResult putResult = ossClient.putObject(
                    bucketName, filePath + fileName, is, metadata);
            //解析结果
            resultStr = putResult.getETag();
            //上传成功后，返回该问地址 , 及相关信息。
            ossResult.setStatus(true);
            String resultFilePath = "https://" +
                    EnvironmentUtil.getProperty("oss.bucketName") + "." +
                    EnvironmentUtil.getProperty("oss.name") +
                    OssFileUtil.SEPARATOR +
                    filePath + fileName;
            ossResult.setHttpsFilePath(resultFilePath);
            ossResult.setSignatureMD5(resultStr);
            ossResult.setFileName(fileName);
            ossResult.setPath(OssFileUtil.getFileCatalogue(file.getName()));
        } catch (Exception e) {
            ossResult.setStatus(false);
            logger.error("uploadObject2OSS error:{}", e.getMessage(), e);
        }
        return ossResult;
    }


    /**
     * 上传图片至OSS
     *
     * @param ossClient  oss连接
     * @param bucketName 存储空间
     * @param filePath   文件路径
     * @param fileName   文件名
     * @param is         上传文件流
     * @return String 返回的唯一MD5数字签名
     */
    public static OssResult uploadObject2OSS(OSSClient ossClient, String bucketName,
                                             String filePath, String fileName, InputStream is) {
        OssResult ossResult = new OssResult();
        String resultStr = null;
        try {
            //文件大小
            int fileSize = is.available();
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            //上传的文件的长度
            metadata.setContentLength(is.available());
            //指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            //如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(fileName));
            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            //上传文件   (上传文件流的形式),bucketName,filepath
            PutObjectResult putResult = ossClient.putObject(
                    bucketName, filePath + fileName, is, metadata);
            //解析结果
            resultStr = putResult.getETag();
            //上传成功后，返回该问地址 , 及相关信息。
            ossResult.setStatus(true);
            String resultFilePath = "https://" +
                    EnvironmentUtil.getProperty("oss.bucketName") + "." +
                    EnvironmentUtil.getProperty("oss.name") +
                    OssFileUtil.SEPARATOR +
                    filePath + fileName;
            ossResult.setHttpsFilePath(resultFilePath);
            ossResult.setSignatureMD5(resultStr);
            ossResult.setFileName(fileName);
            ossResult.setPath(filePath + fileName);
        } catch (Exception e) {
            ossResult.setStatus(false);
            logger.error("uploadObject2OSS error:{}", e.getMessage(), e);
        }
        return ossResult;
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    protected static String getContentType(String fileName) {
        //文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension) || ".png".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if (".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if (".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        //默认返回类型
        return "image/jpeg";
    }


}
