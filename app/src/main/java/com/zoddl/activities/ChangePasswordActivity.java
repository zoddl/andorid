package com.zoddl.activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.zoddl.R;
import com.zoddl.databinding.ActivityChangePasswordBinding;
import com.zoddl.utils.PrefManager;
import com.zoddl.utils.ValidationHelper;

public class ChangePasswordActivity extends BaseActivity {

    private ActivityChangePasswordBinding binding;
    private Context context;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        context = ChangePasswordActivity.this;
        binding.headerToolbar.tvHeader.setText("Change Password");
        prefManager=PrefManager.getInstance(context);
        setListeners();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.btn_submit:
                if (checkValidation()) {
                  //apiChangePassword();
                }
                break;
        }
    }

    @Override
    public String getTagName() {
        return ChangePasswordActivity.class.getSimpleName();
    }

    @Override
    public void setListeners() {
        binding.btnSubmit.setOnClickListener(this);
        binding.headerToolbar.ivLeft.setOnClickListener(this);
    }





    /*
    this method is used to check validation for field
     */
    private boolean checkValidation() {
        settingNoErrorUI();
        if (ValidationHelper.emptyFieldValidation(binding.etOldPass.getText().toString())) {
            binding.etOldPass.requestFocus();
            binding.etOldPass.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.tvOldPassError.setText("*Please enter old password.");
            binding.tvOldPassError.setVisibility(View.VISIBLE);
            return false;
        } else if (ValidationHelper.lengthFieldValidation(binding.etOldPass.getText().toString(), 6)) {
            binding.etOldPass.requestFocus();
            binding.etOldPass.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.tvOldPassError.setText("*Password should contain atleast 6 characters.");
            binding.tvOldPassError.setVisibility(View.VISIBLE);
            return false;
        } else if (ValidationHelper.emptyFieldValidation(binding.etNewPass.getText().toString())) {
            binding.etNewPass.requestFocus();
            binding.etNewPass.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.tvNewPassError.setText("*Please enter new password.");
            binding.tvNewPassError.setVisibility(View.VISIBLE);
            return false;
        } else if (ValidationHelper.lengthFieldValidation(binding.etNewPass.getText().toString(), 6)) {
            binding.etNewPass.requestFocus();
            binding.etNewPass.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.tvNewPassError.setText("*Password should contain atleast 6 characters.");
            binding.tvNewPassError.setVisibility(View.VISIBLE);
            return false;
        } else if (ValidationHelper.confirmPasswordValidation(binding.etNewPass.getText().toString(), binding.etConfirmPass.getText().toString())) {
            binding.etConfirmPass.requestFocus();
            binding.etConfirmPass.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.tvConfirmPassError.setText("*Passwords don\'t match.");
            binding.tvConfirmPassError.setVisibility(View.VISIBLE);
            return false;
        } else {
            return true;
        }
    }

    /*
    this method is used to set firld background without error
     */
    private void settingNoErrorUI() {
        binding.etOldPass.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_navy_blue_bottom));
        binding.etNewPass.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_navy_blue_bottom));
        binding.etConfirmPass.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_navy_blue_bottom));

        binding.tvOldPassError.setVisibility(View.GONE);
        binding.tvNewPassError.setVisibility(View.GONE);
        binding.tvConfirmPassError.setVisibility(View.GONE);
    }
}
