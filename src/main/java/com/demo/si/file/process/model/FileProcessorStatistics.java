package com.demo.si.file.process.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.FastDateFormat;

import com.demo.si.file.process.constants.Constants;
import com.demo.si.file.process.utils.CustomToStringStyle;

/**
 * @author Karthik Palanivelu.
 */
public class FileProcessorStatistics implements Serializable {

    private static final long serialVersionUID = -1467825767016652071L;

    private Integer totalRecordsReceived;

    private Integer totalRecordsSplitted;

    private Integer totalRecordsProcessed;

    private Integer totalRecordsSent;

    private List<String> errorRecords;

    private Date startTime;

    private Date endTime;

    public Integer getTotalRecordsReceived() {
        return totalRecordsReceived;
    }

    public void setTotalRecordsReceived(Integer totalRecordsReceived) {
        this.totalRecordsReceived = totalRecordsReceived;
    }

    public Integer getTotalRecordsSplitted() {
        return totalRecordsSplitted;
    }

    public void setTotalRecordsSplitted(Integer totalRecordsSplitted) {
        this.totalRecordsSplitted = totalRecordsSplitted;
    }

    public Integer getTotalRecordsSent() {
        return totalRecordsSent;
    }

    public void setTotalRecordsSent(Integer totalRecordsSent) {
        this.totalRecordsSent = totalRecordsSent;
    }

    public List<String> getErrorRecords() {
        return errorRecords;
    }

    public void setErrorRecords(List<String> errorRecords) {
        this.errorRecords = errorRecords;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    private Integer getTotalRecordsProcessed() {
        return totalRecordsProcessed;
    }

    public void setTotalRecordsProcessed(Integer totalRecordsProcessed) {
        this.totalRecordsProcessed = totalRecordsProcessed;
    }

    private String getElapsedTime() {
        double d = (double) ((new Date().getTime() - getStartTime().getTime()) / 1000);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return decimalFormat.format(d);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    @Override
    public boolean equals(final Object rhs) {
        return EqualsBuilder.reflectionEquals(this, rhs, false);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, CustomToStringStyle.NO_CLASS_NAME_MULTI_LINE_STYLE)
                .append(Constants.TOTAL_RECORDS_RECEIVED_PRINT, getTotalRecordsReceived())
                .append(Constants.TOTAL_RECORDS_SPLIT, getTotalRecordsSplitted())
                .append(Constants.TOTAL_RECORDS_PROCESSED_PRINT, getTotalRecordsProcessed())
                .append(Constants.ERROR_RECORDS_PRINT, getErrorRecords().toString())
                .append(Constants.START_TIME_PRINT, FastDateFormat.getInstance("MM-dd-yyyy HH:mm:ss").format(getStartTime()))
                .append(Constants.END_TIME_PRINT, FastDateFormat.getInstance("MM-dd-yyyy HH:mm:ss").format(new Date()))
                .append(Constants.ELAPSED_TIME_PRINT, getElapsedTime() + " Secs").toString();

    }
}
