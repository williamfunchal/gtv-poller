package com.consensus.gtv.poller.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.ZonedDateTime;

@Data
@With
@NoArgsConstructor
@AllArgsConstructor
public class DbLockData {

    private ZonedDateTime timestampPointer;
}
