package com.demo.si.file.process.constants;

/**
 * @author Karthik Palanivelu
 */
public interface Constants {

    String TOTAL_RECORDS_RECEIVED = "totalRecordsReceived";
    String TOTAL_RECORDS_SPLIT = "totalRecordsSplitted";
    String TOTAL_RECORDS_PROCESSED = "totalRecordsProcessed";
    String ERROR_RECORDS = "errorRecords";
    String START_TIME = "startTime";
    String END_TIME = "endTime";

    String TOTAL_RECORDS_RECEIVED_PRINT = "Total Records Received";
    String TOTAL_RECORDS_SPLIT_PRINT = "Total Records Split";
    String TOTAL_RECORDS_PROCESSED_PRINT = "Total Records Processed";
    String ERROR_RECORDS_PRINT = "Error Records";
    String START_TIME_PRINT = "Start Time";
    String END_TIME_PRINT = "End Time";
    String ELAPSED_TIME_PRINT = "Elapsed Time";

    // Global Header Properties
    String BASE_DIR = "baseDir";
    String FILE_PREFIX = "filePrefix";
    String PROCESSING_FILE_PATTERN = "processingFilePattern";
    String FILE_NAME = "fileName";
    String BUCKET_NAME = "bucketName";
    String BUCKET_PREFIX = "bucketPrefix";
    String S3_KEY = "s3Key";
    String STATS_FILE_NAME = "statsFileName";
    String STATS_FILE_DATE = "statsFileDate";
}
