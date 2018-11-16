
package com.zoddl.model.postdetials;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Payload implements Parcelable {

    @SerializedName("Count")
    private String mCount;
    @SerializedName("Date")
    private String mDate;
    @SerializedName("Images")
    private List<Image> mImages;
    @SerializedName("Month")
    private String mMonth;
    @SerializedName("Primary_Tag")
    private String mPrimaryTag;
    @SerializedName("Prime_Name")
    private String mPrimeName;
    @SerializedName("Total")
    private String mTotal;

    public Payload() {
    }

    public String getCount() {
        return mCount;
    }

    public void setCount(String Count) {
        mCount = Count;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String Date) {
        mDate = Date;
    }

    public List<Image> getImages() {
        return mImages;
    }

    public void setImages(List<Image> Images) {
        mImages = Images;
    }

    public String getMonth() {
        return mMonth;
    }

    public void setMonth(String Month) {
        mMonth = Month;
    }

    public String getPrimaryTag() {
        return mPrimaryTag;
    }

    public void setPrimaryTag(String PrimaryTag) {
        mPrimaryTag = PrimaryTag;
    }

    public String getPrimeName() {
        return mPrimeName;
    }

    public void setPrimeName(String PrimeName) {
        mPrimeName = PrimeName;
    }

    public String getTotal() {
        return mTotal;
    }

    public void setTotal(String Total) {
        mTotal = Total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
