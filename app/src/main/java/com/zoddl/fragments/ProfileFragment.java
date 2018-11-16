package com.zoddl.fragments;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.zoddl.AppController;
import com.zoddl.R;
import com.zoddl.activities.HomeActivity;
import com.zoddl.databinding.FragmentProfileBinding;
import com.zoddl.helper.CustomRequest;
import com.zoddl.utils.CommonUtils;
import com.zoddl.utils.DialogUtils;
import com.zoddl.utils.PrefManager;
import com.zoddl.utils.UrlConstants;
import com.zoddl.utils.ValidationHelper;
import com.zoddl.widgets.PinEntryView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 *
 */
public class ProfileFragment extends BaseFragment {

    private Context context;
    private FragmentProfileBinding binding;
    private PrefManager prefManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_profile, container, false);
        prefManager=PrefManager.getInstance(context);
        binding.toolbarHeader.cardHeader.setVisibility(View.GONE);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        apiProfile();
        //Log.e("profileFrag","onResume");
    }

    private void apiProfile() {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());

        AppController.getInstance().addToRequestQueue(new CustomRequest(Request.Method.POST, UrlConstants.ZODDL_GET_USER_PROFILE, params, SetSettingListener, SetSettingListenerError), "tag_Send_Message_req");

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

                        JSONArray mJSONArray = response.getJSONArray("Payload");
                        JSONObject mJSONObject = mJSONArray.getJSONObject(0);

                        ((HomeActivity) context).forProfileData(mJSONObject);
                        settingProfileData(mJSONObject);
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
           // Log.d("response", error.toString());


        }
    };


    /**
     * setting profile data
     */
    private void settingProfileData(JSONObject mJSONObject) {

        try {
            String urlString = mJSONObject.getString("profile_image_url");
            //Log.e("profUrl",urlString);

           /* Glide.with(context).
                    load(url)
                    .placeholder(R.drawable.image1)
                    .into(binding.ivProfilePic);*/


            Glide.with(this)
                    .load(urlString)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.app_placeholder)
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            //binding.postDetailsLibViewer.loadUrl(GOOGLE_URL_FOR_OPEN_DOC +mDetails.getImageUrl());

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(new SimpleTarget<Bitmap>(300,300) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            binding.ivProfilePic.setImageBitmap(resource);
                        }
                    });

            binding.tvName.setText(ValidationHelper.checkNull(mJSONObject.getString("first_name"))+ " " +ValidationHelper.checkNull(mJSONObject.getString("last_name")));

            binding.tvLocation.setText(ValidationHelper.checkNull(mJSONObject.getString("company_name"))+ " " +ValidationHelper.checkNull(mJSONObject.getString("city")));
            binding.tvSkypeId.setText(ValidationHelper.checkNull(mJSONObject.getString("skype_id")));
            binding.tvEmailId.setText(ValidationHelper.checkNull(mJSONObject.getString("Email_Id")));
            binding.tvMobileNo.setText(ValidationHelper.checkNull(mJSONObject.getString("phone")));
            binding.tvMobileNoAlt.setText(ValidationHelper.checkNull(mJSONObject.getString("alt_phone")));
            binding.tvPanNo.setText(ValidationHelper.checkNull(mJSONObject.getString("pan_number")));
            binding.tvAadharNo.setText(ValidationHelper.checkNull(mJSONObject.getString("aadhar_number")));
            binding.tvGenders.setText(ValidationHelper.checkNull(mJSONObject.getString("gender")));
            binding.tvDateOfBirth.setText(ValidationHelper.checkNull(mJSONObject.getString("dob")));
            binding.tvGstn1.setText(ValidationHelper.checkNull(mJSONObject.getString("gstn")));

           // Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        } catch (JSONException mE) {
            mE.printStackTrace();
        }

    }

    @Override
    public void setListeners() {
        binding.ivCheck.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_check:
                final Dialog dialog = DialogUtils.createDialog(context, R.layout.dialog_otp);
                final PinEntryView pinEntrySimple = (PinEntryView) dialog.findViewById(R.id.pin_entry_simple);
                TextView tvYes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tvNo = (TextView) dialog.findViewById(R.id.tv_no);

                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(pinEntrySimple.getText().toString())) {
                            CommonUtils.showToast(context, "Please enter otp.");
                        } else if (TextUtils.getTrimmedLength(pinEntrySimple.getText().toString()) < 4) {
                            CommonUtils.showToast(context, "Please enter valid otp.");
                        }else {
                            dialog.dismiss();
                        }
                    }
                });

                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    public String getTagName() {
        return ProfileFragment.class.getSimpleName();
    }
}
