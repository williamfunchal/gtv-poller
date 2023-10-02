package com.consensus.gtv.poller.service;

import com.amazonaws.services.s3.AmazonS3;
import com.consensus.gtv.poller.config.properties.AwsS3Properties;
import com.consensus.gtv.poller.models.dto.IspServiceDTO;
import org.springframework.stereotype.Service;

@Service
public class ServiceS3ReaderService extends AbstractS3ReaderService<IspServiceDTO>{

    public ServiceS3ReaderService(AmazonS3 s3Client, AwsS3Properties awsS3Properties) {
        super(s3Client, awsS3Properties.getBucketName(), awsS3Properties.getServicePrefix());
    }
}
