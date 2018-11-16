package com.zoddl.model.commommodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by garuv on 2/5/18.
 */

public class PrimaryTagTypeModel implements Parcelable {

    @SerializedName("Prime_Name")private String primaryname;
    @SerializedName("Id")private String primaryid;


    //constructor
    protected PrimaryTagTypeModel(Parcel in) {
        primaryname = in.readString();
        primaryid = in.readString();
    }


    public PrimaryTagTypeModel(String mPrimaryname) {
        primaryname = mPrimaryname;
    }

    public static final Creator<PrimaryTagTypeModel> CREATOR = new Creator<PrimaryTagTypeModel>() {
        @Override
        public PrimaryTagTypeModel createFromParcel(Parcel in) {
            return new PrimaryTagTypeModel(in);
        }

        @Override
        public PrimaryTagTypeModel[] newArray(int size) {
            return new PrimaryTagTypeModel[size];
        }
    };

    public PrimaryTagTypeModel(String mS, String mI) {
        this.primaryname = mS;
        this.primaryid = mI;
    }


    //size
    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(primaryname);
        parcel.writeString(primaryid);

    }


    public String getprimaryName() {
        return primaryname;
    }


    public String getPrimaryid() {
        return primaryid;
    }



}
