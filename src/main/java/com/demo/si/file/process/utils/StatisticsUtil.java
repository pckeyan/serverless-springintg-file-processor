

package com.demo.si.file.process.utils;

import java.util.Date;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

/**
 * @author asv132 - Karthik Palanivelu on 10/13/17.
 */
@Component
public class StatisticsUtil {

    @Inject
    private ConcurrentMap<String, Object> fileMetrics;

    public void addCount(String key) {
        if (fileMetrics.get(key) != null) {
            fileMetrics.put(key, ((Integer) (fileMetrics.get(key)) + 1));
        } else {
            fileMetrics.put(key, Integer.valueOf(1));
        }
    }

    public void addTime(String key) {
        fileMetrics.put(key, new Date());
    }

}
