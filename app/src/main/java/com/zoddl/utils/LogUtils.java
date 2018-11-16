package com.zoddl.utils;

import android.util.Log;

/**
 * @author abhishek.tiwari on 7/9/15.
 */
public class LogUtils {
    public  static boolean DEBUG= true;

    public static void errorLog(String TAG, String message){
        if (DEBUG) {
            Log.e(TAG, message);
        }
    }

    public static void debugLog(String TAG, String message){
        if (DEBUG) {
            Log.d(TAG, message);
        }
    }

    public static void warnLog(String TAG, String message){
        if (DEBUG) {
            Log.w(TAG, message);
        }
    }

    public static void verboseLog(String TAG, String message){
        if (DEBUG) {
            Log.v(TAG, message);
        }
    }

    public static void infoLog(String TAG, String message){
        if (DEBUG) {
            Log.i(TAG, message);
        }
    }
}
