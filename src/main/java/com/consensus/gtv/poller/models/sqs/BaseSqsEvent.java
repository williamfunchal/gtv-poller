package com.consensus.gtv.poller.models.sqs;

import com.consensus.gtv.poller.models.rawdata.DataOperation;
import com.consensus.gtv.poller.models.rawdata.IspUsageData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "event_type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = IspNewUsageEvent.class, name = IspNewUsageEvent.TYPE),
        @JsonSubTypes.Type(value = IspNewCustomerEvent.class, name = IspNewCustomerEvent.TYPE)
})
public abstract class BaseSqsEvent<T> {

    protected String eventType;
    protected String tableName;
    protected DataOperation operation;
    protected T data;

    @JsonIgnore
    private String eventId;
    @JsonIgnore
    private String correlationId;
}
