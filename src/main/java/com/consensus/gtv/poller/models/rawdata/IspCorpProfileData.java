package com.consensus.gtv.poller.models.rawdata;

import lombok.Data;

@Data
public class IspCorpProfileData {

    private String resellerId;
    private String offerCode;
    private String userDidLimit;
    private String createDate;
    private String lastUpdateDate;
    private String status;
    private String secureType;
    private String paymentTerms;
}
