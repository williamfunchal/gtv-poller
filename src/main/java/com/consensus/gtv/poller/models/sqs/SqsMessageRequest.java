package com.consensus.gtv.poller.models.sqs;

import com.amazonaws.services.sqs.model.MessageAttributeValue;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class SqsMessageRequest<T extends BaseSqsEvent> {

    private String messageId;
    private T payload;
    private String groupId;
    private String deduplicationId;
    private Map<String, MessageAttributeValue> attributes;
}
