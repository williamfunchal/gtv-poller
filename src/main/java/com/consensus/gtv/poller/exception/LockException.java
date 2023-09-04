package com.consensus.gtv.poller.exception;

public class LockException extends RuntimeException {

    public LockException(String msg) {
        super(msg, null, false, false);
    }

    public LockException(String msg, Throwable cause) {
        super(msg, cause, false, false);
    }
}
