package com.consensus.gtv.poller.repository;

import com.amazonaws.services.dynamodbv2.AcquireLockOptions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBLockClient;
import com.amazonaws.services.dynamodbv2.LockItem;
import com.consensus.gtv.poller.exception.LockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PollerLockRepository {

    private final AmazonDynamoDBLockClient lockClient;

    public LockItem acquireLockById(String lockId) {
        try {
            return lockClient.acquireLock(AcquireLockOptions.builder(lockId).build());
        } catch (InterruptedException ex) {
            LOG.error("Failed to acquire lock: {}", ex.getMessage(), ex);
            Thread.currentThread().interrupt();
            throw new LockException(ex.getMessage(), ex);
        } catch (Exception ex) {
            LOG.error("Failed to acquire lock: {}", ex.getMessage(), ex);
            throw new LockException(ex.getMessage(), ex);
        }
    }
}
