package com.consensus.gtv.poller.models.event;

import com.consensus.gtv.poller.models.IspGtvMapping;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DataMappingStoreEvent extends AdapterEvent {

    public static final String TYPE = "save-data-mapping";

    private IspGtvMapping ispGtvMapping;

    public DataMappingStoreEvent() {
        this.eventType = TYPE;
    }
}
