package com.zoddl.activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.zoddl.R;
import com.zoddl.databinding.ActivityOtpBinding;
import com.zoddl.utils.PrefManager;

public class OtpActivity extends BaseActivity {

    private Context context;
    private ActivityOtpBinding binding;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);
        context = OtpActivity.this;
        binding.headerToolbar.tvHeader.setText("OTP");
        prefManager=PrefManager.getInstance(context);
        setListeners();
    }

    @Override
    public void setListeners() {
        binding.headerToolbar.ivLeft.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(binding.pinEntrySimple.getText().toString())) {
                    showToast("Please enter otp.");
                } else if (TextUtils.getTrimmedLength(binding.pinEntrySimple.getText().toString()) < 4) {
                    showToast("Please enter valid otp.");
                } else {
                   // apiOtpVerification();
                }
                break;
        }
    }


    @Override
    public String getTagName() {
        return OtpActivity.class.getSimpleName();
    }
}
