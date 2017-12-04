package com.demo.si.file.process.transformer;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.validation.DataBinder;

import com.demo.si.file.process.constants.Constants;
import com.demo.si.file.process.model.FileProcessorStatistics;

/**
 * @author Karthik Palanivelu.
 */
@Component
public class StatisticsTransformer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${baseDir}")
    private String baseDir;

    @Value("${filePrefix : }")
    private String filePrefix;

    @Inject
    private ConcurrentMap<String, Object> fileMetrics;

    public Message populateFromHeader(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        fileMetrics.put(Constants.FILE_NAME, getValue(String.valueOf(headers.get(Constants.FILE_NAME))));
        fileMetrics.put(Constants.BASE_DIR, getValue(String.valueOf(headers.get(Constants.BASE_DIR))));
        fileMetrics.put(Constants.BUCKET_NAME, getValue(String.valueOf(headers.get(Constants.BUCKET_NAME))));
        fileMetrics.put(Constants.BUCKET_PREFIX, getValue(String.valueOf(headers.get(Constants.BUCKET_PREFIX))));
        fileMetrics.put(Constants.S3_KEY, getValue(String.valueOf(headers.get(Constants.S3_KEY))));
        return message;
    }

    private String getValue(String value) {
        String returnValue = "";
        if (StringUtils.isNotBlank(value)) {
            returnValue = value;
        }
        return returnValue;
    }

    private String statisticsText() {
        String statsText = "Following are the statistics for the file - " + String.valueOf(fileMetrics.get(Constants.FILE_NAME))
                + " acquired: from Bucket Name " + fileMetrics.get(Constants.BUCKET_NAME) + " processed today " + FastDateFormat
                .getInstance("yyyyMMdd").format(new Date()) + ":";
        MutablePropertyValues mpv = new MutablePropertyValues(fileMetrics);
        FileProcessorStatistics fileStats = new FileProcessorStatistics();
        DataBinder db = new DataBinder(fileStats);
        db.bind(mpv);
        return statsText + fileStats.toString();
    }

    public void saveStatistics() {
        String[] processingFilePrefix = StringUtils.split(String.valueOf(fileMetrics.get(Constants.FILE_NAME)), ".");
        String statsFileDate = FastDateFormat.getInstance("yyyyMMdd").format(new java.util.Date());
        String filename = StringUtils.join(processingFilePrefix[0], "-STATS-", statsFileDate, ".txt");
        fileMetrics.put(Constants.STATS_FILE_NAME, filename);
        fileMetrics.put(Constants.STATS_FILE_DATE, statsFileDate);
        String fullFilePath = null;
        if (StringUtils.isBlank(filePrefix)) {
            fullFilePath = StringUtils.join(baseDir, "/stats/", filename);
        } else {
            fullFilePath = StringUtils.join(baseDir, "/", filePrefix, "/stats/", filename);
        }
        File f = new File(fullFilePath);
        try {
            FileUtils.writeStringToFile(f, statisticsText(), "UTF-8");
        } catch (IOException e) {
            log.error("Error in writing Statistics File --- {} -- {}", filename, e);
        }
    }

}
