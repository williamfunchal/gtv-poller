package com.consensus.gtv.poller.models.rawdata;

import lombok.Data;

@Data
public class IspServiceData {

    private String serviceKey;
    private String serviceId;
    private String customerKey;
    private String serviceType;
    private String accountFlag;
    private String status;
    private String startDate;
    private String endDate;
    private String productType;
    private String resourceType;
    private String createDateTime;
    private String resellerId;
    private String freeTrialFlag;
    private String freeTrialEndDate;
}
