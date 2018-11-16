package com.zoddl.utils;

/**
 * Created by Abhishek Tiwari on 4/7/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */
public class AppConstant {

    public static final String DATABASE_NAME = "_database_zoddl_db";

    public static final int FRAGMENT_CONTACT_US = 0;
    public static final int FRAGMENT_MY_SETTINGS = 1;
    public static final int FRAGMENT_MY_PROFILE = 2;
    public static final int FRAGMENT_ALERT_NOTIFICATION = 3;
    public static final int FRAGMENT_MAKE_PAYMENT = 4;
    public static final int FRAGMENT_HOW_ITS_WORK = 5;
    public static final int FRAGMENT_FAQ = 6;
    public static final int FRAGMENT_LOGOUT = 7;
    //public static final int FRAGMENT_PLAN_SUBSCRIPTION = 8;

    public static final int FRAGMENT_HOME = 9;
    public static final int FRAGMENT_GALLERY = 10;
    public static final int FRAGMENT_CAMERA = 11;
    public static final int FRAGMENT_WEB = 12;
    public static final int FRAGMENT_DOC = 13;
    public static final int FRAGMENT_POST = 14;
    public static final int FRAGMENT_POST_DETAIL = 15;
    public static final int FRAGMENT_POST_SORT = 16;
    public static final int FRAGMENT_GALLERY_Click = 17;

    public static final int FRAGMENT_HOME_SEEALL_POST_DETAILS = 21;
    public static final int FRAGMENT_HOME_SEEALL_POST_DETAILS_BY_MONTH = 22;
    public static final int FRAGMENT_HOME_SEEALL_POST_DETAILS_BY_MONTH_DETAILS = 23;

    public static final int FRAGMENT_POST_PRIMARY_SECONDARY = 24;

    public static final int HOME_TOLLBAR_FILLTER_NONE = 50;
    public static final int HOME_TOLLBAR_FILLTER_BY_AMOUNT = 51;
    public static final int HOME_TOLLBAR_FILLTER_BY_COUNT = 52;
    public static final int HOME_TOLLBAR_FILLTER_BY_USES = 53;
    public static final int HOME_TOLLBAR_FILLTER_BY_ALPHABET = 54;
    public static final int HOME_TOLLBAR_SEARCH_EVENT = 55;
    public static final int HOME_TOLLBAR_ADDIN_DB = 56;

    public static final int HOME_TOLLBAR_ADD_BUTTON = 57;
    public static final int HOME_TOLLBAR_GALLERY_BUTTON = 58;
    public static final int HOME_TOLLBAR_DOC_BUTTON = 59;

    public static final int HOME_TOLLBAR_REPORT_SHARE_BUTTON = 60;
    public static final int HOME_TOLLBAR_REPORT_REFRESH_BUTTON = 61;


    public static final String GALLERY_SUBMAIN_ADAPTER_EVENT = "GALLERY_SUBMAIN_ADAPTER_EVENT";


    public static final String HOME_BOTTOM_MENU_SELECTION_FROM = "HOME_BOTTOM_MENU_SELECTION_FROM";
    public static final String HOME_BOTTOM_MENU_SELECTION_FROM_GALLERY = "HOME_BOTTOM_MENU_SELECTION_FROM_GALLERY";
    public static final String HOME_BOTTOM_MENU_SELECTION_FROM_DOCUMENT = "HOME_BOTTOM_MENU_SELECTION_FROM_DOCUMENT";


    public static final String HOME_POST_SORT_DETAILS_DATA = "HOME_POST_SORT_DETAILS_DATA";

    public static final String HOME_POST_SORT_NEXT_NAV_DETAILS_ID = "HOME_POST_SORT_NEXT_NAV_DETAILS_ID";
    public static final String HOME_POST_SORT_NEXT_NAV_DETAILS_DATE = "HOME_POST_SORT_NEXT_NAV_DETAILS_DATE";


    public static final String HOME_SENDING_REPORT_AT_HOME = "HOME_SENDING_REPORT_AT_HOME";

    public static final String HOME_POST_SORT_SEEALL_DETAILS_ID = "HOME_POST_SORT_SEEALL_DETAILS_ID";
    public static final String HOME_POST_SORT_SEEALL_DETAILS_TYPE = "HOME_POST_SORT_SEEALL_DETAILS_TYPE";
    public static final String HOME_POST_SORT_SEEALL_DETAILS_SOURCE_TYPE = "HOME_POST_SORT_SEEALL_DETAILS_SOURCE_TYPE";

    public static final String HOME_POST_SORT_SEEALL_DETAILS_OBJECT = "HOME_POST_SORT_SEEALL_DETAILS_OBJECT";

    public static final String HOME_TAG_DETAILS_ID = "HOME_TAG_DETAILS_ID";
    public static final String HOME_TAG__DETAILS_FROM = "HOME_TAG__DETAILS_FROM";

    public static final String LOCAL_BROADCAST_FOR_GALARY_UPDATE_UI = "LOCAL_BROADCAST_FOR_GALARY_UPDATE_UI";

    public static final String LOCAL_BROADCAST_FOR_ALERT = "LOCAL_BROADCAST_FOR_ALERT";

    public static final String DEVICE_TYPE = "android";
    public static final String LOGIN_TYPE = "m";

    public static String getHeader(int pos){
        if (pos == FRAGMENT_GALLERY){
            return "Gallery";
        }else if (pos == FRAGMENT_DOC){
            return "Document";
        }else if (pos == FRAGMENT_HOME){
            return "Home";
        }
        return "Untagged";
    }
}
