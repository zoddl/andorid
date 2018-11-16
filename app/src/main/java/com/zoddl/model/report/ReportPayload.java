
package com.zoddl.model.report;

import com.google.gson.annotations.SerializedName;

public class ReportPayload {

    @SerializedName("html")
    private String mHtml;

    public String getHtml() {
        return mHtml;
    }

    public void setHtml(String html) {
        mHtml = html;
    }

}
