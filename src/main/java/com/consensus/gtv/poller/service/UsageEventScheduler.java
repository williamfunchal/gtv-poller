package com.consensus.gtv.poller.service;

import com.consensus.common.util.CCSITimeUtils;
import com.consensus.gtv.poller.config.properties.PollerProperties;
import com.consensus.gtv.poller.config.properties.PollerProperties.PollerJob;
import com.consensus.gtv.poller.config.properties.PollerProperties.Trigger;
import com.consensus.gtv.poller.repository.CoreDbRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import static java.util.Objects.isNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsageEventScheduler implements SchedulingConfigurer {

    private final PollerProperties pollerProperties;
    private final CoreDbRepository coreDbRepository;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        PollerJob usage = pollerProperties.getUsage();
        if (isNull(usage) || !usage.isEnabled()) {
            LOG.warn("!!! Usage event schedule trigger is disabled !!!");
            return;
        }

        Trigger trigger = usage.getTrigger();
        var jobTrigger = new PeriodicTrigger(trigger.getPeriod(), trigger.getTimeUnit());
        taskRegistrar.addTriggerTask(this::pollUsageEvents, jobTrigger);
    }

    private void pollUsageEvents() {
        try {
            LOG.info("Usage event polling job started.");
            StopWatch stopWatch = CCSITimeUtils.startStopWatch();

            // TODO Implement logic to poll Core DB for usage events and sent to SQS
            String cloudDbTime = coreDbRepository.getCloudDbTime();
            LOG.info(">>>Test Purposes<<< Core CloudDB local time: {} >>>Test Purposes<<<", cloudDbTime);
            // -------

            long jobTime = CCSITimeUtils.getTimeMillis(stopWatch);
            LOG.info("Usage event polling job completed in {}(ms)", jobTime);
        } catch (Exception ex) {
            LOG.error("Exception executing usage event polling job: {}", ex.getMessage(), ex);
        }
    }
}
