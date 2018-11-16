package com.zoddl;

import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Avanish.
 */

/*@ReportsCrashes(formUri = "",  mailTo = "avanish@apptology.in",
        customReportContent = { ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA,
                ReportField.STACK_TRACE, ReportField.LOGCAT },
        logcatArguments = { "-t", "200", "-v", "long","test:I" ,"*:D","*:S"},
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text,
        reportType= HttpSender.Type.JSON)*/
public class AppController extends MultiDexApplication {
    public static final String TAG = AppController.class.getSimpleName();
    private static Context context;
    private RequestQueue mRequestQueue;
    private int currentSection;
    private static AppController mInstance;
    public static final String CAA_PREF = "CAA_PREF";

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        /*final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);*/

        Fabric.with(this, new Crashlytics());
       // ACRA.init(this);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {

        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static Context getContext() {
        return context;
    }


    /**
     * @return Gets the value of currentSection and returns currentSection
     */
    public int getCurrentSection() {
        return currentSection;
    }

    /**
     * Sets the currentSection
     * You can use getCurrentSection() to get the value of currentSection
     */
    public void setCurrentSection(int currentSection) {
        this.currentSection = currentSection;
    }
}