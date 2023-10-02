package com.consensus.gtv.poller;

import com.consensus.gtv.poller.config.properties.AwsS3Properties;
import com.consensus.gtv.poller.config.properties.PollerProperties;
import com.consensus.gtv.poller.config.properties.QueueProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication(scanBasePackages = {
        "com.consensus.gtv.poller",
        "com.consensus.common.sqs",
        "com.consensus.common.logging",
        "com.consensus.common.metrics",
        "com.consensus.common.dynamo.db",
        "com.consensus.common.health"
})
@EnableConfigurationProperties({QueueProperties.class, PollerProperties.class, AwsS3Properties.class})
public class GtvPollerService {
    
    public static void main(String[] args) {
        SpringApplication.run(GtvPollerService.class, args);
    }
}

