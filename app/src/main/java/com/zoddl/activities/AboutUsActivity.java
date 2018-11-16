package com.zoddl.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.zoddl.R;
import com.zoddl.databinding.ActivityAboutUsBinding;

public class AboutUsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAboutUsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);
        binding.headerToolbar.tvHeader.setText("About Us");
        binding.headerToolbar.ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public String getTagName() {
        return AboutUsActivity.class.getSimpleName();
    }

}
