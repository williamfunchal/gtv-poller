package com.consensus.gtv.poller.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CoreDbRepository {

    private final JdbcTemplate jdbcTemplate;

    public String getCloudDbTime() {
        return jdbcTemplate.queryForObject("SELECT CURRENT_DATE FROM dual", String.class);
    }

}
