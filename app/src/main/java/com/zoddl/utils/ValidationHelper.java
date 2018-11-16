package com.zoddl.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by Abhishek Tiwari on 22/5/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */
public class ValidationHelper {

    public static boolean emptyFieldValidation(String fieldValue){
        return TextUtils.isEmpty(fieldValue);
    }

    public static boolean emailFieldValidation(String fieldValue){
        return !Patterns.EMAIL_ADDRESS.matcher(fieldValue).matches();
    }

    public static boolean lengthFieldValidation(String fieldValue, int length){
        return fieldValue.length()<length;
    }

    public static boolean confirmPasswordValidation(String fieldValue1, String fieldValue2){
        return !TextUtils.equals(fieldValue1, fieldValue2);
    }

    public static String checkNull(String fieldValue){
        if (TextUtils.isEmpty(fieldValue)){
            return "";
        }
        return fieldValue;
    }

    @NonNull
    public static String getRating(String value) {
        value = checkNull(value);
        switch (value) {
            case "1":
                return "*";
            case "2":
                return "**";
            case "3":
                return "***";
            case "4":
                return "****";
            case "5":
                return "*****";
            default:
                return "";
        }
    }

}
