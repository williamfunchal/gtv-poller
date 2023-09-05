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

    public String getCloudDbTime() {
        return jdbcTemplate.queryForObject("SELECT CURRENT_DATE FROM dual", String.class);
    }

    public List<IspUsageDTO> getUsageEvents(ZonedDateTime startTimestamp, ZonedDateTime endTimestamp, int limit) {
        // TODO write query for usage events
        return Collections.emptyList();
    }

}
