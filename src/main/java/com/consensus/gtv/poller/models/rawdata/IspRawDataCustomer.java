package com.consensus.gtv.poller.models.rawdata;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonTypeName("isp-customer-data")
public class IspRawDataCustomer extends IspRawData{
    private IspCustomerData data;
}
