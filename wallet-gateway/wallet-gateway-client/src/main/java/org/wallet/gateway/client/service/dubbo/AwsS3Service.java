package org.wallet.gateway.client.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.wallet.dap.common.dubbo.*;
import org.wallet.gateway.client.config.AwsProperties;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;

/**
 * @author zengfucheng
 **/
@Service(group = DubboServiceGroup.CLIENT_AWS_S3, timeout = 30000)
@org.springframework.stereotype.Service
public class AwsS3Service extends BaseDubboService implements IService {

    @Autowired
    S3Client client;

    @Autowired
    AwsProperties awsProperties;

    public ServiceResponse upload(ServiceRequest request, ServiceResponse response) {
        String path = request.getParamValue("path");
        String fileName = request.getParamValue("fileName");
        File file = request.getParamValue("file");
        byte[] bytes = request.getParamValue("bytes");

        RequestBody requestBody;

        if(null != file){
            requestBody = RequestBody.fromFile(file);
        }else if(null != bytes){
            requestBody = RequestBody.fromBytes(bytes);
        }else{
            return Responses.missingParam("file|bytes");
        }

        fileName = StringUtils.isEmpty(fileName) && null != file ? file.getName() : fileName;
        String key = path + "/" + fileName;
        key = key.replace("//", "/");
        if(key.startsWith("/")){
            key = key.substring(1);
        }
        String bucket = awsProperties.getS3().getBucket();
        /*
         * ACL 上传文件权限
         *
         * private
         * public-read
         * public-read-write
         * aws-exec-read
         * authenticated-read
         * bucket-owner-read
         * bucket-owner-full-control
         */
        PutObjectResponse putObjectResponse = client.putObject(
                PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .acl("public-read")
                    .build(),
                requestBody);

        String domain = "https://" + bucket + ".s3." + awsProperties.getS3().getRegion() + ".amazonaws.com/";
        String url = domain + key;

        response.setResultValue("domain", domain);
        response.setResultValue("path", "/" + key);
        response.setResultValue("url", url);

        return response;
    }

    public ServiceResponse delete(ServiceRequest request, ServiceResponse response) {
        String path = request.getParamValue("path");
        if(StringUtils.isEmpty(path)){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[path]不存在！");
            return response;
        }

        String bucket = awsProperties.getS3().getBucket();

        client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(path).build());

        return response;
    }
}
