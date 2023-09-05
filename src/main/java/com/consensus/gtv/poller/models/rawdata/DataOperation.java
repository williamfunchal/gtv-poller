package com.consensus.gtv.poller.models.rawdata;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public enum DataOperation {

    CREATE("I"),
    UPDATE("U"),
    DELETE("D");

    private static final Map<String, DataOperation> CONSTANTS = new HashMap<>();

    static {
        for (DataOperation c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private final String value;

    DataOperation(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public static DataOperation fromValue(String value) {
        return Optional.ofNullable(CONSTANTS.get(value))
                .orElseThrow(() -> new IllegalArgumentException("Unsupported operation exception: " + value));
    }
}
