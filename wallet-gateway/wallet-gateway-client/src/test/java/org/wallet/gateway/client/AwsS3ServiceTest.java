package org.wallet.gateway.client;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.gateway.client.service.dubbo.AwsS3Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.util.List;

/**
 * @author zengfucheng
 **/
public class AwsS3ServiceTest extends SpringBootJUnitTest{

    private static S3Client s3;

    @Autowired
    AwsS3Service s3Service;

    @Test
    public void test(){
        Region region = Region.AP_NORTHEAST_1;

        s3 = S3Client.builder().region(region).build();

        String bucket = "walletsaasfile";
        String key = "app/1535036086989.png";

//        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucket).key(key).build();
//        s3.deleteObject(deleteObjectRequest);

//        for (int i = 0; i < 10; i++) {
//            long start = System.currentTimeMillis();
//
//            key = "app/" + start + ".png";
//
//            // Put Object
//            s3.putObject(PutObjectRequest.builder().bucket(bucket).key(key)
//                            .acl("public-read")
//                            .build(),
//                    RequestBody.fromFile(new File("C:\\Users\\Administrator\\Pictures\\1535036086989.png")));
//
//            System.out.printf("%s %s ms\n", i, (System.currentTimeMillis() - start));
//        }

        GetObjectAclRequest aclReq = GetObjectAclRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectAclResponse aclRes = s3.getObjectAcl(aclReq);
        List<Grant> grants = aclRes.grants();
        for (Grant grant : grants) {
            System.out.format(grant.toString());
        }
    }

    @Test
    public void testUpload(){
        ServiceRequest request = getRequest();
        request.setMethodName("upload");
        request.setParamValue("path", "app");
        request.setParamValue("file", new File("C:\\Users\\Administrator\\Pictures\\1535036086989.png"));
        log.info(JSON.toJSONString(s3Service.invoke(request)));
    }

    @Test
    public void testDelete(){
        ServiceRequest request = getRequest();
        request.setMethodName("delete");
        request.setParamValue("path", "app/1535036086989.png");
        log.info(JSON.toJSONString(s3Service.invoke(request)));
    }
}
