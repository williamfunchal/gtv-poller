package com.consensus.gtv.poller.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.consensus.gtv.poller.models.event.IspNewCustomerEvent;
import org.springframework.stereotype.Service;

import com.consensus.gtv.poller.models.dto.customer.IspS3CustomerDTO;
import com.consensus.gtv.poller.mapper.ISPDataReadyMapper;
import com.consensus.gtv.poller.sqs.ISPDataPublishService;
import com.consensus.gtv.poller.util.SqsUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ISPDataService {

    private final ISPDataPublishService ispDataPublishService;
    private final S3ReaderService<IspS3CustomerDTO> s3ReaderService;
    private final ISPDataReadyMapper ispDataReadyMapper;
    private ObjectMapper objectMapper;

    //@Scheduled(fixedDelayString = "${app.scheduler.isp-data-delay}")
    public void fetchISPData() throws IOException, URISyntaxException{
        LOG.info("New ISP data found");
        
        //Read CSV Customer content from S3
        List<IspS3CustomerDTO> ispS3CustomerDTO = s3ReaderService.readCsvFromS3(IspS3CustomerDTO.class);

         for(IspS3CustomerDTO customerDTO : ispS3CustomerDTO){
            LOG.info("CustomerDTO: {}", customerDTO);
             final IspNewCustomerEvent newCustomerEvent = ispDataReadyMapper.map(customerDTO);
             String message = objectMapper.writeValueAsString(newCustomerEvent);
            ispDataPublishService.publishMessageToQueue(message, SqsUtils.createMessageAttributesWithCorrelationId(newCustomerEvent.getCorrelationId().toString()));
        }             
    }
}
