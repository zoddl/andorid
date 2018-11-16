package com.zoddl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zoddl.R;
import com.zoddl.fragments.EditPhotoFragment;

/**
 * Created by Hoang Anh Tuan on 12/14/2017.
 */

public class EditPhotoActivity extends BaseActivity {

    public static final String INPUT_URL = "inputUrl";
    public static final String IMAGE_PATH_LOCATION = "IMAGE_PATH";

    String inputUrl;

    /*public static void start(Context context, String inputUrl) {
        Intent starter = new Intent(context, EditPhotoActivity.class);
        starter.putExtra(INPUT_URL, inputUrl);
        context.startActivity(starter);
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_photo);

        if (getIntent()!=null){
            inputUrl = getIntent().getExtras().getString(INPUT_URL);
        }
        addFragment(EditPhotoFragment.create(getIntent().getStringExtra(INPUT_URL)));
    }

    private void addFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.rootMain, fragment);
        ft.commit();
    }

    public void addFragmentToStack(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.rootMain, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void setResultData(String imagePath){
        Intent intent = new Intent();
        intent.putExtra(IMAGE_PATH_LOCATION, imagePath);
        setResult(RESULT_OK, intent);
        finish();
    }
    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public String getTagName() {
        return null;
    }
}
