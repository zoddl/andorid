package com.zoddl.model.homeReport;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by avanish on 7/6/18.
 */

public class ImageReport {

    @SerializedName("Id")
    private String mId;
    @SerializedName("Timestamp")
    private String TimeStamp;
    @SerializedName("reportjson")
    private List<Reportjson> reportJson;

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String mTimeStamp) {
        TimeStamp = mTimeStamp;
    }

    public List<Reportjson> getReportJson() {
        return reportJson;
    }

    public void setReportJson(List<Reportjson> mReportJson) {
        reportJson = mReportJson;
    }
}
