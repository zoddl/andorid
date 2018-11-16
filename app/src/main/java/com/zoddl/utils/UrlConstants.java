package com.zoddl.utils;

/**
 * Created by Deepak Sikka .
 */

public class UrlConstants {

    public static final String GOOGLE_URL_FOR_OPEN_DOC = "https://docs.google.com/viewer?url=";
    public static final String ZODDL_PAYMENT_LINK = "https://www.payumoney.com/paybypayumoney/#/1CDDF7AD3FAEEF86C50C9693C6D87FB4";

    //staging
    //public static final String BASE_URL = "http://13.127.139.247/";

    //production
    public static final String BASE_URL = "http://www.zoddl.in/";

    public static final String RESPONSE_OK = "200";

    public static final String HEADER_KEY = "Content-Type";
    public static final String HEADER_VALUE = "application/json";


    public static final String S3_BUCKET_DEV_URL = "https://s3.amazonaws.com/zoddl-development/";


    public static final String ZODDL_LOGIN = BASE_URL + "Customer_Api/customerlogin";
    public static final String ZODDL_SIGNUP = BASE_URL + "Customer_Api/customersignup";
    public static final String ZODDL_LOGOUT = BASE_URL + "Customer_Api/logout";
    public static final String ZODDL_FORGET = BASE_URL + "Customer_Api/forget";
    public static final String ZODDL_CONTACT_SEND = BASE_URL + "Customer_Api/sendmessage";
    public static final String ZODDL_GET_CONTACT_DETAIL = BASE_URL + "Customer_Api/getcontactdetails";


    public static final String ZODDL_GET_PERMISSION = BASE_URL + "Customer_Api/getpermission";
    public static final String ZODDL_PERMISSION = BASE_URL + "Customer_Api/permission";
    public static final String ZODDL_GET_USER_PROFILE = BASE_URL + "Customer_Api/getUserProfile";
    public static final String ZODDL_EDIT_USER_PROFILE = BASE_URL + "Customer_Api/updateUserProfile";

    //************************************** Gallery api list **************************************
    public static final String ZODDL_POST_IMAGE_ADD_TAG = BASE_URL+"Customer_Api/addtag";

    public static final String ZODDL_PRIMARY_TAG = BASE_URL + "Customer_Api/primarytag";
    public static final String ZODDL_SECONDARY_TAG = BASE_URL + "Customer_Api/secondarytag";
    public static final String ZODDL_GALLERY_TAG = BASE_URL + "Customer_Api/gallerytag";

    public static final String ZODDL_GALLERY_TAG_BASED_ON_PRIMARY_TAG = BASE_URL + "Customer_Api/userprimarytagsearchlist";
    public static final String ZODDL_GALLERY_TAG_BASED_ON_SECONDARY_TAG = BASE_URL + "Customer_Api/usersecondarytagsearchlist";
    public static final String ZODDL_GALLERY_TAG_BASED_ON_PRIMARY_SECONDARY_TAG = BASE_URL + "Customer_Api/userprimarytagsearchdetails";

    public static final String ZODDL_GALLERY_USER_ITEM_DATA = BASE_URL+"Customer_Api/useritemtagdetails";
    public static final String ZODDL_GALLERY_USER_ITEM_DATA_EDIT = BASE_URL+"Customer_Api/useritemedittagdetails";
    public static final String ZODDL_GALLERY_USER_ITEM_DATA_DELETE = BASE_URL+"Customer_Api/deletetag";
    public static final String ZODDL_GALLERY_DETAIL_BASED_ON_PRIMARY_TAG = BASE_URL+"Customer_Api/gallerydetail";
    public static final String ZODDL_GALLERY_DETAILS_BASED_ON_PRIMARY_TAG_AND_MONTH = BASE_URL +"Customer_Api/gallerydetailbymonth";

   // public static final String ZODDL_GALLERY_TAG_SEARCH = BASE_URL + "Customer_Api/searchuserdata";
   public static final String ZODDL_GALLERY_TAG_SEARCH_GLOBAL = BASE_URL + "Customer_Api/globalsearch";

    //************************************** Document api list **************************************
    public static final String ZODDL_DOC_ADD_TAG = BASE_URL+"Document_Api/addtag";

    public static final String ZODDL_DOC_PRIMARY_TAG = BASE_URL+"Document_Api/primarytag";
    public static final String ZODDL_DOC_SECONDARY_TAG = BASE_URL+"Document_Api/secondarytag";
    public static final String ZODDL_DOC_GALLERY_TAG = BASE_URL+"Document_Api/gallerytag";

    public static final String ZODDL_DOC_TAG_BASED_ON_PRIMARY_TAG = BASE_URL + "Document_Api/userprimarytagsearchlist";
    public static final String ZODDL_DOC_TAG_BASED_ON_SECONDARY_TAG = BASE_URL + "Document_Api/usersecondarytagsearchlist";
    public static final String ZODDL_DOC_TAG_BASED_ON_PRIMARY_SECONDARY_TAG = BASE_URL + "Document_Api/userprimarytagsearchdetails";

    public static final String ZODDL_DOC_USER_ITEM_DATA = BASE_URL+"Document_Api/useritemtagdetails";
    public static final String ZODDL_DOC_USER_ITEM_DATA_EDIT = BASE_URL+"Document_Api/useritemedittagdetails";
    public static final String ZODDL_DOC_USER_ITEM_DATA_DELETE = BASE_URL+"Document_Api/deletetag";


     // Home Fargement
    public static final String ZODDL_HOME_FARGEMENT_DETAILS = BASE_URL + "Customer_Api/toplistprimetagdata";
    public static final String ZODDL_REPORT_HOME_GALLERY_DATA = BASE_URL + "Customer_Api/homedata";
    public static final String ZODDL_REPORT_HOME_DOC_DATA = BASE_URL + "Document_Api/homedata";


    //Details of TagWise
    public static final String ZODDL_SEE_ALL_DETAILS = BASE_URL + "Customer_Api/tagtypeseeall";
    public static final String ZODDL_SEE_ALL_DETAILS_BY_MONTH = BASE_URL + "Customer_Api/tagtypedetailbymonth";

    //Report Fragment
    public static final String ZODDL_REPORT_PRIMARY_TAG = BASE_URL + "Customer_Api/reportprimarytag";
    public static final String ZODDL_REPORT_SECONDARY_TAG = BASE_URL + "Customer_Api/reportsecondarytag";
    public static final String ZODDL_REPORT_PRIMARY_TAG_BASED_ID = BASE_URL + "Report_Api/reportdetail";
    public static final String ZODDL_REPORT_REPORT_DATA = BASE_URL + "Report_Api/report";

    public static final String ZODDL_DOC_DETAIL_BASED_ON_PRIMARY_TAG = BASE_URL+"Document_Api/gallerydetail";
    public static final String ZODDL_DOC_DETAILS_BASED_ON_PRIMARY_TAG_AND_MONTH = BASE_URL +"Document_Api/gallerydetailbymonth";

    //firebase token url
    public static final String ZODDL_FIREBASE_TOKEN = BASE_URL + "Customer_Api/updatefirebasetoken";

    //menu section
    public static final String ZODDL_MENU_HOW_IT_WORKS= "http://howitworks.zoddl.com";
    public static final String ZODDL_MENU_FAQ = "http://faq.zoddl.com";
}
