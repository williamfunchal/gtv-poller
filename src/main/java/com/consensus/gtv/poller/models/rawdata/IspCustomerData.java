package com.consensus.gtv.poller.models.rawdata;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IspCustomerData {
    private String customerkey;
    private String company;
    private String country;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String mailRegion;
    private String mailCode;
    private String emailAddress;
    private String startDate;
    private String currencyCode;
    private String paymentTerms;
    private String resellerId;
    private String offerCode;
}
