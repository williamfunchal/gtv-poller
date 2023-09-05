package com.consensus.gtv.poller.util;

import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.consensus.gtv.poller.models.sqs.BaseSqsEvent;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.consensus.common.sqs.CCSIQueueConstants.MessageAttributes.CORRELATION_ID;
import static com.consensus.common.sqs.CCSIQueueConstants.MessageAttributes.EVENT_TYPE;

@UtilityClass
public class SqsUtils {

    public static final String EVENT_ID_ATTR = "EVENT_ID";

    public static MessageAttributeValue createAttribute(Object value) {
        return new MessageAttributeValue()
                .withDataType("String")
                .withStringValue(getValueString(value));
    }

    private static String getValueString(Object value) {
        return Optional.ofNullable(value)
                .map(Objects::toString)
                .filter(StringUtils::hasText).orElse("null");
    }

    public static Map<String, MessageAttributeValue> createMessageAttributes(BaseSqsEvent<?> sqsEvent) {
        Map<String, MessageAttributeValue> attributes = new HashMap<>();
        attributes.put(EVENT_ID_ATTR, createAttribute(sqsEvent.getEventId()));
        attributes.put(EVENT_TYPE, createAttribute(sqsEvent.getEventType()));
        attributes.put(CORRELATION_ID, createAttribute(sqsEvent.getCorrelationId()));
        return attributes;
    }
}
