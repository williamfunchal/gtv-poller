package com.consensus.gtv.poller.models;

import com.consensus.gtv.poller.models.request.GtvRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MappedData {
    private List<GtvRequest> requests;
}
