package com.consensus.gtv.poller.repository;

import com.consensus.gtv.poller.models.dto.IspUsageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CoreDbRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<IspUsageDTO> getUsageInboxEvents(ZonedDateTime startTimestamp, ZonedDateTime endTimestamp, int limit) {
        // TODO write query for usage inbox events
        return Collections.emptyList();
    }

    public List<IspUsageDTO> getUsageOutboxEvents(ZonedDateTime startTimestamp, ZonedDateTime endTimestamp, int batchSize) {
        // TODO write query for usage outbox events
        return Collections.emptyList();
    }
}
