package com.zoddl.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zoddl.AppController;
import com.zoddl.R;
import com.zoddl.databinding.ActivityResetPasswordBinding;
import com.zoddl.helper.CustomRequest;
import com.zoddl.utils.UrlConstants;
import com.zoddl.utils.ValidationHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;


public class ForgotPasswordActivity extends BaseActivity {

    private ActivityResetPasswordBinding binding;
    private Context context;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);
        context = ForgotPasswordActivity.this;
        binding.headerToolbar.tvHeader.setText("Forgot Password");
        progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        setListeners();
    }

    @Override
    public void setListeners() {
        binding.btnSubmit.setOnClickListener(this);
        binding.headerToolbar.ivLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.btn_submit:
                if (checkValidation()) {
                    progressDialog.show();
                    apiForgotPassword();
                }
                break;
        }
    }

    /*
    api for forgot password
     */
    private void apiForgotPassword() {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("Email_Id", binding.etEmail.getText().toString());
        AppController.getInstance().addToRequestQueue(new CustomRequest(Request.Method.POST, UrlConstants.ZODDL_FORGET, params, forgetResponseListener, forgetResponseError), "tag_foget_req");
    }


    Response.Listener<JSONObject> forgetResponseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.d("response", response.toString());
            try {
                progressDialog.hide();

                if (response != null && response.getString("ResponseCode") != null) {
                    if (!TextUtils.isEmpty(response.getString("ResponseCode")) &&
                            TextUtils.equals(response.getString("ResponseCode"), UrlConstants.RESPONSE_OK)) {

                        final AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
                        alertDialog.setTitle(R.string.app_name);
                        alertDialog.setCancelable(false);

                        alertDialog.setMessage(response.getString("ResponseMessage"));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, ForgotPasswordActivity.this.getString(android.R.string.ok), new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(new Intent(context, LoginActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }
                        });
                        if (!((Activity) ForgotPasswordActivity.this).isFinishing()) {

                            alertDialog.show();
                        }

                    } else {
                        final AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
                        alertDialog.setTitle(R.string.app_name);
                        alertDialog.setCancelable(false);

                        alertDialog.setMessage(response.getString("ResponseMessage"));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, ForgotPasswordActivity.this.getString(android.R.string.ok), new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        if (!((Activity) ForgotPasswordActivity.this).isFinishing()) {

                            alertDialog.show();
                        }
                    }
                } else {
                    showAlertDialog(getString(R.string.server_error));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };
    Response.ErrorListener forgetResponseError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            progressDialog.hide();
            Log.d("response", error.toString());


        }
    };


    @Override
    public String getTagName() {
        return ForgotPasswordActivity.class.getSimpleName();
    }

    private boolean checkValidation() {
        binding.etEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_navy_blue_bottom));
        binding.tvEmailError.setVisibility(View.GONE);
        if (ValidationHelper.emptyFieldValidation(binding.etEmail.getText().toString())) {
            binding.etEmail.requestFocus();
            binding.tvEmailError.setVisibility(View.VISIBLE);
            binding.etEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.tvEmailError.setText("*Please enter email.");
            return false;
        } else if (ValidationHelper.emailFieldValidation(binding.etEmail.getText().toString())) {
            binding.tvEmailError.setVisibility(View.VISIBLE);
            binding.etEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.etEmail.requestFocus();
            binding.tvEmailError.setText("*Please enter valid email.");
            return false;
        } else {
            return true;
        }
    }

}
