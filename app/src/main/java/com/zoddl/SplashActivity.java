package com.zoddl;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.zoddl.activities.CameraActivity;
import com.zoddl.activities.LoginActivity;
import com.zoddl.eventBusModels.DeviceTokenEvent;
import com.zoddl.utils.LogUtils;
import com.zoddl.utils.PrefManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {

    private boolean loggingStatus;
    private PrefManager prefManager;
    private Context context = SplashActivity.this;
    public static final int SPLASH_TIME_OUT = 3000;

    @Override
    public void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    @Override
    public void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /*final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);*/

        AWSMobileClient.getInstance().initialize(this).execute();

        context = SplashActivity.this;
        prefManager = PrefManager.getInstance(context);
        getDeviceDetails();
        printHashKey(SplashActivity.this);

        if (!TextUtils.isEmpty(prefManager.getKeyDeviceKey())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogUtils.infoLog(getTAG(), ":::getKeyDeviceToken:::" + prefManager.getKeyDeviceToken());
                    loggingStatus = prefManager.getKeyIsLoggedIn();
                    if (loggingStatus) {
                        CameraActivity.startCamera(context);

                        //startActivity(new Intent(context, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    } else {
                        startActivity(new Intent(context, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                }
            }, SPLASH_TIME_OUT);
        }

    }

    /**
     * getting device token and ids
     */
    private void getDeviceDetails() {
        if (TextUtils.isEmpty(prefManager.getKeyDeviceKey())) {
            String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            LogUtils.infoLog(getTAG(), ":::androidId:::" + androidId);
            if (!TextUtils.isEmpty(androidId)) {
                prefManager.setKeyDeviceKey(androidId);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeviceTokenEvent deviceTokenEvent) {
        if (deviceTokenEvent != null) {
            prefManager.setKeyDeviceToken(deviceTokenEvent.getDeviceToken());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogUtils.infoLog(getTAG(), ":::getKeyDeviceToken:::" + prefManager.getKeyDeviceToken());
                    LogUtils.infoLog(getTAG(), ":::getKeyDeviceKey:::" + prefManager.getKeyDeviceKey());
                    startActivity(new Intent(context, LoginActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }

    ;

    public String getTAG() {
        return SplashActivity.class.getSimpleName();
    }

    public  void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {

        } catch (Exception e) {

        }
    }
}
