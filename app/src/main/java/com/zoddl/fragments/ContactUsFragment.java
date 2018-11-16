package com.zoddl.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zoddl.AppController;
import com.zoddl.R;
import com.zoddl.adapters.ContactUsAdapter;
import com.zoddl.databinding.FragmentContactUsBinding;
import com.zoddl.helper.CustomRequest;
import com.zoddl.model.ContactListModel;
import com.zoddl.utils.PrefManager;
import com.zoddl.utils.UrlConstants;
import com.zoddl.utils.ValidationHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ContactUsFragment extends BaseFragment {

    private Context context;
    private FragmentContactUsBinding binding;
    private ProgressDialog progressDialog;
    private PrefManager prefManager;

    private List<ContactListModel> conOldList = new ArrayList<>();
    private ContactUsAdapter contactAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_us, container, false);
        prefManager = PrefManager.getInstance(context);

        setListeners();
        contactAdapter = new ContactUsAdapter(context,conOldList);
        binding.rvPastMessages.setLayoutManager(new LinearLayoutManager(context));
        binding.rvPastMessages.setAdapter(contactAdapter);
        binding.rvPastMessages.setNestedScrollingEnabled(false);
        binding.rvPastMessages.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        apiContactDetails();
        return binding.getRoot();
    }

    @Override
    public void setListeners() {
        binding.btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                if (checkValidation()) {
                    progressDialog.show();
                    apiSendMessage();
                }
                break;
        }
    }

    private boolean checkValidation() {
        settingNoErrorUI();
        if (TextUtils.isEmpty(binding.etName.getText().toString())) {
            binding.etName.requestFocus();
            binding.etName.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.tvNameError.setText("*Please enter  your name.");
            binding.tvNameError.setVisibility(View.VISIBLE);
            return false;
        } else if (ValidationHelper.lengthFieldValidation(binding.etName.getText().toString(), 3)) {
            binding.etName.requestFocus();
            binding.etName.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.tvNameError.setText("*Name should contain atleast 3 characters.");
            binding.tvNameError.setVisibility(View.VISIBLE);
            return false;
        } else if (TextUtils.isEmpty(binding.etEmail.getText().toString())) {
            binding.etEmail.requestFocus();
            binding.etEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.tvEmailError.setText("*Please enter  your email id.");
            binding.tvEmailError.setVisibility(View.VISIBLE);
            return false;
        } else if (ValidationHelper.emailFieldValidation(binding.etEmail.getText().toString())) {
            binding.etEmail.requestFocus();
            binding.etEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.tvEmailError.setText("*Please enter valid email id.");
            binding.tvEmailError.setVisibility(View.VISIBLE);
            return false;
        } else if (TextUtils.isEmpty(binding.etMobileNumber.getText().toString())) {
            binding.etMobileNumber.requestFocus();
            binding.etMobileNumber.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.tvMobileNumberError.setText("*Please enter  your mobile number.");
            binding.tvMobileNumberError.setVisibility(View.VISIBLE);
            return false;
        } else if (ValidationHelper.lengthFieldValidation(binding.etMobileNumber.getText().toString(), 10)) {
            binding.etMobileNumber.requestFocus();
            binding.etMobileNumber.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.tvMobileNumberError.setText("*Mobile number should contain atleast 10 characters.");
            binding.tvMobileNumberError.setVisibility(View.VISIBLE);
            return false;
        } else if (TextUtils.isEmpty(binding.etMessage.getText().toString())) {
            binding.etMessage.requestFocus();
            binding.etMessage.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.tvMessageError.setText("*Please enter message.");
            binding.tvMessageError.setVisibility(View.VISIBLE);
            return false;
        } else {
            return true;
        }
    }

    private void settingNoErrorUI() {
        binding.etName.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_navy_blue_bottom));
        binding.etEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_navy_blue_bottom));
        binding.etMobileNumber.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_navy_blue_bottom));
        binding.etMessage.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_navy_blue_bottom));

        binding.tvNameError.setVisibility(View.GONE);
        binding.tvEmailError.setVisibility(View.GONE);
        binding.tvMobileNumberError.setVisibility(View.GONE);
        binding.tvMessageError.setVisibility(View.GONE);
    }

    @Override
    public String getTagName() {
        return ContactUsFragment.class.getSimpleName();
    }


    /**
     * apiContactUs
     *
     * @param
     */
    private void apiSendMessage() {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        params.put("Customer_Name", binding.etName.getText().toString());
        params.put("Email_Id", binding.etEmail.getText().toString());
        params.put("phone", binding.etMobileNumber.getText().toString());
        params.put("Message", binding.etMessage.getText().toString());

        AppController.getInstance().addToRequestQueue(new CustomRequest(Request.Method.POST, UrlConstants.ZODDL_CONTACT_SEND, params, SendMessageResponseListener, SendMessageResponseError), "tag_Send_Message_req");
    }


    Response.Listener<JSONObject> SendMessageResponseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.d("response", response.toString());
            try {
                progressDialog.hide();

                if (response != null && response.getString("ResponseCode") != null) {
                    if (!TextUtils.isEmpty(response.getString("ResponseCode")) &&
                            TextUtils.equals(response.getString("ResponseCode"), UrlConstants.RESPONSE_OK)) {
                        showAlertDialog("Contact Us!",response.getString("ResponseMessage"));

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
    Response.ErrorListener SendMessageResponseError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            progressDialog.hide();
            Log.d("response", error.toString());


        }
    };


    /**
     * apiContactDetails
     *
     * @param
     */
    private void apiContactDetails() {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());

        AppController.getInstance().addToRequestQueue(new CustomRequest(Request.Method.POST, UrlConstants.ZODDL_GET_CONTACT_DETAIL, params, ContactDetailResponseListener, ContactDetailResponseError), "tag_Contact_Detail_req");
    }

    Response.Listener<JSONObject> ContactDetailResponseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.d("response", response.toString());
            try {

                if (response != null && response.getString("ResponseCode") != null) {
                    if (!TextUtils.isEmpty(response.getString("ResponseCode")) &&
                            TextUtils.equals(response.getString("ResponseCode"), UrlConstants.RESPONSE_OK)) {


                        JSONObject mJsonObjectContact = null;

                        JSONArray jsonArrayPayload = response.getJSONArray("Payload");
                        if (jsonArrayPayload.length() != 0) {

                            JSONObject obj = jsonArrayPayload.getJSONObject(0);

                            binding.tvEmail.setText(obj.getString("Email_Id"));
                            binding.tvMobile.setText(obj.getString("phone"));
                            binding.tvWebsite.setText(obj.getString("Website"));

                            JSONArray arr = obj.getJSONArray("contacts");

                            for (int i = 0; i < arr.length(); i++) {
                                mJsonObjectContact = arr.getJSONObject(i);

                                ContactListModel cList = new ContactListModel(mJsonObjectContact.getString("Message"),
                                        mJsonObjectContact.getString("Date"));
                                conOldList.add(cList);
                            }
                            contactAdapter.notifyDataSetChanged();

                        } else {
                            /*fragment_buyer_tv_no_records.setVisibility(View.VISIBLE);*/
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

    Response.ErrorListener ContactDetailResponseError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("response", error.toString());


        }
    };

}
