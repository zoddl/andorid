package com.zoddl.model.commommodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by garuv on 2/5/18.
 */

public class TagImageDetailsModel implements Parcelable {


    @SerializedName("Prime_Name")private String galleryimageprimename;
    @SerializedName("Id")private String galleryiamgeid;
    @SerializedName("Customer_Id")private String galleryimagecustomerid;
    @SerializedName("isUploaded")private String galleryiamgeisUploaded;
    @SerializedName("Primary_Tag")private String galleryimageprimarytag;
    @SerializedName("Doc_Url")private String galleryimagedocurl;
    @SerializedName("Amount")private String galleryimageamount;
    @SerializedName("Image_Url")private String galleryimageimageurl;
    @SerializedName("Image_Url_Thumb")private String galleryimageimageurlthumb;
    @SerializedName("Description")private String galleryimagedescription;
    @SerializedName("Tag_Type")private String galleryimagetag;
    @SerializedName("Tag_Send_Date")private String galleryimagetagsenddate;
    @SerializedName("Tag_Status")private String galleryimagetagstatus;
    @SerializedName("Sub_Description")private String galleryimagesubdescritpion;



    //constructor
    protected TagImageDetailsModel(Parcel in) {
        galleryimageprimename = in.readString();
        galleryiamgeid = in.readString();
        galleryimagecustomerid = in.readString();
        galleryiamgeisUploaded = in.readString();
        galleryimageprimarytag = in.readString();
        galleryimagedocurl = in.readString();
        galleryimageamount = in.readString();
        galleryimageimageurl = in.readString();
        galleryimageimageurlthumb = in.readString();
        galleryimagedescription = in.readString();
        galleryimagetag = in.readString();
        galleryimagetagsenddate = in.readString();
        galleryimagetagstatus = in.readString();
        galleryimagesubdescritpion = in.readString();


    }





    public static final Creator<TagImageDetailsModel> CREATOR = new Creator<TagImageDetailsModel>() {
        @Override
        public TagImageDetailsModel createFromParcel(Parcel in) {
            return new TagImageDetailsModel(in);
        }

        @Override
        public TagImageDetailsModel[] newArray(int size) {
            return new TagImageDetailsModel[size];
        }
    };


    //size
    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(galleryimageprimename);
        parcel.writeString(galleryiamgeid);
        parcel.writeString(galleryimagecustomerid);
        parcel.writeString(galleryiamgeisUploaded);
        parcel.writeString(galleryimageprimarytag);
        parcel.writeString(galleryimagedocurl);
        parcel.writeString(galleryimageamount);
        parcel.writeString(galleryimageimageurl);
        parcel.writeString(galleryimageimageurlthumb);
        parcel.writeString(galleryimagedescription);
        parcel.writeString(galleryimagetag);
        parcel.writeString(galleryimagetagsenddate);
        parcel.writeString(galleryimagetagstatus);
        parcel.writeString(galleryimagesubdescritpion);

    }

    public String getGalleryimageprimename() {
        return galleryimageprimename;
    }


    public String getGalleryiamgeid() {
        return galleryiamgeid;
    }


    public String getGalleryimagecustomerid() {
        return galleryimagecustomerid;
    }


    public String getGalleryiamgeisUploaded() {
        return galleryiamgeisUploaded;
    }


    public String getGalleryimageprimarytag() {
        return galleryimageprimarytag;
    }


    public String getGalleryimagedocurl() {
        return galleryimagedocurl;
    }


    public String getGalleryimageamount() {
        return galleryimageamount;
    }


    public String getGalleryimageimageurl() {
        return galleryimageimageurl;
    }


    public String getGalleryimageimageurlthumb() {
        return galleryimageimageurlthumb;
    }


    public String getGalleryimagedescription() {
        return galleryimagedescription;
    }


    public String getGalleryimagetag() {
        return galleryimagetag;
    }


    public String getGalleryimagetagsenddate() {
        return galleryimagetagsenddate;
    }


    public String getGalleryimagetagstatus() {
        return galleryimagetagstatus;
    }


    public String getGalleryimagesubdescritpion() {
        return galleryimagesubdescritpion;
    }


}
