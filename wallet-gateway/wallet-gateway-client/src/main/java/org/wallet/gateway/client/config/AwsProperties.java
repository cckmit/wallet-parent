package org.wallet.gateway.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zengfucheng
 **/
@Data
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {
    /** S3 Service */
    private S3 s3;

    @Data
    public static class S3 {
        /** The region of the bucket */
        private String region;
        /** Bucket */
        private String bucket;
        /** Access Key ID(From <a href="https://console.aws.amazon.com/iam/home">IAM User</a>) */
        private String accessKeyId;
        /** Access Key Secret(From <a href="https://console.aws.amazon.com/iam/home">IAM User</a>) */
        private String accessKeySecret;
    }
}
