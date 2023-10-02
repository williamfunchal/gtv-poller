package com.consensus.gtv.poller.models.sqs;

import com.consensus.gtv.poller.models.rawdata.IspUsageData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IspNewUsageEvent extends BaseSqsEvent<IspUsageData> {

    public static final String TYPE = "isp-new-usage";

    public IspNewUsageEvent() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return "usage";
    }
}
