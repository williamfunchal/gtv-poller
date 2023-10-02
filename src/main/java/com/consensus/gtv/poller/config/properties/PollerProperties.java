package com.consensus.gtv.poller.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

@Data
@ConfigurationProperties("poller.config")
public class PollerProperties {

    private PollerJob usage;
    private PollerJob customer;
    private PollerJob service;
    private PollerJob corpProfile;
    private LockProperties lock;

    @Data
    public static class PollerJob {

        private boolean enabled;
        private int batchSize;
        private Trigger trigger;
    }

    @Data
    public static class Trigger {

        private long period;
        private TimeUnit timeUnit;
    }

    @Data
    public static class LockProperties {

        private long leaseDuration;
        private long heartbeatPeriod;
    }
}
