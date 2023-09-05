package com.consensus.gtv.poller.models.mapper;

import com.consensus.gtv.poller.models.dto.IspUsageDTO;
import com.consensus.gtv.poller.models.sqs.IspNewUsageEvent;
import org.springframework.stereotype.Component;

@Component
public class IspUsageMapper {

    public IspNewUsageEvent toNewUsageEvent(IspUsageDTO ispUsageDTO) {
        return null;
    }
}
