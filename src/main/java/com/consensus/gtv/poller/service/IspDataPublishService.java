package com.consensus.gtv.poller.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.consensus.common.sqs.CCSIQueueProperties;
import com.consensus.gtv.poller.config.properties.QueueProperties;
import com.consensus.gtv.poller.models.sqs.BaseSqsEvent;
import com.consensus.gtv.poller.util.SqsUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Component
public class IspDataPublishService {

    private final AmazonSQS amazonSQS;
    private final CCSIQueueProperties dataReadyQueue;
    private final ObjectMapper objectMapper;

    public IspDataPublishService(AmazonSQS amazonSQS, QueueProperties queueProperties, ObjectMapper objectMapper) {
        this.amazonSQS = amazonSQS;
        this.dataReadyQueue = queueProperties.getIspDataReady();
        this.objectMapper = objectMapper;
    }

    public <T extends BaseSqsEvent<?>> void sendMessageBatch(List<T> messageBatch) {
        LOG.debug("Publishing message batch to SQS {}", messageBatch);

        if (isEmpty(messageBatch) || messageBatch.size() > 10) {
            throw new IllegalArgumentException("Send SQS message batch size must be between 1 and 10");
        }

        SendMessageBatchRequest batchRequest = new SendMessageBatchRequest()
                .withQueueUrl(dataReadyQueue.getQueueUrl())
                .withEntries(messageBatch.stream().map(this::toRequestEntry).collect(toList()));

        amazonSQS.sendMessageBatch(batchRequest);
    }

    @SneakyThrows
    private SendMessageBatchRequestEntry toRequestEntry(BaseSqsEvent<?> message) {
        return new SendMessageBatchRequestEntry()
                .withId(message.getEventId())
                .withMessageBody(objectMapper.writeValueAsString(message))
                .withMessageAttributes(SqsUtils.createMessageAttributes(message))
                .withMessageGroupId("TBD");
    }
}
