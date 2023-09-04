package com.consensus.gtv.poller.models.event;

import com.consensus.gtv.poller.models.rawdata.DataOperation;
import com.consensus.gtv.poller.models.rawdata.IspCustomerData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IspNewCustomerEvent extends AdapterEvent {

    public static final String TYPE = "isp-new-customer";

    private String tableName;
    private DataOperation operation;
    private IspCustomerData data;

    public IspNewCustomerEvent() {
        this.eventType = TYPE;
    }
}
