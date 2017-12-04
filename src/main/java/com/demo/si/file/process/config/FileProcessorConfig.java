package com.demo.si.file.process.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.demo.si.file.process.constants.Constants;

/**
 * @author Karthik Palanivelu
 */
@Configuration
public class FileProcessorConfig {

    @Bean
    public ConcurrentMap<String, Object> fileMetrics() {
        ConcurrentMap<String, Object> fileMetrics = new ConcurrentHashMap<>();
        fileMetrics.put(Constants.TOTAL_RECORDS_RECEIVED, Integer.valueOf(0));
        fileMetrics.put(Constants.TOTAL_RECORDS_SPLIT, Integer.valueOf(0));
        fileMetrics.put(Constants.TOTAL_RECORDS_PROCESSED, Integer.valueOf(0));
        fileMetrics.put(Constants.ERROR_RECORDS, new ArrayList<List<String>>());
        fileMetrics.put(Constants.START_TIME, new Date());
        fileMetrics.put(Constants.END_TIME, new Date());
        return fileMetrics;
    }
}
