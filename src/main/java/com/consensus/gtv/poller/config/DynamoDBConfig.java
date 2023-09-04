package com.consensus.gtv.poller.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBLockClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBLockClientOptions;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.consensus.common.dynamo.db.CCSIDynamoHealthCheckContributor;
import com.consensus.gtv.poller.config.properties.PollerProperties;
import com.consensus.gtv.poller.config.properties.PollerProperties.LockProperties;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

@Configuration
public class DynamoDBConfig {

    private static final String LOCK_TABLE_NAME = "locks";
    private static final String LOCK_ID_FIELD = "lock_id";

    @Bean
    public DynamoDBMapper getDynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB);
    }

    @Bean
    public DynamoDB notifyDynamoDbClient(AmazonDynamoDB dynamoDbService) {
        return new DynamoDB(dynamoDbService);
    }

    @Bean
    public AmazonDynamoDBLockClient amazonDynamoDBLockClient(@Value("${ENVIRONMENT}") String env,
            @Value("${NAMESPACE}") String namespace, AmazonDynamoDB amazonDynamoDB, PollerProperties pollerProperties) {
        LockProperties lockProperties = pollerProperties.getLock();
        return new AmazonDynamoDBLockClient(
                AmazonDynamoDBLockClientOptions.builder(amazonDynamoDB, String.format("%s_%s_%s", env, namespace, LOCK_TABLE_NAME))
                        .withPartitionKeyName(LOCK_ID_FIELD)
                        .withTimeUnit(TimeUnit.SECONDS)
                        .withLeaseDuration(lockProperties.getLeaseDuration())
                        .withHeartbeatPeriod(lockProperties.getHeartbeatPeriod())
                        .withCreateHeartbeatBackgroundThread(true)
                        .build());
    }

    @Bean
    public DynamoDBHealthCheckContributor dynamoDBHealthCheckContributor(@Value("${ENVIRONMENT}") String env,
            @Value("${NAMESPACE}") String namespace) {
        return new DynamoDBHealthCheckContributor(env, namespace);
    }

    @Getter
    public static class DynamoDBHealthCheckContributor implements CCSIDynamoHealthCheckContributor {

        private static final List<String> BILLING_TABLES = List.of(
                "locks"
        );

        private final List<String> tableNames;

        public DynamoDBHealthCheckContributor(String env, String namespace) {
            this.tableNames = BILLING_TABLES.stream()
                    .map(tableName -> env + "_" + namespace + "_" + tableName)
                    .collect(toList());
        }
    }
}
