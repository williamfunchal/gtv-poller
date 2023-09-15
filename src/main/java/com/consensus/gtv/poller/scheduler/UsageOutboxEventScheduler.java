package com.consensus.gtv.poller.scheduler;

import com.consensus.gtv.poller.config.properties.PollerProperties;
import com.consensus.gtv.poller.models.DbLockData;
import com.consensus.gtv.poller.models.mapper.IspUsageMapper;
import com.consensus.gtv.poller.models.sqs.IspNewUsageEvent;
import com.consensus.gtv.poller.repository.CoreDbRepository;
import com.consensus.gtv.poller.repository.LockRepository;
import com.consensus.gtv.poller.service.IspDataPublishService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

import static java.time.ZoneOffset.UTC;
import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class UsageOutboxEventScheduler extends AbstractEventScheduler<IspNewUsageEvent> {

    private static final String JOB_NAME = "usage outbox";
    private static final String JOB_LOCK_ID = "USAGE_OUTBOX_POLLER_JOB_LOCK";

    private final CoreDbRepository coreDbRepository;
    private final IspUsageMapper ispUsageMapper;

    public UsageOutboxEventScheduler(IspDataPublishService ispDataPublishService, CoreDbRepository coreDbRepository,
            LockRepository lockRepository, PollerProperties pollerProperties, ObjectMapper objectMapper,
            IspUsageMapper ispUsageMapper) {
        super(ispDataPublishService, lockRepository, pollerProperties.getUsage(), objectMapper);
        this.coreDbRepository = coreDbRepository;
        this.ispUsageMapper = ispUsageMapper;
    }

    @Override
    protected String jobType() {
        return JOB_NAME;
    }

    @Override
    protected String jobLockId() {
        return JOB_LOCK_ID;
    }

    @Override
    protected JobPollResult<IspNewUsageEvent> pollEvents(DbLockData dbLockData, int batchSize) {
        ZonedDateTime startTimestamp = dbLockData.getTimestampPointer();
        ZonedDateTime endTimestamp = ZonedDateTime.now(UTC);

        List<IspNewUsageEvent> events = coreDbRepository.getUsageOutboxEvents(startTimestamp, endTimestamp, batchSize)
                .stream()
                .map(ispUsageMapper::toNewUsageEvent)
                .collect(toList());

        LOG.debug("Pooled 'IspNewUsageEvent' outbox events: {}", events);

        return JobPollResult.<IspNewUsageEvent>builder()
                .polledEvents(events)
                .newLockData(dbLockData.withTimestampPointer(endTimestamp))
                .build();
    }
}
