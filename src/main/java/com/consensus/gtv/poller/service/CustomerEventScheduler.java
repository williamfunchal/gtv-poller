package com.consensus.gtv.poller.service;

import com.consensus.common.util.CCSITimeUtils;
import com.consensus.gtv.poller.config.properties.PollerProperties;
import com.consensus.gtv.poller.models.dto.IspS3CustomerDTO;
import com.consensus.gtv.poller.models.sqs.IspNewCustomerEvent;
import com.consensus.gtv.poller.models.mapper.IspCustomerMapper;
import com.consensus.gtv.poller.repository.LockRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class CustomerEventScheduler implements SchedulingConfigurer {

    private final IspDataPublishService ispDataPublishService;
    private final S3ReaderService<IspS3CustomerDTO> s3ReaderService;
    private final LockRepository lockRepository;
    private final PollerProperties.PollerJob customerJob;
    private final IspCustomerMapper ispCustomerMapper;
    private final ObjectMapper objectMapper;

    public CustomerEventScheduler(IspDataPublishService ispDataPublishService,
            S3ReaderService<IspS3CustomerDTO> s3ReaderService,
            LockRepository lockRepository, PollerProperties pollerProperties,
            IspCustomerMapper ispCustomerMapper, ObjectMapper objectMapper) {
        this.ispDataPublishService = ispDataPublishService;
        this.s3ReaderService = s3ReaderService;
        this.lockRepository = lockRepository;
        this.customerJob = pollerProperties.getCustomer();
        this.ispCustomerMapper = ispCustomerMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (isNull(customerJob) || !customerJob.isEnabled()) {
            LOG.warn("!!! Customer event schedule trigger is disabled !!!");
            return;
        }

        PollerProperties.Trigger trigger = customerJob.getTrigger();
        var jobTrigger = new PeriodicTrigger(trigger.getPeriod(), trigger.getTimeUnit());
        taskRegistrar.addTriggerTask(this::pollCustomerEvents, jobTrigger);
    }

    private void pollCustomerEvents() {
        try {
            LOG.info("Customer event polling job started.");
            StopWatch stopWatch = CCSITimeUtils.startStopWatch();

            int batchSize = customerJob.getBatchSize();

            //Read CSV Customer content from S3
            List<IspS3CustomerDTO> ispS3CustomerDTO = s3ReaderService.readCsvFromS3(IspS3CustomerDTO.class);

            for (IspS3CustomerDTO customerDTO : ispS3CustomerDTO) {
                LOG.debug("IspS3CustomerDTO: {}", customerDTO);

                IspNewCustomerEvent newCustomerEvent = ispCustomerMapper.map(customerDTO);
                String message = objectMapper.writeValueAsString(newCustomerEvent);
                // TODO override to push message in batches up to 10 - figure out eventId/dedupId/groupId
                //ispDataPublishService.publishMessageToQueue(message, SqsUtils.createMessageAttributesWithCorrelationId(newCustomerEvent.getCorrelationId()));
            }

            long jobTime = CCSITimeUtils.getTimeMillis(stopWatch);
            LOG.info("Usage event polling job completed in {}(ms)", jobTime);
        } catch (Exception ex) {
            LOG.error("Exception executing usage event polling job: {}", ex.getMessage(), ex);
        }
    }
}
