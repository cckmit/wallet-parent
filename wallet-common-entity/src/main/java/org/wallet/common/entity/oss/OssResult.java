package org.wallet.common.entity.oss;

import java.io.Serializable;

/**
 * 上传文件到阿里   返回值。
 * @author zengfucheng
 */
public class OssResult implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 上传状态 True Success
     */
    private boolean status;
    /**
     * 返回HTTPS 访问地址
     */
    private String httpsFilePath;
    /**
     * 上传后的文件MD5数字唯一签名: 例：40F4131427068E08451D37F02021473A
     */
    private String signatureMD5;
    /**
     * 路径
     */
    private String path;
    /**
     * 文件名
     */
    private String fileName;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getHttpsFilePath() {
        return httpsFilePath;
    }

    public void setHttpsFilePath(String httpsFilePath) {
        this.httpsFilePath = httpsFilePath;
    }

    public String getSignatureMD5() {
        return signatureMD5;
    }

    public void setSignatureMD5(String signatureMD5) {
        this.signatureMD5 = signatureMD5;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    @Override
    public String toString() {
        return "OssResult{" + "status=" + status + ", httpsFilePath='" + httpsFilePath + '\'' + ", signatureMD5='" + signatureMD5 + '\'' + ", path='" + path + '\'' + ", fileName='" + fileName + '\'' + '}';
    }
}
