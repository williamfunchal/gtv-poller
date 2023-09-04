package com.consensus.gtv.poller.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.consensus.common.sqs.CCSIQueueProperties;
import com.consensus.gtv.poller.config.properties.QueueProperties;
import org.springframework.stereotype.Service;

@Service
public class ISPDataPublishService extends AbstractQueuePublishService {

    private final CCSIQueueProperties queueProperties;

    public ISPDataPublishService(final AmazonSQS amazonSQS, final QueueProperties queueProperties) {
        super(amazonSQS);
        this.queueProperties = queueProperties.getIspDataReady();
    }

    @Override
    public CCSIQueueProperties getQueueProperties() {
        return this.queueProperties;
    }
}
