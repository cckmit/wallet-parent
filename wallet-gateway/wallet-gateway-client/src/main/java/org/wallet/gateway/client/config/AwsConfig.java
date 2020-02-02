package org.wallet.gateway.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * @author zengfucheng
 **/
@Configuration
@EnableConfigurationProperties(AwsProperties.class)
public class AwsConfig {
    @Autowired
    AwsProperties awsProperties;

    @Bean
    S3Client s3Client(Region region, AwsCredentialsProvider awsCredentialsProvider){
        return S3Client.builder()
                .region(region)
                .credentialsProvider(awsCredentialsProvider)
                .build();
    }

    @Bean
    AwsCredentialsProvider credentialsProvider(){
        return () -> new AwsCredentials(){
            @Override
            public String accessKeyId() {
                return awsProperties.getS3().getAccessKeyId();
            }

            @Override
            public String secretAccessKey() {
                return awsProperties.getS3().getAccessKeySecret();
            }
        };
    }

    @Bean
    Region region(){
        return Region.of(awsProperties.getS3().getRegion().toLowerCase());
    }
}
