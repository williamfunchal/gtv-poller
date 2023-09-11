package com.consensus.gtv.poller.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.consensus.gtv.poller.config.properties.AwsS3Properties;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ReaderService<T> {

    public static final String S3_ARCHIVE_FOLDER = "_Archive";

    private final AmazonS3 s3Client;
    private final AwsS3Properties awsS3Properties;

    public List<T> readCsvFromS3(Class<T> type) {
        List<T> objects = new ArrayList<>();
        //createArchiveFolder();

        List<S3ObjectSummary> s3ObjectSummaries = readS3SortedObjects();
        for (S3ObjectSummary s3ObjectSummary : s3ObjectSummaries) {
            List<T> csvS3ObjectData = parseCsvFromS3Object(s3ObjectSummary, type);
            objects.addAll(csvS3ObjectData);
        }

        // Archive all files only when all data is parsed
        // TODO Need to make it more resilient and archive after data is sent to SQS
        s3ObjectSummaries.forEach(this::archiveS3Object);

        return objects;
    }

    // Read S3 bucket and return a list of objects sorted by name
    private List<S3ObjectSummary> readS3SortedObjects() {
        String bucketName = awsS3Properties.getBucketName();
        String prefix = awsS3Properties.getCustomerPrefix();
        LOG.debug("Reading S3 objects from: {}/{}", bucketName, prefix);
        return s3Client.listObjects(bucketName, prefix)
                .getObjectSummaries()
                .stream()
                .filter(s3Object -> s3Object.getKey().endsWith(".csv"))
                .sorted(Comparator.comparing(S3ObjectSummary::getKey))
                .collect(toList());
    }

    private void createArchiveFolder() {
        // verify if Archive folder exists. If not, create it
        try {
            String bucketName = awsS3Properties.getBucketName();
            String prefix = awsS3Properties.getCustomerPrefix();
            if (!s3Client.doesObjectExist(bucketName, prefix + S3_ARCHIVE_FOLDER)) {
                s3Client.putObject(bucketName, prefix + S3_ARCHIVE_FOLDER, "");
            }
        } catch (Exception ex) {
            LOG.error("Exception creating Archive folder: {}", ex.getMessage(), ex);
        }
    }

    // move S3 file read to Archive folder
    public void archiveS3Object(S3ObjectSummary s3ObjectSummary) {
        try {
            String key = s3ObjectSummary.getKey();
            s3Client.copyObject(awsS3Properties.getBucketName(), key, awsS3Properties.getBucketName(),
                    key.replace(awsS3Properties.getCustomerPrefix(), awsS3Properties.getCustomerPrefix() + S3_ARCHIVE_FOLDER));
            s3Client.deleteObject(awsS3Properties.getBucketName(), key);
        } catch (Exception ex) {
            LOG.error("Exception moving file to archive file: {}", ex.getMessage(), ex);
        }
    }

    public List<T> parseCsvFromS3Object(S3ObjectSummary s3ObjectSummary, Class<T> type) {
        String key = s3ObjectSummary.getKey();
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(awsS3Properties.getBucketName(), key));
        try (S3ObjectInputStream inputStream = s3Object.getObjectContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        } catch (Exception ex) {
            LOG.error("Exception mapping S3 CSV object to CsvToBean: {}", ex.getMessage(), ex);
            return emptyList();
        }
    }
}
