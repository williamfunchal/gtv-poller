package com.consensus.gtv.poller.sqs;

import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.consensus.common.sqs.CCSIQueueProperties;

import java.util.Map;

public interface QueuePublishService {

    CCSIQueueProperties getQueueProperties();

    SendMessageResult publishMessageToQueue(String message, Map<String, MessageAttributeValue> attributes);
}
