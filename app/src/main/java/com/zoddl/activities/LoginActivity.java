package com.zoddl.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.zoddl.AppController;
import com.zoddl.R;
import com.zoddl.databinding.ActivityLoginBinding;
import com.zoddl.helper.CustomRequest;
import com.zoddl.interfaces.SocialMediaGetDataInterface;
import com.zoddl.model.CustomerDetail;
import com.zoddl.model.LoginResponse;
import com.zoddl.model.SocialMediaModel;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.CheckPermission;
import com.zoddl.utils.PrefManager;
import com.zoddl.utils.ValidationHelper;
import com.zoddl.helper.FBLoginActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.zoddl.interfaces.StringConstant._DATA_NOT_FOUND;
import static com.zoddl.utils.UrlConstants.ZODDL_LOGIN;
import static com.zoddl.utils.UrlConstants.ZODDL_SIGNUP;


public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, SocialMediaGetDataInterface {
    private ActivityLoginBinding binding;
    private Context context;
    private PrefManager prefManager;
    private ProgressDialog progressDialog;

    /* Google  */
    private GoogleApiClient mGoogleApiClient;
    private final int RC_SIGN_IN = 121;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        context = LoginActivity.this;

        prefManager = PrefManager.getInstance(context);

        setListeners();

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);

        new FBLoginActivity().inItFacebook(LoginActivity.this, callbackManager, LoginActivity.this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Please wait...");

        binding.cbRememberme.setChecked(prefManager.getKeyRememberMe());
        if (prefManager.getKeyRememberMe()) {
            binding.etEmail.setText(ValidationHelper.checkNull(prefManager.getKeyRememberMeUsername()));
            binding.etPassword.setText(ValidationHelper.checkNull(prefManager.getKeyRememberMePassword()));
        }

        if (CheckPermission.checkIsMarshMallowVersion() && !CheckPermission.checkCameraPermission(context)) {
            CheckPermission.requestCameraPermission(LoginActivity.this, CheckPermission.RC_CAMERA_PERMISSION);
        }


    }


    @Override
    public void setListeners() {
        binding.btnFbLogin.setOnClickListener(this);
        binding.btnGmailLogin.setOnClickListener(this);
        binding.btnLogIn.setOnClickListener(this);
        binding.btnSignUp.setOnClickListener(this);
        binding.tvForgotPassword.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_forgot_password:
                startActivity(new Intent(context, ForgotPasswordActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.btn_fb_login:
                if (CheckPermission.checkIsMarshMallowVersion() && !CheckPermission.checkCameraPermission(context)) {
                    CheckPermission.requestCameraPermission(LoginActivity.this, CheckPermission.RC_CAMERA_PERMISSION);
                }else{
                    progressDialog.show();
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("user_photos", "email", "user_birthday", "public_profile"));
                }

                break;
            case R.id.btn_gmail_login:
                if (CheckPermission.checkIsMarshMallowVersion() && !CheckPermission.checkCameraPermission(context)) {
                    CheckPermission.requestCameraPermission(LoginActivity.this, CheckPermission.RC_CAMERA_PERMISSION);
                }else{
                    progressDialog.show();
                    signIn();
                }

                break;
            case R.id.btn_log_in:
                if (checkValidation()) {
                    if (CheckPermission.checkIsMarshMallowVersion() && !CheckPermission.checkCameraPermission(context)) {
                        CheckPermission.requestCameraPermission(LoginActivity.this, CheckPermission.RC_CAMERA_PERMISSION);
                    }else{
                        progressDialog.show();
                        apiSignupLogin(ZODDL_LOGIN);
                    }
                }
                break;
            case R.id.btn_sign_up:
                if (checkValidation()) {
                    progressDialog.show();
                    apiSignupLogin(ZODDL_SIGNUP);
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            storeRegIdInPref(refreshedToken);
        } catch (NullPointerException mE) {
            mE.printStackTrace();
        }
    }
    private void storeRegIdInPref(String token) {
        PrefManager.getInstance(getApplicationContext()).setFirebaseId(token);
    }
    private boolean checkValidation() {
        settingNoErrorUI();
        if (ValidationHelper.emptyFieldValidation(binding.etEmail.getText().toString())) {
            binding.etEmail.requestFocus();
            binding.tvEmailError.setVisibility(View.VISIBLE);
            binding.etEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.tvEmailError.setText("*Please enter email id.");
            return false;
        } else if (ValidationHelper.emailFieldValidation(binding.etEmail.getText().toString())) {
            binding.tvEmailError.setVisibility(View.VISIBLE);
            binding.etEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.etEmail.requestFocus();
            binding.tvEmailError.setText("*Please enter valid email id.");
            return false;
        } else if (ValidationHelper.emptyFieldValidation(binding.etPassword.getText().toString())) {
            binding.tvPasswordError.setVisibility(View.VISIBLE);
            binding.etPassword.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.etPassword.requestFocus();
            binding.tvPasswordError.setText("*Please enter password.");
            return false;
        } else if (ValidationHelper.lengthFieldValidation(binding.etPassword.getText().toString(), 8)) {
            binding.tvPasswordError.setVisibility(View.VISIBLE);
            binding.etPassword.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_red_bottom));
            binding.etPassword.requestFocus();
            binding.tvPasswordError.setText("*Password must be of atleast 8 character.");
            return false;
        } else {
            if (binding.cbRememberme.isChecked()) {
                prefManager.setKeyRememberMe(true);
                prefManager.setKeyRememberMeUsername(binding.etEmail.getText().toString());
                prefManager.setKeyRememberMePassword(binding.etPassword.getText().toString());
            } else {
                prefManager.removeFromPreference(PrefManager.KEY_REMEMBER_ME);
                prefManager.removeFromPreference(PrefManager.KEY_REMEMBER_ME_PASSWORD);
                prefManager.removeFromPreference(PrefManager.KEY_REMEMBER_ME_USERNAME);
            }
            return true;
        }

    }

    private void settingNoErrorUI() {
        binding.etEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_navy_blue_bottom));
        binding.etPassword.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_navy_blue_bottom));

        binding.tvEmailError.setVisibility(View.GONE);
        binding.tvPasswordError.setVisibility(View.GONE);

    }


    /**
     * apiSignupLogin
     *
     * @param
     */
    String url="";
    private void apiSignupLogin(String Url) {
        url = Url;
        Map<String, String> params = new LinkedHashMap<>();
        params.put("Password", binding.etPassword.getText().toString());
        params.put("Email_Id", binding.etEmail.getText().toString());
        params.put("Login_Type", AppConstant.LOGIN_TYPE);
        params.put("Device_Token", prefManager.getFirebaseId());
        params.put("Device_Type", AppConstant.DEVICE_TYPE);

        AppController.getInstance().addToRequestQueue(new CustomRequest(Request.Method.POST, Url, params, loginResponseListener, loginResponseError), "tag_login_req");
    }


    Response.Listener<JSONObject> loginResponseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.d("response", response.toString());
            try {
                progressDialog.hide();

                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setResponseCode(response.getString("ResponseCode"));
                loginResponse.setResponseMessage(response.getString("ResponseMessage"));

                if (response.getInt("ResponseCode") == 200) {

                    if (url.equalsIgnoreCase(ZODDL_SIGNUP)){
                        showAlert(getString(R.string.congratulations),loginResponse.getResponseMessage());
                    }
                    else{
                        Toast.makeText(context, response.getString("ResponseMessage"), Toast.LENGTH_SHORT).show();
                        prefManager.setKeyIsLoggedIn(true);
                        prefManager.setKeyIsFirstTime(true);

                        JSONObject customerObj = response.getJSONObject("CustomerDetail");
                        CustomerDetail customerDetail = new CustomerDetail();
                        customerDetail.setCustomerid(customerObj.getString("Customerid"));
                        customerDetail.setPaidStatus(customerObj.getString("Paid_Status"));
                        customerDetail.setAuthtoken(customerObj.getString("Authtoken"));
                        customerDetail.setBaseUrl(customerObj.getString("BaseUrl"));
                        loginResponse.setCustomerDetail(customerDetail);
                        prefManager.setKeyCustomerId(customerObj.getString("Customerid"));
                        prefManager.setKeyAuthtoken(customerObj.getString("Authtoken"));

                        //HomeActivity.startHome(context);
                        CameraActivity.startCamera(context);
                    }

                } else {
                    showAlertDialog(loginResponse.getResponseMessage());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };
    Response.ErrorListener loginResponseError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            progressDialog.hide();
            Log.d("response", error.toString());


        }
    };

    private void makeSocialMediaLoginRequest(String firstName, String lastName, String email, String gender, String userId, String imageuri, String loginType, String dob) {

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Map<String, String> params = new LinkedHashMap<>();
        params.put("Login_Type", loginType);
        params.put("Email_Id", email);
        params.put("Social_Id", userId);
        params.put("Device_Token", prefManager.getFirebaseId());
        params.put("Device_Type", AppConstant.DEVICE_TYPE);

        AppController.getInstance().addToRequestQueue(new CustomRequest(Request.Method.POST, ZODDL_LOGIN, params, loginResponseListener, loginResponseError), "tag_user_social_login_req");

    }


    private void signIn() {

        readyGoogleSignIn();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // ****************** Google Login Start************************


    private void readyGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 201) {
            // HomeScreen.login=0;
            SocialMediaModel socialMediaModel = data
                    .getParcelableExtra("socialModel");
            if (socialMediaModel != null) {
                String firstName = socialMediaModel.getFirstName();
                String lastName = socialMediaModel.getLastName();
                String email = socialMediaModel.getEmail();
                String gender = socialMediaModel.getGender();
                String userId = socialMediaModel.getUserId();
                String imageuri = socialMediaModel.getProfilePic();
                String dob = socialMediaModel.getDob();

                makeSocialMediaLoginRequest(firstName, lastName, email, gender, userId, imageuri, "g", dob);

            }

        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }

        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("GOOGLE", "handleSignInResult:" + result.isSuccess());
        signOut();
        mGoogleApiClient.stopAutoManage(LoginActivity.this);
        mGoogleApiClient.disconnect();
        if (result.isSuccess()) {
            String pic = "";
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            Uri personPhotoUrl = acct.getPhotoUrl();
            String firstName = acct.getGivenName();
            String lastName = acct.getFamilyName();
            // int gender = acct.get;
            String userId = acct.getId();
            String email = acct.getEmail();

            //  signOut();

            if (personPhotoUrl != null) {
                pic = personPhotoUrl.toString();
            }

            makeSocialMediaLoginRequest(firstName, lastName, email, "f", userId, pic, "google", "");


        } else {

            showToast(_DATA_NOT_FOUND);
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("TAG", "Permission: " + permissions[0] + "was " + grantResults[0]);
            if (requestCode == CheckPermission.RC_CAMERA_PERMISSION){
                return;
            }
            signIn();

        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showToast(connectionResult.getErrorMessage());
    }

    @Override
    public void saveSocialMediaData(SocialMediaModel socialMediaModel) {
        if (socialMediaModel.getEmail().equalsIgnoreCase("")) {
            showToast("email not getting");
        } else {
            makeSocialMediaLoginRequest(socialMediaModel.getFirstName(), socialMediaModel.getLastName(), socialMediaModel.getEmail(), socialMediaModel.getGender(),
                    socialMediaModel.getUserId(), socialMediaModel.getProfilePic(), "f", socialMediaModel.getDob());

        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public String getTagName() {
        return LoginActivity.class.getSimpleName();
    }


}
