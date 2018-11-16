package com.zoddl.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.zoddl.AppController;
import com.zoddl.model.UserDetail;
import com.google.gson.Gson;

/**
 * @author abhishek.tiwari on 7/9/15.
 */
@SuppressLint("ALL")
public class PrefManager {

    public static final String KEY_REMEMBER_ME = "remember_me";
    public static final String KEY_REMEMBER_ME_USERNAME = "remember_me_username";
    public static final String KEY_REMEMBER_ME_PASSWORD = "remember_me_password";
    public static final String KEY_USER_DETAILS = "user_details";
    public static final String KEY_NOTIFICATION_ID = "notification_id";
    public static final String KEY_USER_ID = "user_id";
    private static final String KEY_DEVICE_KEY = "device_key";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_DEVICE_TOKEN = "device_token";
    private static final String KEY_CUSTOMER_ID = "customer_id";
    private static final String KEY_AUTHTOKEN = "Authtoken";

    //private static final String KEY_OLD_TAG_LIST = "oldTagList";
    //private static final String KEY_OLD_SEC_TAG_LIST = "oldSecTagList";

    private static final String KEY_FREE_OR_PAID = "freeOrPaidUser";
    private static final String KEY_EDIT_ANY_IMAGES = "edit_any_images";

    private static final String KEY_IS_FIRST_TIME_AT_HOME = "KEY_IS_FIRST_TIME_AT_HOME";

    static final String NOTIFICATION_APP_ID = "NOTIFICATION_APP_ID";

    private static PrefManager instance;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    /**
     * Empty constructor
     */
    private PrefManager() {
    }

    /**
     * PrefManager constructor
     *
     * @param context current application context
     */
    private PrefManager(Context context) {
        super();
        this.preferences = context.getSharedPreferences(AppController.CAA_PREF, Context.MODE_PRIVATE);
        this.editor = this.preferences.edit();
    }

