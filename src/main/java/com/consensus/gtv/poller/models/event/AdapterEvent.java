package com.consensus.gtv.poller.models.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "event_type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DataMappingStoreEvent.class, name = DataMappingStoreEvent.TYPE),
        @JsonSubTypes.Type(value = GtvAccountCreationEvent.class, name = GtvAccountCreationEvent.TYPE),
        @JsonSubTypes.Type(value = IspNewCustomerEvent.class, name = IspNewCustomerEvent.TYPE)
})
public abstract class AdapterEvent {
    protected String eventType;
    protected UUID correlationId;
}
