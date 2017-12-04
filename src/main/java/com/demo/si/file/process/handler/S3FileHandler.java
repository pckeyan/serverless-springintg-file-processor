package com.demo.si.file.process.handler;

import java.io.File;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.SSEAwsKeyManagementParams;
import com.demo.si.file.process.constants.Constants;

/**
 * @author Karthik Palanivelu
 */
@Component
public class S3FileHandler {

    private final transient Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${baseDir}")
    private String baseDir;

    @Value("${filePrefix : }")
    private String filePrefix;

    @Inject
    private ConcurrentMap<String, Object> fileMetrics;

    public void exportStatsFile() {
        String metricFileName = String.valueOf(fileMetrics.get(Constants.STATS_FILE_NAME));
        String fullPathMetricFile = null;
        if (StringUtils.isBlank(filePrefix)) {
            fullPathMetricFile = StringUtils.join(baseDir, "/stats/", metricFileName);
        } else {
            fullPathMetricFile = StringUtils.join(baseDir, "/", filePrefix, "/stats/", metricFileName);
        }
        writeToS3(metricFileName, fullPathMetricFile, "_results/stats");
    }

    private void writeToS3(String s3FileName, String fullPathWithFileName, String resultsPrefix) {
        AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
        File f = new File(fullPathWithFileName);
        if (f.exists()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(StringUtils
                    .join(fileMetrics.get(Constants.BUCKET_NAME), "/", fileMetrics.get(Constants.BUCKET_PREFIX), resultsPrefix),
                    s3FileName, f).withSSEAwsKeyManagementParams(new SSEAwsKeyManagementParams());
            log.info("FileName to export -->>>>>>>>>>>>>>>" + fullPathWithFileName);
            s3Client.putObject(putObjectRequest);
        } else {
            log.info("No File found to export - {} --->>>>>>>", fullPathWithFileName);
        }

    }
}
