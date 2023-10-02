package com.consensus.gtv.poller.service;

import com.amazonaws.services.s3.AmazonS3;
import com.consensus.gtv.poller.config.properties.AwsS3Properties;
import com.consensus.gtv.poller.models.dto.IspS3CustomerDTO;
import org.springframework.stereotype.Service;

@Service
public class CustomerS3ReaderService extends AbstractS3ReaderService<IspS3CustomerDTO>{

    public CustomerS3ReaderService(AmazonS3 s3Client, AwsS3Properties awsS3Properties) {
        super(s3Client, awsS3Properties.getBucketName(), awsS3Properties.getCustomerPrefix());
    }
}
