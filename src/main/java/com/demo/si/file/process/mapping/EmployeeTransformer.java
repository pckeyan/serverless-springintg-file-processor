package com.demo.si.file.process.mapping;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.validation.DataBinder;

import com.demo.si.file.process.constants.Constants;
import com.demo.si.file.process.model.Employee;
import com.demo.si.file.process.utils.StatisticsUtil;

@Component
public class EmployeeTransformer {

    @Inject
    private StatisticsUtil statisticsUtil;
    @Value("${names}")
    private String namesStr;
    @Value("${columns}")
    private String columnsStr;

    public Employee transform(Message<String> item) {
        MutablePropertyValues mpv = new MutablePropertyValues(parseData(item.getPayload()));
        Employee emp = new Employee();
        DataBinder db = new DataBinder(emp);
        db.bind(mpv);
        statisticsUtil.addCount(Constants.TOTAL_RECORDS_PROCESSED);
        return emp;
    }

    public int[] convertStingToIntArray(String[] s) {
        return Stream.of(s).mapToInt(Integer::parseInt).toArray();
    }

    public Map<String, String> parseData(String item) {
        ConcurrentMap<String, String> parsedData = new ConcurrentHashMap<>();
        popuplateData(item, parsedData, org.springframework.util.StringUtils.commaDelimitedListToStringArray(namesStr),
                org.springframework.util.StringUtils.commaDelimitedListToStringArray(columnsStr));
        return parsedData;
    }

    private void popuplateData(String item, Map<String, String> parsedData, String[] names, String[] columns) {
        if (names != null && columns != null) {
            for (int i = 0; i < names.length; i++) {
                int[] indexes = convertStingToIntArray(StringUtils.split(columns[i], "-"));
                parsedData.put(names[i].trim(), StringUtils.substring(item, indexes[0], indexes[1]).trim());
            }
        }
    }
}
