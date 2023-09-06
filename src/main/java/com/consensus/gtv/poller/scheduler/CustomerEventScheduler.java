package com.consensus.gtv.poller.scheduler;

import com.consensus.gtv.poller.config.properties.PollerProperties;
import com.consensus.gtv.poller.models.DbLockData;
import com.consensus.gtv.poller.models.dto.IspS3CustomerDTO;
import com.consensus.gtv.poller.models.mapper.IspCustomerMapper;
import com.consensus.gtv.poller.models.sqs.IspNewCustomerEvent;
import com.consensus.gtv.poller.repository.LockRepository;
import com.consensus.gtv.poller.service.IspDataPublishService;
import com.consensus.gtv.poller.service.S3ReaderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class CustomerEventScheduler extends AbstractEventScheduler<IspNewCustomerEvent> {

    private static final String JOB_NAME = "customer";
    private static final String JOB_LOCK_ID = "CUSTOMER_POLLER_JOB_LOCK";

    private final S3ReaderService<IspS3CustomerDTO> s3ReaderService;
    private final IspCustomerMapper ispCustomerMapper;

    public CustomerEventScheduler(IspDataPublishService ispDataPublishService,
            S3ReaderService<IspS3CustomerDTO> s3ReaderService,
            LockRepository lockRepository, PollerProperties pollerProperties,
            IspCustomerMapper ispCustomerMapper, ObjectMapper objectMapper) {
        super(ispDataPublishService, lockRepository, pollerProperties.getCustomer(), objectMapper);
        this.s3ReaderService = s3ReaderService;
        this.ispCustomerMapper = ispCustomerMapper;
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
    protected JobPollResult<IspNewCustomerEvent> pollEvents(DbLockData dbLockData, int batchSize) {
        List<IspNewCustomerEvent> events = s3ReaderService.readCsvFromS3(IspS3CustomerDTO.class)
                .stream()
                .map(ispCustomerMapper::toNewCustomerEvent)
                .collect(toList());

        LOG.debug("Pooled 'IspNewCustomerEvent' events: {}", events);

        return JobPollResult.<IspNewCustomerEvent>builder()
                .polledEvents(events)
                .newLockData(dbLockData)
                .build();
    }
}
