package com.consensus.gtv.poller.models.sqs;

import com.consensus.gtv.poller.models.rawdata.IspCorpProfileData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IspCorpProfileEvent extends BaseSqsEvent<IspCorpProfileData>{

    public static final String TYPE = "corp-profile-isp";

    public IspCorpProfileEvent() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return "service";
    }
}
