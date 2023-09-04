package com.consensus.gtv.poller.models.dto;

import com.consensus.gtv.poller.models.IspGtvMapping;
import lombok.Data;

import java.util.UUID;

@Data
public class IspGtvMappingDTO extends IspGtvMapping {
    private UUID correlationId;
}
