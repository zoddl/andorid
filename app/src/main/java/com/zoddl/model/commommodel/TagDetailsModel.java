package com.zoddl.model.commommodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by garuv on 2/5/18.
 */

public class TagDetailsModel implements Parcelable {

    @SerializedName("Prime_Name")private String galleryname;
    @SerializedName("Tag_Date_int")private String gallerytagdateint;
    @SerializedName("Primary_Tag")private String galleryprimarytag;
    @SerializedName("Total")private String gallerytotal;
    @SerializedName("Source_Type")private String gallerysourcetype;
    @SerializedName("Timestamp")private String gallerytimestamp;
    @SerializedName("Count")private String gallerycount;



    //constructor
    protected TagDetailsModel(Parcel in) {
        galleryname = in.readString();
        gallerytagdateint = in.readString();
        galleryprimarytag = in.readString();
        gallerytotal = in.readString();
        gallerysourcetype = in.readString();
        gallerytimestamp = in.readString();
        gallerycount = in.readString();
    }





    public static final Creator<TagDetailsModel> CREATOR = new Creator<TagDetailsModel>() {
        @Override
        public TagDetailsModel createFromParcel(Parcel in) {
            return new TagDetailsModel(in);
        }

        @Override
        public TagDetailsModel[] newArray(int size) {
            return new TagDetailsModel[size];
        }
    };


    //size
    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(galleryname);
        parcel.writeString(gallerytagdateint);
        parcel.writeString(galleryprimarytag);
        parcel.writeString(gallerytotal);
        parcel.writeString(gallerysourcetype);
        parcel.writeString(gallerytimestamp);
        parcel.writeString(gallerycount);

    }


    public String getgalleryName() {
        return galleryname;
    }


    public String getgallerydate() {
        return gallerytagdateint;
    }


    public String getgallerytag() {
        return galleryprimarytag;
    }

    public String getgallerytotal() {
        return gallerytotal;
    }

    public String getgallerytype() {
        return gallerysourcetype;
    }

    public String getgallerytamp() {
        return gallerytimestamp;
    }

    public String getgallerycount() {
        return gallerycount;
    }


}

