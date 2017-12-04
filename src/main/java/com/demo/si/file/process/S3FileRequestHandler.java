package com.demo.si.file.process;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectId;
import com.demo.si.file.process.constants.Constants;

/**
 * @author asv132 - Karthik Palanivelu on 10/4/17.
 */
public class S3FileRequestHandler implements RequestHandler<S3Event, String> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String bucketName;

    private String fileName;

    private String prefix;

    private String baseDir;

    private String filePrefix;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getPmtProcessorName() {
        return filePrefix;
    }

    public void setPmtProcessorName(String filePrefix) {
        this.filePrefix = filePrefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String handleRequest(S3Event input, Context context) {
        setBaseDir(System.getenv(Constants.BASE_DIR));
        setPmtProcessorName(System.getenv(Constants.FILE_PREFIX));

        AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
        String bucket = input.getRecords().get(0).getS3().getBucket().getName();
        setBucketName(bucket);
        String key = input.getRecords().get(0).getS3().getObject().getKey();
        String[] keys = StringUtils.split(key, "/");
        if(keys.length > 1) {
            setPrefix(keys[0]);
            setFileName(keys[1]);
        }else{
            setFileName(keys[0]);
        }

        log.info("Connected to S3 - {}", bucket);
        log.info("Retrieving File from S3 - {} / {}", bucket, key);

        File localFile = new File(StringUtils.join(getBaseDir(), "/", getPmtProcessorName(), "/", getFileName()));

        log.info("Retrieved the File - {}", localFile.getAbsolutePath());
        GetObjectRequest request = new GetObjectRequest(new S3ObjectId(bucket, key));
        s3Client.getObject(request, localFile);
        boolean success = false;
        do {
            success = localFile.exists() && localFile.canRead();
        } while (!success);

        if (success) {
            log.info("Retrieved File {} from S3 - {} / {}", getFileName(), bucket, key);
            Map<String, String> headers = new HashMap<>(3);
            headers.put(Constants.FILE_NAME, getFileName());
            headers.put(Constants.BUCKET_NAME, getBucketName());
            headers.put(Constants.BUCKET_PREFIX, getPrefix());
            headers.put(Constants.S3_KEY, key);
            Application application = new Application();
            application.processFileMessage(localFile.getAbsolutePath(), headers);
        }
        return "SUCCESS";
    }
}