    /**
     * get single instance
     *
     * @param context current application context
     * @return instance of PrefManager
     */
    public static PrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new PrefManager(context);
        }
        return instance;
    }

    public void setKeyCustomerId(String value) {
        setStringValue(KEY_CUSTOMER_ID, value);
    }


    public String getKeyCustomerId() {
        return getStringValue(KEY_CUSTOMER_ID);
    }


    public void setKeyAuthtoken(String value) {
        setStringValue(KEY_AUTHTOKEN, value);
    }


    public String getKeyAuthtoken() {
        return getStringValue(KEY_AUTHTOKEN);
    }

    /**
     * @return Gets the value of notification id and returns notification id
     */
    public int getKeyNotificationId() {
        return getIntValue(KEY_NOTIFICATION_ID);
    }

    /**
     * Sets the notification id
     * You can use setKeyNotificationId() to get the value of notification id
     */
    public void setKeyNotificationId(int value) {
        setIntValue(KEY_NOTIFICATION_ID, value);
    }

    /**
     * @return Gets the value of device key and returns device key
     */
    public String getKeyUserId() {
        return getStringValue(KEY_USER_ID);
    }

    /**
     * Sets the device key
     * You can use setKeyDeviceKey() to get the value of device key
     */
    public void setKeyUserId(String value) {
        setStringValue(KEY_USER_ID, value);
    }

    /**
     * @return Gets the value of device key and returns device key
     */
    public String getKeyDeviceKey() {
        return getStringValue(KEY_DEVICE_KEY);
    }

    /**
     * Sets the device key
     * You can use setKeyDeviceKey() to get the value of device key
     */
    public void setKeyDeviceKey(String value) {
        setStringValue(KEY_DEVICE_KEY, value);
    }

    /**
     * @return Gets the value of device token and returns device token
     */
    public String getKeyDeviceToken() {
        return getStringValue(KEY_DEVICE_TOKEN);
    }

    /**
     * Sets the device token
     * You can use setKeyDeviceToken() to get the value of device token
     */
    public void setKeyDeviceToken(String value) {
        setStringValue(KEY_DEVICE_TOKEN, value);
    }

    /**
     * @return Gets the value of is logged in and returns is logged in
     */
    public boolean getKeyIsLoggedIn() {
        return getBooleanValue(KEY_IS_LOGGED_IN);
    }

    /**
     * Sets the is logged in
     * You can use setKeyIsLoggedIn() to get the value of is logged in
     */
    public void setKeyIsLoggedIn(boolean value) {
        setBooleanValue(KEY_IS_LOGGED_IN, value);
    }

    /**
     * getKeyRememberMeUsername
     *
     * @return KEY_REMEMBER_ME_USERNAME
     */
    public String getKeyRememberMeUsername() {
        return getStringValue(KEY_REMEMBER_ME_USERNAME);
    }

    /**
     * setKeyRememberMeUsername
     *
     * @param value KEY_REMEMBER_ME_USERNAME
     */
    public void setKeyRememberMeUsername(String value) {
        setStringValue(KEY_REMEMBER_ME_USERNAME, value);
    }

    /**
     * getKeyRememberMePassword
     *
     * @return KEY_REMEMBER_ME_PASSWORD
     */
    public String getKeyRememberMePassword() {
        return getStringValue(KEY_REMEMBER_ME_PASSWORD);
    }

    /**
     * setKeyRememberMePassword
     *
     * @param value KEY_REMEMBER_ME_PASSWORD
     */
    public void setKeyRememberMePassword(String value) {
        setStringValue(KEY_REMEMBER_ME_PASSWORD, value);
    }

    /**
     * getKeyRememberMe
     *
     * @return boolean value
     */
    public boolean getKeyRememberMe() {
        return getBooleanValue(KEY_REMEMBER_ME);
    }

    /**
     * setKeyRememberMe
     *
     * @param value boolean value
     */
    public void setKeyRememberMe(boolean value) {
        setBooleanValue(KEY_REMEMBER_ME, value);
    }

    /**
     * getKeyRememberMe
     *
     * @return boolean value
     */
    public boolean getKeyIsFirstTime() {
        return getBooleanValue(KEY_IS_FIRST_TIME_AT_HOME);
    }

    /**
     * setKeyRememberMe
     *
     * @param value boolean value
     */
    public void setKeyIsFirstTime(boolean value) {
        setBooleanValue(KEY_IS_FIRST_TIME_AT_HOME, value);
    }


    public UserDetail getUserDetail() {
        Gson gson = new Gson();
        String json = preferences.getString(KEY_USER_DETAILS, "");
        return gson.fromJson(json, UserDetail.class);
    }

    public void setUserDetail(UserDetail model) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        editor.putString(KEY_USER_DETAILS, json);
        editor.commit();
    }

    /*public Set<String> getKeyOldTagList() {
        return this.preferences.getStringSet(KEY_OLD_TAG_LIST,null);
    }

    public void setKeyOldTagList(Set<String> value){
        Set<String> set = new HashSet<>();
        set.addAll(value);
        editor.putStringSet(KEY_OLD_TAG_LIST, set);
        editor.commit();
    }


    public Set<String> getKeySecOldTagList() {
        return this.preferences.getStringSet(KEY_OLD_SEC_TAG_LIST,null);
    }

    public void setKeySecOldTagList(Set<String> value){
        Set<String> set = new HashSet<>();
        set.addAll(value);
        editor.putStringSet(KEY_OLD_SEC_TAG_LIST, set);
        editor.commit();
    }*/

    /**
     * Retrieving the value from the preference for the respective key.
     *
     * @param key : Key for which the value is to be retrieved
     * @return return value for the respective key as boolean.
     */
    private boolean getBooleanValue(String key) {
        return this.preferences.getBoolean(key, false);
    }

    /**
     * Saving the preference
     *
     * @param key   : Key of the preference.
     * @param value : Value of the preference.
     */
    private void setBooleanValue(String key, boolean value) {
        this.editor.putBoolean(key, value);
        this.editor.commit();
    }

    /**
     * Retrieving the value from the preference for the respective key.
     *
     * @param key : Key for which the value is to be retrieved
     * @return return value for the respective key as string.
     */
    private String getStringValue(String key) {
        return this.preferences.getString(key, "");
    }

    /**
     * Saving the preference
     *
     * @param key   : Key of the preference.
     * @param value : Value of the preference.
     */
    private void setStringValue(String key, String value) {
        this.editor.putString(key, value);
        this.editor.commit();
    }

    /**
     * Retrieving the value from the preference for the respective key.
     *
     * @param key : Key for which the value is to be retrieved
     * @return return value for the respective key as string.
     */
    private int getIntValue(String key) {
        return this.preferences.getInt(key, 0);
    }

    /**
     * Saving the preference
     *
     * @param key   : Key of the preference.
     * @param value : Value of the preference.
     */
    private void setIntValue(String key, int value) {
        this.editor.putInt(key, value);
        this.editor.commit();
    }

    /**
     * Retrieving the value from the preference for the respective key.
     *
     * @param key : Key for which the value is to be retrieved
     * @return return value for the respective key as string.
     */
    private long getLongValue(String key) {
        return this.preferences.getLong(key, 0);
    }

    /**
     * Saving the preference
     *
     * @param key   : Key of the preference.
     * @param value : Value of the preference.
     */
    private void setLongValue(String key, long value) {
        this.editor.putLong(key, value);
        this.editor.commit();
    }

    /**
     * Retrieving the value from the preference for the respective key.
     * @return return value for the respective key as string.
     */
    public String getFirebaseId() {
        return this.preferences.getString(NOTIFICATION_APP_ID, "");
    }

    /**
     * Saving the preference
     *
     * @param value : Value of the preference.
     */
    public void setFirebaseId(String value) {
        this.editor.putString(NOTIFICATION_APP_ID, value);
        this.editor.commit();
    }

    /**
     * Removes all the fields from SharedPrefs
     */
    public void clearPrefs() {
        this.editor.clear();
        this.editor.commit();
    }

    /**
     * Remove the preference for the particular key
     *
     * @param key : Key for which the preference to be cleared.
     */
    public void removeFromPreference(String key) {
        this.editor.remove(key);
        this.editor.commit();
    }

    public boolean getFreeOrPaid() {
        return this.preferences.getBoolean(KEY_FREE_OR_PAID,true);
    }

    public void setFreeOrPaid(boolean value) {
        this.editor.putBoolean(KEY_FREE_OR_PAID, value);
        this.editor.commit();
    }

    public boolean getEditAnyImage() {
        return this.preferences.getBoolean(KEY_EDIT_ANY_IMAGES,false);
    }

    public void setEditAnyImage(boolean value) {
        this.editor.putBoolean(KEY_EDIT_ANY_IMAGES, value);
        this.editor.commit();
    }

}
