package com.zoddl.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
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
import com.zoddl.databinding.ActivityEditProfileBinding;
import com.zoddl.helper.CustomRequest;
import com.zoddl.utils.S3FileUploadHelper;
import com.zoddl.utils.CheckPermission;
import com.zoddl.utils.CommonUtils;
import com.zoddl.utils.DatePickerFragment;
import com.zoddl.utils.PrefManager;
import com.zoddl.utils.UrlConstants;
import com.zoddl.utils.ValidationHelper;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static com.zoddl.utils.UrlConstants.S3_BUCKET_DEV_URL;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityEditProfileBinding binding;
    //private Context context;
    private String gen = "Gender";
    private String[] gender = new String[]{"Gender", "Male", "Female"};
    private String imageName;
    private PrefManager prefManager;
    private boolean isFirstTime= true;
    JSONObject jsonObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        //context = EditProfileActivity.this;
        prefManager = PrefManager.getInstance(this);
        //editProfileRequest = new EditProfileRequest();

        settingData();
        setListeners();

        if (getIntent()!=null){
            try {
                String data = getIntent().getExtras().getString("ProfileData");
                jsonObj = new JSONObject(data);
                settingProfileData(jsonObj);
            } catch (JSONException mE) {
                mE.printStackTrace();
            }
        }

    }

    private void settingData() {
        binding.headerToolbar.tvHeader.setText("Edit Profile");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, gender);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        binding.spnGender.setAdapter(adapter);
        binding.spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gen = gender[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    /**
     * setting profile data
     */
    private void settingProfileData(JSONObject mJSONObject) {

        try {
            binding.etFirstName.setText(ValidationHelper.checkNull(mJSONObject.getString("first_name")));
            binding.etLastName.setText(ValidationHelper.checkNull(mJSONObject.getString("last_name")));
            binding.etEmail.setText(ValidationHelper.checkNull(mJSONObject.getString("Email_Id")));
            binding.etPhone.setText(ValidationHelper.checkNull(mJSONObject.getString("phone")));
            binding.etAltPhone.setText(ValidationHelper.checkNull(mJSONObject.getString("alt_phone")));
            binding.tvDob.setText(ValidationHelper.checkNull(mJSONObject.getString("dob")));
            binding.etPanNo.setText(ValidationHelper.checkNull(mJSONObject.getString("pan_number")));
            binding.etAaddharNo.setText(ValidationHelper.checkNull(mJSONObject.getString("aadhar_number")));
            binding.etCity.setText(ValidationHelper.checkNull(mJSONObject.getString("city")));
            binding.etCompanyName.setText(ValidationHelper.checkNull(mJSONObject.getString("company_name")));
            binding.etSkypeId.setText(ValidationHelper.checkNull(mJSONObject.getString("skype_id")));
            binding.etGstnNo.setText(ValidationHelper.checkNull(mJSONObject.getString("gstn")));
            switch (ValidationHelper.checkNull(mJSONObject.getString("gender"))){
                case "male":
                    binding.spnGender.setSelection(1);
                    break;
                case "female":
                    binding.spnGender.setSelection(2);
                    break;
                default:
                    binding.spnGender.setSelection(0);
                    break;
            }

            try {
                String urlString = mJSONObject.getString("profile_image_url");
                String tm = String.valueOf(System.currentTimeMillis() / (24 * 60 * 60 * 1000));

               /* Glide.with(this)
                        .load(url)
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


            } catch (Throwable mE) {
                mE.printStackTrace();
            }

        } catch (JSONException mE) {
            mE.printStackTrace();
        }

    }

    private void apiSaveProfile() {
        String gender;
        switch (binding.spnGender.getSelectedItemPosition()){
            case 1:
                gender = "male";
                break;
            case 2:
                gender = "female";
                break;
            default:
                gender = "other";
                break;
        }

        Map<String, String> params = new LinkedHashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        params.put("first_name",  binding.etFirstName.getText().toString());
        params.put("last_name", binding.etLastName.getText().toString());
        params.put("gender", gender);
        params.put("dob", binding.tvDob.getText().toString());

        params.put("pan_number", binding.etPanNo.getText().toString());
        params.put("company_name", binding.etCompanyName.getText().toString());

        params.put("gstn", binding.etGstnNo.getText().toString());
        params.put("city", binding.etCity.getText().toString());

        params.put("aadhar_number", binding.etAaddharNo.getText().toString());
        params.put("phone", binding.etPhone.getText().toString());

        params.put("alt_phone", binding.etAltPhone.getText().toString());
        params.put("skype_id", binding.etSkypeId.getText().toString());

        params.put("profile_image_url", S3_BUCKET_DEV_URL+imageName);


        AppController.getInstance().addToRequestQueue(new CustomRequest(Request.Method.POST, UrlConstants.ZODDL_EDIT_USER_PROFILE, params, SetProfileListener, SetProfileListenerError), "tag_Send_Message_req");

    }

    com.android.volley.Response.Listener<JSONObject> SetProfileListener = new com.android.volley.Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.d("response", response.toString());
            try {
                if (response.getString("ResponseCode") != null) {
                    if (!TextUtils.isEmpty(response.getString("ResponseCode")) &&
                            TextUtils.equals(response.getString("ResponseCode"), UrlConstants.RESPONSE_OK)) {
                        Toast.makeText(getBaseContext(), response.getString("ResponseMessage"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                    }
                } else {

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };
    com.android.volley.Response.ErrorListener SetProfileListenerError = new com.android.volley.Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("response", error.toString());


        }
    };


    public void setListeners() {
        binding.tvDob.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.ivProfilePic.setOnClickListener(this);
        binding.headerToolbar.ivLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dob:
                DatePickerFragment.getInstance(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        binding.tvDob.setText(String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year));
                    }
                }).show(getSupportFragmentManager(), "EditProfileActivity");
                break;
            case R.id.iv_profile_pic:
                addPhotoDialog();
                break;
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.btn_save:
                if (checkValidation()) {
                    apiSaveProfile();
                }
                break;
        }
    }

    private void addPhotoDialog() {
        if (CheckPermission.checkIsMarshMallowVersion() && !CheckPermission.checkCameraPermission(this)) {
            CheckPermission.requestCameraPermission(EditProfileActivity.this, CheckPermission.RC_CAMERA_PERMISSION);
        }
        com.theartofdev.edmodo.cropper.CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(EditProfileActivity.this);

       /* final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.choose_from_lib), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.take_photo))) {
                    imageName = CommonUtils.createProfileImageNameWithoutExtention(EditProfileActivity.this);
                    //takePicture(imageName+_IMAGE_EXTENSION_JPG);
                    dispatchTakePictureIntent();
                } else if (items[item].equals(getString(R.string.choose_from_lib))) {
                    imageName = CommonUtils.createProfileImageNameWithoutExtention(EditProfileActivity.this);
                    TakePictureUtils.openGallery(EditProfileActivity.this);

                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.create().show();
        builder.setCancelable(true);*/
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void takePicture(String fileName) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri = Uri.fromFile(new File(getExternalFilesDir("temp"), fileName));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 2222);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CheckPermission.RC_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    addPhotoDialog();
                } else {
                    Toast.makeText(EditProfileActivity.this, getString(R.string.camera_permission_denial), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            com.theartofdev.edmodo.cropper.CropImage.ActivityResult result = com.theartofdev.edmodo.cropper.CropImage.getActivityResult(intent);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                AWSMobileClient.getInstance().initialize(this).execute();
                imageName = CommonUtils.createProfileImageName(EditProfileActivity.this,"jpg");
                uploadImage(resultUri,imageName);


            } else if (resultCode == com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
       /* if (requestCode == TakePictureUtils.PICK_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(intent.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(this.getExternalFilesDir(_IMAGE_TEP_JPG), imageName + _IMAGE_EXTENSION_JPG));
                    TakePictureUtils.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    TakePictureUtils.startCropImage(this, imageName + _IMAGE_EXTENSION_JPG);

                } catch (Exception e) {
                    Toast.makeText(EditProfileActivity.this, getString(R.string.error_in_picture), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                TakePictureUtils.startCropImage(this, imageName+_IMAGE_EXTENSION_JPG);
            }

        } else if (requestCode == TakePictureUtils.CROP_FROM_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                String path = null;
                if (intent != null) {
                    path = intent.getStringExtra(CropImage.IMAGE_PATH);
                }
                if (path == null) {
                    return;
                }

                Uri uri = Uri.parse(path);
                binding.ivProfilePic.setImageURI(uri);

                try {
                    uploadImage(path);
                } catch (Throwable mE) {
                    Toast.makeText(EditProfileActivity.this, "Uploading failed, Please try again...", Toast.LENGTH_SHORT).show();
                }

                *//*try {
                    Glide.with(context)
                            .load(new File(path))
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(binding.ivProfilePic);
                } catch (Exception mE) {
                    mE.printStackTrace();
                }*//*
            }
        }*/
    }


    private synchronized void uploadImage(Uri urlPath,String imageName) {

        binding.ivProfilePic.setImageURI(urlPath);
        String path = urlPath.getPath();

        S3FileUploadHelper transferHelper = new S3FileUploadHelper(this);
        transferHelper.upload(path,imageName);

        transferHelper.setFileTransferListener(new S3FileUploadHelper.FileTransferListener() {
            @Override
            public void onSuccess(int id, TransferState state, String fileName) {
                Log.e("profile_img", String.format("%s uploaded successfully!", fileName));

            }
            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                //float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                //int percentDone = (int) percentDonef;
                // Log.d(TAG, "ID:" + id + " bytesCurrent: " + bytesCurrent + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }
            @Override
            public void onError(int id, Exception ex) {
                Log.e("profile_img",ex.getMessage());
            }
        });
    }

    private boolean checkValidation() {
        if (ValidationHelper.emptyFieldValidation(binding.etFirstName.getText().toString())) {
            CommonUtils.showToast(this, getString(R.string.please_enter_first_name));
            return false;
        } else if (ValidationHelper.emptyFieldValidation(binding.etLastName.getText().toString())) {
            CommonUtils.showToast(this, getString(R.string.please_enter_last_name));
            return false;
        } else if (ValidationHelper.emptyFieldValidation(binding.etEmail.getText().toString())) {
            CommonUtils.showToast(this, getString(R.string.please_enter_email));
            return false;
        } else if (ValidationHelper.emptyFieldValidation(binding.etPhone.getText().toString())) {
            CommonUtils.showToast(this, "Please enter mobile number.");
            return false;
        } else if (ValidationHelper.lengthFieldValidation(binding.etPhone.getText().toString(),10)) {
            CommonUtils.showToast(this, "Please enter valid primary mobile number.");
            return false;
        } else if (TextUtils.equals(gen, "Gender")) {
            CommonUtils.showToast(this, "Please select gender.");
            return false;
        } else if (ValidationHelper.emptyFieldValidation(binding.tvDob.getText().toString())) {
            CommonUtils.showToast(this, "Please select date of birth.");
            return false;
        } else {
            return true;
        }
    }


    /*@Override
    public String getTagName() {
        return EditProfileActivity.class.getSimpleName();
    }*/


    private void apiProfile() {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());

        AppController.getInstance().addToRequestQueue(new CustomRequest(Request.Method.POST, UrlConstants.ZODDL_GET_USER_PROFILE, params, SetSettingListener, SetSettingListenerError), "tag_Send_Message_req");

    }

    com.android.volley.Response.Listener<JSONObject> SetSettingListener = new com.android.volley.Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.d("response", response.toString());
            try {
                if (response.getString("ResponseCode") != null) {
                    if (!TextUtils.isEmpty(response.getString("ResponseCode")) &&
                            TextUtils.equals(response.getString("ResponseCode"), UrlConstants.RESPONSE_OK)) {
                        isFirstTime = false;
                        //Toast.makeText(context, response.getString("ResponseMessage"), Toast.LENGTH_SHORT).show();

                        JSONArray mJSONArray = response.getJSONArray("Payload");
                        JSONObject mJSONObject = mJSONArray.getJSONObject(0);


                    } else {

                    }
                } else {

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };
    com.android.volley.Response.ErrorListener SetSettingListenerError = new com.android.volley.Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("response", error.toString());


        }
    };

}
