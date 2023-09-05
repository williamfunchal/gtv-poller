package com.consensus.gtv.poller.models.sqs;

import com.consensus.gtv.poller.models.rawdata.IspCustomerData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IspNewCustomerEvent extends BaseSqsEvent<IspCustomerData> {

    public static final String TYPE = "isp-new-customer";

    public IspNewCustomerEvent() {
        this.eventType = TYPE;
    }
}