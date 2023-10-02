package com.consensus.gtv.poller.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("aws.s3")
public class AwsS3Properties {

    private String bucketName;
    private String customerPrefix;
    private String servicePrefix;
    private String corpProfilePrefix;
}
