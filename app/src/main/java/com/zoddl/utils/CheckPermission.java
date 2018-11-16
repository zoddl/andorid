package com.zoddl.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 *@author abhishek.tiwari  on 30/5/16.
 */
public class CheckPermission {

    public static final int RC_LOCATION_PERMISSION = 111;
    public static final int RC_CAMERA_PERMISSION = 222;
    public static final int RC_VIDEO_CAMERA_PERMISSION = 333;
    public static final int RC_CONTACT_PERMISSION = 444;
    public static final int RC_SMS_PERMISSION = 555;
    public static final int RC_STORAGE_PERMISSION = 555;

    /**
     * check is marshmallow or greater version
     * @return {true} if marshmallow or greater version else {false}
     */
    public static boolean checkIsMarshMallowVersion() {
        int sdkVersion = Build.VERSION.SDK_INT;
        return sdkVersion >= Build.VERSION_CODES.M;
    }

    /**
     * check Storage permission
     *
     * @param context current application context
     * @return {true} if Storage permission granted else {false}
     */
    public static boolean checkStoragePermission(Context context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * request Storage permission
     *
     * @param activity              current application context
     * @param RC_STORAGE_PERMISSION request code for Storage permission
     */
    public static void requestStoragePermission(Activity activity, int RC_STORAGE_PERMISSION) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_LOCATION_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_LOCATION_PERMISSION);
        }
    }

    /**
     * check location permission
     * @param context current application context
     * @return {true} if location permission granted else {false}
     */
    public static boolean checkLocationPermission(Context context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * request location permission
     * @param activity current application context
     * @param RC_LOCATION_PERMISSION request code for location permission
     */
    public static void requestLocationPermission(Activity activity, int RC_LOCATION_PERMISSION) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, RC_LOCATION_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, RC_LOCATION_PERMISSION);
        }
    }

    /**
     * check camera permission
     * @param context current application context
     * @return {true} if camera permission granted else {false}
     */
    public static boolean checkCameraPermission(Context context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * request camera permission
     * @param activity current application context
     * @param RC_CAMERA_PERMISSION request code for camera permission
     */
    public static void requestCameraPermission(Activity activity, int RC_CAMERA_PERMISSION) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, RC_CAMERA_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, RC_CAMERA_PERMISSION);

        }
    }

    /**
     * check video camera permission
     * @param context current application context
     * @return {true} if video camera permission granted else {false}
     */
    public static boolean checkVideoCameraPermission(Context context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * request video camera permission
     * @param activity current application context
     * @param RC_VIDEO_CAMERA_PERMISSION request code for video camera permission
     */
    public static void requestVideoCameraPermission(Activity activity, int RC_VIDEO_CAMERA_PERMISSION) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, RC_VIDEO_CAMERA_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, RC_VIDEO_CAMERA_PERMISSION);

        }
    }

    /**
     * check contacts permission
     * @param context current application context
     * @return {true} if contact permission granted else {false}
     */
    public static boolean checkContactsPermission(Context context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * request contacts permission
     * @param activity current application context
     * @param RC_CONTACT_PERMISSION request code for contacts permission
     */
    public static void requestContactsPermission(Activity activity, int RC_CONTACT_PERMISSION) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, RC_CONTACT_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, RC_CONTACT_PERMISSION);
        }
    }

    /**
     * check SMS permission
     * @param context current application context
     * @return {true} if sms permission granted else {false}
     */
    public static boolean checkSMSPermission(Context context) {
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS);
        int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS);
        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * request SMS permission
     * @param activity current application context
     * @param RC_SMS_PERMISSION request code for SMS permission
     */
    public static void requestSMSPermission(Activity activity, int RC_SMS_PERMISSION) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_SMS)||
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECEIVE_SMS)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, RC_SMS_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, RC_SMS_PERMISSION);
        }
    }
}