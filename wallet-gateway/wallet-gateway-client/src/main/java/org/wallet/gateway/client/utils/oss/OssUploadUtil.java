package org.wallet.gateway.client.utils.oss;

import com.aliyun.oss.OSSClient;
import org.wallet.common.entity.oss.OssResult;
import org.wallet.dap.common.utils.EnvironmentUtil;

import java.io.File;
import java.io.InputStream;

/**
 * @author zengfucheng
 * OSS 文件上传服务
 */
public class OssUploadUtil {
    /**
     * 上传文件到阿里 OSS 服务器。
     *
     * @param file
     * @return
     */
    public static OssResult uploadFile(File file, String fileName) {
        OSSClient ossClient = AliyunOssClient.getOSSClient();
        return AliyunOssClient.uploadObject2OSS(ossClient,
                EnvironmentUtil.getProperty("oss.bucketName"),
                OssFileUtil.getFileCatalogue(fileName),
                OssFileUtil.newFileName(fileName), file);
    }

    /**
     * 上传文件到阿里 OSS 服务器。
     *
     * @param file
     * @return
     */
    public static OssResult uploadFile(File file) {
        OSSClient ossClient = AliyunOssClient.getOSSClient();
        return AliyunOssClient.uploadObject2OSS(ossClient,
                EnvironmentUtil.getProperty("oss.bucketName"),
                OssFileUtil.getFileCatalogue(file.getName()),
                OssFileUtil.newFileName(file.getName()), file);
    }

    /**
     * 上传文件到阿里 OSS 服务器。
     *
     * @param fileContent
     * @param fileName
     * @return
     */
    public static OssResult uploadFile(InputStream fileContent, String fileName) {
        OSSClient ossClient = AliyunOssClient.getOSSClient();
        return AliyunOssClient.uploadObject2OSS(ossClient,
                EnvironmentUtil.getProperty("oss.bucketName"),
                OssFileUtil.getFileCatalogue(fileName),
                OssFileUtil.newFileName(fileName), fileContent);
    }


    /**
     * 上传文件到阿里 OSS 服务器。(群组上传头像功能，无需重新生成头像。)
     *
     * @param fileContent
     * @param fileName
     * @return
     */
    public static OssResult uploadFileGroup(InputStream fileContent, String fileName, String pathName) {
        OSSClient ossClient = AliyunOssClient.getOSSClient();

        if (fileName == null || fileName == "") {
            fileName = OssFileUtil.newFileName(fileName);
        }
        if (pathName == null || pathName == "") {
            pathName = OssFileUtil.getFileCatalogue(fileName);
        }
        return AliyunOssClient.uploadObject2OSS(ossClient,
                EnvironmentUtil.getProperty("oss.bucketName"),
                pathName,
                fileName, fileContent);
    }


    /**
     * 在 OSS 服务上，创建文件夹
     * 例： aa/    folder1/aa/cc  等循环创建
     */
    public static String createFolder(String folder) {
        OSSClient ossClient = AliyunOssClient.getOSSClient();
        return AliyunOssClient.createFolder(ossClient,
                EnvironmentUtil.getProperty("oss.bucketName"), folder);
    }

    /**
     * 删除文件
     * 例如：  folder :img/20180909/   key :15240216524003131.jpg
     *
     * @param folder
     * @param key
     * @return
     */
    public static void deleteFile(String folder, String key) {
        OSSClient ossClient = AliyunOssClient.getOSSClient();
        AliyunOssClient.deleteFile(ossClient, EnvironmentUtil.getProperty("oss.bucketName"), folder, key);

    }
}
