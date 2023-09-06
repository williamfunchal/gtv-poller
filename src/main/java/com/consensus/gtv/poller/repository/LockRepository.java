package com.consensus.gtv.poller.repository;

import com.amazonaws.services.dynamodbv2.AcquireLockOptions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBLockClient;
import com.amazonaws.services.dynamodbv2.LockItem;
import com.amazonaws.services.dynamodbv2.ReleaseLockOptions;
import com.consensus.gtv.poller.models.exception.LockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LockRepository {

    private final AmazonDynamoDBLockClient lockClient;

    public LockItem acquireLockById(String lockId) {
        try {
            AcquireLockOptions lockOptions = AcquireLockOptions
                    .builder(lockId)
                    .withDeleteLockOnRelease(false)
                    .withReplaceData(false)
                    .build();
            return lockClient.tryAcquireLock(lockOptions).orElse(null);
        } catch (InterruptedException ex) {
            LOG.error("Failed to acquire lock: {}", ex.getMessage(), ex);
            Thread.currentThread().interrupt();
            throw new LockException(ex.getMessage(), ex);
        } catch (Exception ex) {
            LOG.error("Failed to acquire lock: {}", ex.getMessage(), ex);
            throw new LockException(ex.getMessage(), ex);
        }
    }

    public void releaseLock(LockItem lockItem, ByteBuffer lockData) {
        lockClient.releaseLock(ReleaseLockOptions
                .builder(lockItem)
                .withData(lockData)
                .withDeleteLock(false)
                .build()
        );
    }
}
