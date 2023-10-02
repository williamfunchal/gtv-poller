package com.consensus.gtv.poller.scheduler;

import com.consensus.common.util.CCSIUUIDUtils;
import com.consensus.gtv.poller.config.properties.PollerProperties;
import com.consensus.gtv.poller.models.DbLockData;
import com.consensus.gtv.poller.models.dto.IspCorpProfileDTO;
import com.consensus.gtv.poller.models.mapper.IspCorpProfileMapper;
import com.consensus.gtv.poller.models.rawdata.DataOperation;
import com.consensus.gtv.poller.models.sqs.IspCorpProfileEvent;
import com.consensus.gtv.poller.repository.LockRepository;
import com.consensus.gtv.poller.service.CorpProfileS3ReaderService;
import com.consensus.gtv.poller.service.IspDataPublishService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class CorpProfileEventScheduler extends AbstractEventScheduler<IspCorpProfileEvent>{
    private static final String JOB_NAME = "corp-profile";
    private static final String JOB_LOCK_ID = "CORP_PROFILE_POLLER_JOB_LOCK";

    private final CorpProfileS3ReaderService corpProfileS3ReaderService;
    private final IspCorpProfileMapper ispCorpProfileMapper;

    public CorpProfileEventScheduler(IspDataPublishService ispDataPublishService,
                                 CorpProfileS3ReaderService corpProfileS3ReaderService,
                                 LockRepository lockRepository, PollerProperties pollerProperties,
                                 IspCorpProfileMapper ispCorpProfileMapper, ObjectMapper objectMapper) {
        super(ispDataPublishService, lockRepository, pollerProperties.getCorpProfile(), objectMapper);
        this.corpProfileS3ReaderService = corpProfileS3ReaderService;
        this.ispCorpProfileMapper = ispCorpProfileMapper;
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
    protected JobPollResult<IspCorpProfileEvent> pollEvents(DbLockData dbLockData, int batchSize) {
        List<IspCorpProfileEvent> events = corpProfileS3ReaderService.readCsvFromS3(IspCorpProfileDTO.class)
                .stream()
                .map(this::createCorpProfileEvent)
                .collect(toList());

        LOG.debug("Pooled 'IspServiceEvent' events: {}", events);

        return JobPollResult.<IspCorpProfileEvent>builder()
                .polledEvents(events)
                .newLockData(dbLockData)
                .build();
    }

    private IspCorpProfileEvent createCorpProfileEvent(IspCorpProfileDTO ispCorpProfileDTO){
        final IspCorpProfileEvent ispCorpProfileEvent = new IspCorpProfileEvent();
        ispCorpProfileEvent.setOperation(DataOperation.fromValue(ispCorpProfileDTO.getOp()));
        ispCorpProfileEvent.setTableName("J2_CORP_PROFILE");
        ispCorpProfileEvent.setData(ispCorpProfileMapper.toCorpProfileData(ispCorpProfileDTO));
        ispCorpProfileEvent.setEventId(ispCorpProfileDTO.getResellerId());
        ispCorpProfileEvent.setCorrelationId(CCSIUUIDUtils.generateUUID());
        return ispCorpProfileEvent;
    }
}
