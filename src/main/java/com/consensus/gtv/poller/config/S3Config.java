package com.consensus.gtv.poller.config;

import com.amazonaws.services.s3.AmazonS3;
import com.consensus.gtv.poller.config.properties.AwsS3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Configuration
public class S3Config {

    @Bean
    public S3HealthIndicator s3HealthIndicator(AmazonS3 s3Client, AwsS3Properties awsS3Properties) {
        return new S3HealthIndicator(s3Client, awsS3Properties.getBucketName());
    }

    @Slf4j
    @RequiredArgsConstructor
    public static class S3HealthIndicator implements ReactiveHealthIndicator {

        @Value("${REGION}")
        private String region;

        private final AmazonS3 s3Client;
        private final String bucketName;

        @Override
        public Mono<Health> health() {
            return Mono.fromCallable(this::buildHealth)
                    .subscribeOn(Schedulers.boundedElastic());
        }

        private Health buildHealth() {
            Health.Builder healthBuilder = new Health.Builder()
                    .withDetail("region", region)
                    .withDetail("bucket_name", bucketName)
                    .up();

            try {
                s3Client.getBucketVersioningConfiguration(bucketName);
            } catch (Exception ex) {
                LOG.error("Exception during S3 health validation.", ex);
                healthBuilder = healthBuilder.down()
                        .withDetail("error", ex.getClass().getSimpleName() + ": " + ex.getMessage());
            }

            return healthBuilder.build();
        }
    }
}
