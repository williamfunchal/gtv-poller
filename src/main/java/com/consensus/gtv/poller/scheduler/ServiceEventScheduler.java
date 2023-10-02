package com.consensus.gtv.poller.scheduler;

import com.consensus.common.util.CCSIUUIDUtils;
import com.consensus.gtv.poller.config.properties.PollerProperties;
import com.consensus.gtv.poller.models.DbLockData;
import com.consensus.gtv.poller.models.dto.IspServiceDTO;
import com.consensus.gtv.poller.models.mapper.IspServiceMapper;
import com.consensus.gtv.poller.models.rawdata.DataOperation;
import com.consensus.gtv.poller.models.sqs.IspServiceEvent;
import com.consensus.gtv.poller.repository.LockRepository;
import com.consensus.gtv.poller.service.IspDataPublishService;
import com.consensus.gtv.poller.service.ServiceS3ReaderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.uuid.impl.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class ServiceEventScheduler extends AbstractEventScheduler<IspServiceEvent>{
    private static final String JOB_NAME = "service";
    private static final String JOB_LOCK_ID = "SERVICE_POLLER_JOB_LOCK";

    private final ServiceS3ReaderService serviceS3ReaderService;
    private final IspServiceMapper ispServiceMapper;

    public ServiceEventScheduler(IspDataPublishService ispDataPublishService,
                                  ServiceS3ReaderService serviceS3ReaderService,
                                  LockRepository lockRepository, PollerProperties pollerProperties,
                                  IspServiceMapper ispServiceMapper, ObjectMapper objectMapper) {
        super(ispDataPublishService, lockRepository, pollerProperties.getService(), objectMapper);
        this.serviceS3ReaderService = serviceS3ReaderService;
        this.ispServiceMapper = ispServiceMapper;
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
    protected JobPollResult<IspServiceEvent> pollEvents(DbLockData dbLockData, int batchSize) {
        List<IspServiceEvent> events = serviceS3ReaderService.readCsvFromS3(IspServiceDTO.class)
                .stream()
                .map(this::createServiceEvent)
                .collect(toList());

        LOG.debug("Pooled 'IspServiceEvent' events: {}", events);

        return JobPollResult.<IspServiceEvent>builder()
                .polledEvents(events)
                .newLockData(dbLockData)
                .build();
    }

    private IspServiceEvent createServiceEvent(IspServiceDTO ispServiceDTO){
        final IspServiceEvent ispServiceEvent = new IspServiceEvent();
        ispServiceEvent.setOperation(DataOperation.fromValue(ispServiceDTO.getOp()));
        ispServiceEvent.setTableName("ISPSERVICE");
        ispServiceEvent.setData(ispServiceMapper.toIspServiceData(ispServiceDTO));
        ispServiceEvent.setEventId(ispServiceDTO.getServiceKey());
        ispServiceEvent.setCorrelationId(CCSIUUIDUtils.generateUUID());
        return ispServiceEvent;
    }
}
