package com.consensus.gtv.poller.service;

import com.amazonaws.services.dynamodbv2.LockItem;
import com.consensus.common.util.CCSITimeUtils;
import com.consensus.gtv.poller.config.properties.PollerProperties;
import com.consensus.gtv.poller.config.properties.PollerProperties.PollerJob;
import com.consensus.gtv.poller.config.properties.PollerProperties.Trigger;
import com.consensus.gtv.poller.models.DbLockData;
import com.consensus.gtv.poller.models.sqs.IspNewUsageEvent;
import com.consensus.gtv.poller.models.mapper.IspUsageMapper;
import com.consensus.gtv.poller.repository.CoreDbRepository;
import com.consensus.gtv.poller.repository.LockRepository;
import com.consensus.gtv.poller.util.ByteBufferUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.nio.ByteBuffer;
import java.time.ZonedDateTime;
import java.util.List;

import static java.time.ZoneOffset.UTC;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class UsageEventScheduler implements SchedulingConfigurer {

    private static final String USAGE_LOCK_ID = "USAGE_POLLER_JOB_LOCK";
    private static final int SQS_BATCH_SIZE = 10;

    private final IspDataPublishService ispDataPublishService;
    private final CoreDbRepository coreDbRepository;
    private final LockRepository lockRepository;
    private final PollerJob usageJob;
    private final ObjectMapper objectMapper;
    private final IspUsageMapper ispUsageMapper;

    public UsageEventScheduler(IspDataPublishService ispDataPublishService, CoreDbRepository coreDbRepository,
            LockRepository lockRepository, PollerProperties pollerProperties, ObjectMapper objectMapper,
            IspUsageMapper ispUsageMapper) {
        this.ispDataPublishService = ispDataPublishService;
        this.coreDbRepository = coreDbRepository;
        this.lockRepository = lockRepository;
        this.usageJob = pollerProperties.getUsage();
        this.objectMapper = objectMapper;
        this.ispUsageMapper = ispUsageMapper;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (isNull(usageJob) || !usageJob.isEnabled()) {
            LOG.warn("!!! Usage event schedule trigger is disabled !!!");
            return;
        }

        Trigger trigger = usageJob.getTrigger();
        var jobTrigger = new PeriodicTrigger(trigger.getPeriod(), trigger.getTimeUnit());
        taskRegistrar.addTriggerTask(this::pollUsageEvents, jobTrigger);
    }

    private void pollUsageEvents() {
        LOG.info("Usage event polling job started.");
        StopWatch stopWatch = CCSITimeUtils.startStopWatch();

        try {
            int batchSize = usageJob.getBatchSize();
            List<IspNewUsageEvent> usageEvents = emptyList();

            do {
                LockItem jobLock = lockRepository.acquireLockById(USAGE_LOCK_ID);
                if (jobLock == null) {
                    LOG.info("Other usage event job is in progress. Quitting job.");
                    break;
                }

                ByteBuffer lockByteData = jobLock.getData().orElse(null);
                try {
                    DbLockData dbLockData = deserializeLockData(lockByteData);
                    ZonedDateTime startTimestamp = dbLockData.getTimestampPointer();
                    ZonedDateTime endTimestamp = ZonedDateTime.now(UTC);

                    usageEvents = coreDbRepository.getUsageEvents(startTimestamp, endTimestamp, batchSize)
                            .stream()
                            .map(ispUsageMapper::toNewUsageEvent)
                            .collect(toList());

                    ListUtils.partition(usageEvents, SQS_BATCH_SIZE).forEach(ispDataPublishService::sendMessageBatch);

                    lockByteData = serializeLockData(dbLockData.withTimestampPointer(endTimestamp));
                } finally {
                    lockRepository.releaseLock(jobLock, lockByteData);
                }

                // remove after
                String cloudDbTime = coreDbRepository.getCloudDbTime();
                LOG.info("!!!!! >>>Test Purposes<<< Core CloudDB local time: {} - One Iteration Complete >>>Test Purposes<<< !!!!!", cloudDbTime);
                // ---

            } while (usageEvents.size() >= batchSize);
        } catch (Exception ex) {
            LOG.error("Exception executing usage event polling job: {}", ex.getMessage(), ex);
        } finally {
            long jobTime = CCSITimeUtils.getTimeMillis(stopWatch);
            LOG.info("Usage event polling job completed in {}(ms)", jobTime);
        }
    }

    @SneakyThrows
    private DbLockData deserializeLockData(ByteBuffer byteBuffer) {
        if (byteBuffer == null || !byteBuffer.hasRemaining()) {
            return new DbLockData(ZonedDateTime.now(UTC));
        }
        String bufferData = ByteBufferUtils.toString(byteBuffer);
        return objectMapper.readValue(bufferData, DbLockData.class);
    }

    @SneakyThrows
    private ByteBuffer serializeLockData(DbLockData dbLockData) {
        if (dbLockData == null) {
            return null;
        }
        return ByteBufferUtils.toByteBuffer(objectMapper.writeValueAsString(dbLockData));
    }
}
