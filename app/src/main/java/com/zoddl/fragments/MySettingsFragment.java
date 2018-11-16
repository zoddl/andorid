package com.zoddl.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.zoddl.AppController;
import com.zoddl.R;
import com.zoddl.activities.AboutUsActivity;
import com.zoddl.activities.HomeActivity;
import com.zoddl.databinding.FragmentMySettingsBinding;
import com.zoddl.helper.CustomRequest;
import com.zoddl.utils.PrefManager;
import com.zoddl.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Abhishek Tiwari on 6/7/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */
public class MySettingsFragment extends BaseFragment {

    private Context context;
    private FragmentMySettingsBinding binding;
    private PrefManager prefManager;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        prefManager = ((HomeActivity) context).getPrefManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_my_settings, container, false);

        setListeners();
        apiGetData();
        return binding.getRoot();
    }

    @Override
    public void setListeners() {
        binding.cbSettingGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.cbSettingGal.isChecked())
                    apiSaveSettings("g","0");
                else
                    apiSaveSettings("g","1");
            }
        });
        binding.cbSettingDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.cbSettingDoc.isChecked())
                    apiSaveSettings("d","0");
                else
                    apiSaveSettings("d","1");
            }
        });
        binding.cbSettingReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.cbSettingReport.isChecked())
                    apiSaveSettings("r","0");
                else
                    apiSaveSettings("r","1");
            }
        });
        binding.cbSettingFreePaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.cbSettingFreePaid.isChecked()){
                    apiSaveSettings("Paid_Status","0");
                    prefManager.setFreeOrPaid(true);
                }else{
                    apiSaveSettings("Paid_Status","1");
                    prefManager.setFreeOrPaid(false);
                }
            }
        });
        binding.tvSettingAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, AboutUsActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

    }

    @Override
    public void onClick(View view) {
    }

    private void apiSaveSettings(String mG, String mS) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        params.put("type", mG);
        params.put("value", mS);

        AppController.getInstance().addToRequestQueue(new CustomRequest(Request.Method.POST, UrlConstants.ZODDL_PERMISSION, params, SetSettingListener, SetSettingListenerError), "tag_Send_Message_req");

    }

    com.android.volley.Response.Listener<JSONObject> SetSettingListener = new com.android.volley.Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
           // Log.d("response", response.toString());
            try {
                if (response.getString("ResponseCode") != null) {
                    if (!TextUtils.isEmpty(response.getString("ResponseCode")) &&
                            TextUtils.equals(response.getString("ResponseCode"), UrlConstants.RESPONSE_OK)) {
                        Toast.makeText(context, response.getString("ResponseMessage"), Toast.LENGTH_SHORT).show();

                    } else {
                        showAlertDialog("Contact Us!",TextUtils.isEmpty(response.getString("ResponseMessage")) ?
                                getString(R.string.server_error) : response.getString("ResponseMessage"));
                    }
                } else {
                    showAlertDialog("Contact Us!",getString(R.string.server_error));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };
    com.android.volley.Response.ErrorListener SetSettingListenerError = new com.android.volley.Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            //Log.d("response", error.toString());


        }
    };



    private void apiGetData() {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());


        AppController.getInstance().addToRequestQueue(new CustomRequest(Request.Method.POST, UrlConstants.ZODDL_GET_PERMISSION, params, GetSettingListener, GetSettingListenerError), "tag_Send_Message_req");
    }


    com.android.volley.Response.Listener<JSONObject> GetSettingListener = new com.android.volley.Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
           // Log.d("response", response.toString());
            try {
                if (response != null && response.getString("ResponseCode") != null) {
                    if (!TextUtils.isEmpty(response.getString("ResponseCode")) &&
                            TextUtils.equals(response.getString("ResponseCode"), UrlConstants.RESPONSE_OK)) {

                        JSONArray mJSONArray = response.getJSONArray("Payload");
                        JSONObject mJSONObject = mJSONArray.getJSONObject(0);

                        if (mJSONObject.getString("g").equalsIgnoreCase("1")){
                            binding.cbSettingGal.setChecked(true);
                        }else{
                            binding.cbSettingGal.setChecked(false);
                        }

                        if (mJSONObject.getString("d").equalsIgnoreCase("1")){
                            binding.cbSettingDoc.setChecked(true);
                        }else{
                            binding.cbSettingDoc.setChecked(false);
                        }

                        if (mJSONObject.getString("r").equalsIgnoreCase("1")){
                            binding.cbSettingReport.setChecked(true);
                        }else{
                            binding.cbSettingReport.setChecked(false);
                        }

                        if (mJSONObject.getString("Paid_Status").equalsIgnoreCase("1")){
                            binding.cbSettingFreePaid.setChecked(true);
                        }else{
                            binding.cbSettingFreePaid.setChecked(false);
                        }


                    } else {
                        showAlertDialog("Contact Us!",TextUtils.isEmpty(response.getString("ResponseMessage")) ?
                                getString(R.string.server_error) : response.getString("ResponseMessage"));
                    }
                } else {
                    showAlertDialog("Contact Us!",getString(R.string.server_error));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };
    com.android.volley.Response.ErrorListener GetSettingListenerError = new com.android.volley.Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
           // Log.d("response", error.toString());


        }
    };


    @Override
    public String getTagName() {
        return MySettingsFragment.class.getSimpleName();
    }
}
