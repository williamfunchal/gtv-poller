package com.consensus.gtv.poller.config.properties;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("aws.sqs")
public class QueueProperties {

    private CCSIQueueListenerProperties ispDataReady;
}
