package com.consensus.gtv.poller.models.gtv.usage;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UsageEvent {
    private String serviceResourceIdentifier;
    private ZonedDateTime startTime;
    private UsageUom usageUom;
    private Long usageAmount;
    private String description;
    private ZonedDateTime endTime;
    private String referenceId;
    private String sequenceId;
    private String text01;
    private String text02;
    private String text03;
    private String text04;
    private String text05;
    private String text06;
    private Long number01;
    private Long number02;
    private Long number03;
    private Long number04;
    private Long number05;
    private Boolean boolean01;
    private Boolean boolean02;
    private Boolean boolean03;
    private Boolean boolean04;
    private Boolean boolean05;
    private ZonedDateTime date01;
    private ZonedDateTime date02;
    private ZonedDateTime date03;
    private ZonedDateTime date04;
    private ZonedDateTime date05;
}
