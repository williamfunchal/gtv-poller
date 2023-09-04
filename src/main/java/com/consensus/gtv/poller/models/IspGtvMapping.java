package com.consensus.gtv.poller.models;

import com.consensus.gtv.poller.models.rawdata.IspRawData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IspGtvMapping {
    private IspRawData rawData;
    private MappedData mappedData;
}
