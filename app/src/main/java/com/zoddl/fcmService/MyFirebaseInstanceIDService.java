package com.zoddl.fcmService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zoddl.VolleyApiRequest;
import com.zoddl.utils.PrefManager;
import com.zoddl.utils.UrlConstants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author abhishek.tiwari on 2/1/17
 * for Mobiloitte
 */

@SuppressWarnings("ALL")
@SuppressLint("ALL")
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private PrefManager prefManager;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //EventBus.getDefault().post(new DeviceTokenEvent(refreshedToken));
        Log.e("fb_token","id->"+refreshedToken);
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        postFirebaseToken(getApplicationContext(),refreshedToken);

    }

    private void storeRegIdInPref(String token) {
            PrefManager.getInstance(getApplicationContext()).setFirebaseId(token);
    }

    //Primary Tag List
    public void postFirebaseToken(Context context,final String token) {
        prefManager = PrefManager.getInstance(context);

        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        Log.e("fb_token","success");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };

        Map<String, String> params = new HashMap<>();
        params.put("Authtoken",prefManager.getKeyAuthtoken());
        params.put("Device_Token",token);
        VolleyApiRequest request = new VolleyApiRequest(UrlConstants.ZODDL_FIREBASE_TOKEN, params, stringListener, errorListener);
        Volley.newRequestQueue(context).add(request);
    }
}
