package com.consensus.gtv.poller.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@ConfigurationProperties("aws.s3")
@Configuration
public class AwsS3Properties {

    private String bucketName;
    private String customerPrefix;
}
