package com.consensus.gtv.poller.models.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.time.Instant;

@Data
public class GtvRequestDetails {
    private Instant requestDateTime;
    private Instant responseDateTime;
    private Integer statusCode;
    private JsonNode payload;
}
