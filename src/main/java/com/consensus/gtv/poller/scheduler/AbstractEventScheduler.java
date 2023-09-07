package com.consensus.gtv.poller.scheduler;

import com.amazonaws.services.dynamodbv2.LockItem;
import com.consensus.common.util.CCSITimeUtils;
import com.consensus.gtv.poller.config.properties.PollerProperties.PollerJob;
import com.consensus.gtv.poller.config.properties.PollerProperties.Trigger;
import com.consensus.gtv.poller.models.DbLockData;
import com.consensus.gtv.poller.models.sqs.BaseSqsEvent;
import com.consensus.gtv.poller.repository.LockRepository;
import com.consensus.gtv.poller.service.IspDataPublishService;
import com.consensus.gtv.poller.util.ByteBufferUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.util.StopWatch;

import java.nio.ByteBuffer;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.capitalize;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventScheduler<T extends BaseSqsEvent<?>> implements SchedulingConfigurer {

    protected static final int SQS_BATCH_SIZE = 10;

    private final IspDataPublishService ispDataPublishService;
    private final LockRepository lockRepository;
    private final PollerJob schedulerJob;
    private final ObjectMapper objectMapper;

    protected abstract String jobType();

    protected abstract String jobLockId();

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (isNull(schedulerJob) || !schedulerJob.isEnabled()) {
            LOG.warn("!!! {} event schedule trigger is disabled !!!", capitalize(jobType()));
            return;
        }

        Trigger trigger = schedulerJob.getTrigger();
        var jobTrigger = new PeriodicTrigger(trigger.getPeriod(), trigger.getTimeUnit());
        taskRegistrar.addTriggerTask(this::pollEventsJob, jobTrigger);

        LOG.warn("!!! {} event schedule trigger enabled !!!", capitalize(jobType()));
    }

    private void pollEventsJob() {
        LOG.info("{} event polling job started.", capitalize(jobType()));
        StopWatch stopWatch = CCSITimeUtils.startStopWatch();

        try {
            int batchSize = schedulerJob.getBatchSize();
            List<T> polledEvents = emptyList();

            do {
                LockItem jobLock = lockRepository.acquireLockById(jobLockId());
                if (jobLock == null) {
                    LOG.info("Other {} event job is in progress. Quitting job.", jobType());
                    break;
                }

                ByteBuffer lockByteData = jobLock.getData().orElse(null);
                try {
                    // Get job data from lock
                    DbLockData dbLockData = deserializeLockData(lockByteData);

                    // Poll ISP events
                    JobPollResult<T> jobPollResult = pollEvents(dbLockData, batchSize);
                    polledEvents = jobPollResult.getPolledEvents();

                    // Send ISP events to SQS in batches
                    ListUtils.partition(polledEvents, SQS_BATCH_SIZE)
                            .forEach(ispDataPublishService::sendMessageBatch);

                    // Update lock data
                    lockByteData = serializeLockData(jobPollResult.getNewLockData());
                } finally {
                    lockRepository.releaseLock(jobLock, lockByteData);
                }

            } while (polledEvents.size() >= batchSize);
        } catch (Exception ex) {
            LOG.error("Exception executing {} event polling job: {}", jobType(), ex.getMessage(), ex);
        } finally {
            long jobTime = CCSITimeUtils.getTimeMillis(stopWatch);
            LOG.info("{} event polling job completed in {}(ms)", capitalize(jobType()), jobTime);
        }
    }

    protected abstract JobPollResult<T> pollEvents(DbLockData dbLockData, int batchSize);

    @SneakyThrows
    protected DbLockData deserializeLockData(ByteBuffer byteBuffer) {
        if (byteBuffer == null || !byteBuffer.hasRemaining()) {
            return new DbLockData();
        }
        String bufferData = ByteBufferUtils.toString(byteBuffer);
        return objectMapper.readValue(bufferData, DbLockData.class);
    }

    @SneakyThrows
    protected ByteBuffer serializeLockData(DbLockData dbLockData) {
        if (dbLockData == null) {
            return null;
        }
        return ByteBufferUtils.toByteBuffer(objectMapper.writeValueAsString(dbLockData));
    }

    @Data
    @Builder
    protected static class JobPollResult<T extends BaseSqsEvent<?>> {
        private List<T> polledEvents;
        private DbLockData newLockData;
    }
}
