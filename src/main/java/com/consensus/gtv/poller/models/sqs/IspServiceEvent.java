package com.consensus.gtv.poller.models.sqs;

import com.consensus.gtv.poller.models.rawdata.IspServiceData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IspServiceEvent extends BaseSqsEvent<IspServiceData>{

    public static final String TYPE = "service-isp";

    public IspServiceEvent() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return "service";
    }
}
