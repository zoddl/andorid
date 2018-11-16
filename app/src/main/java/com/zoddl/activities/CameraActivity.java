package com.zoddl.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zoddl.R;
import com.zoddl.database.MyRoomDatabase;
import com.zoddl.database.SycnTags;
import com.zoddl.databinding.ActivityCameraBinding;
import com.zoddl.fragments.AddTagDialog;
import com.zoddl.interfaces.StringConstant;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.CheckPermission;
import com.zoddl.utils.CommonUtils;
import com.zoddl.utils.DocUtils;
import com.zoddl.utils.ImageFilePath;
import com.zoddl.utils.NetworkUtils;
import com.zoddl.utils.PrefManager;
import com.zoddl.utils.imageutils.TakePictureUtils;
import com.zoddl.widgets.QuickAction;
import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraHostProvider;
import com.commonsware.cwac.camera.PictureTransaction;
import com.commonsware.cwac.camera.SimpleCameraHost;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import eu.janmuller.android.simplecropimage.CropImage;

import static com.zoddl.fragments.AddTagDialog.ARGS_DIALOG_CALLER_TYPE;
import static com.zoddl.fragments.AddTagDialog.ARGS_DIALOG_FLOW_NAME;
import static com.zoddl.fragments.AddTagDialog.ARGS_DIALOG_IMAGE_PATH;

/**
 *
 */

public class CameraActivity extends BaseActivity implements CameraHostProvider, StringConstant, AddTagDialog.OnEventListener {
    private static final Interpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();

    private static final int PICK_FILE_REQUEST_CODE = 4001;

    private String filePath;
    private Context context;
    private boolean pictureTaken = false;
    private ActivityCameraBinding binding;
    private String imageName;
    private String flowName;
    private boolean isProgress = true;
    private PrefManager mPrefManager;

    //********************for google drive****************************
    private static final String TAG = "BaseDriveActivity";
    protected static final String ACCOUNT_NAME_KEY = "account_name";
    protected static final int NEXT_AVAILABLE_REQUEST_CODE = 111;
    protected GoogleSignInClient mGoogleSignInClient;
    protected static final int REQUEST_CODE_SIGN_IN = 101;
    protected DriveResourceClient mDriveResourceClient;
    protected Uri mUri;
    protected DriveClient mDriveClient;
    protected String mAccountName;
    private static final int REQUEST_CODE_OPENER = NEXT_AVAILABLE_REQUEST_CODE + 1;
    private DriveId mCurrentDriveId;
    private Metadata mMetadata;
    private DriveContents mDriveContents;


    public static void startCamera(Context context) {
        context.startActivity(new Intent(context, CameraActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = CameraActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera);

        if (CheckPermission.checkIsMarshMallowVersion() && !CheckPermission.checkCameraPermission(context)) {
            CheckPermission.requestCameraPermission(CameraActivity.this, CheckPermission.RC_CAMERA_PERMISSION);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mPrefManager = PrefManager.getInstance(context);

        setListeners();

        binding.toolbarHeader.ivShare.setVisibility(View.GONE);
        binding.toolbarHeader.ivFilter.setVisibility(View.GONE);
        binding.toolbarHeader.ivSearch.setVisibility(View.GONE);
        binding.toolbarHeader.ivEdit.setVisibility(View.GONE);
        binding.toolbarHeader.ivGal.setVisibility(View.VISIBLE);
        binding.toolbarHeader.ivDoc.setVisibility(View.VISIBLE);
        binding.toolbarHeader.ivPlus.setVisibility(View.VISIBLE);
        binding.toolbarHeader.ivCrop.setVisibility(View.GONE);
        binding.toolbarHeader.ivCross.setVisibility(View.VISIBLE);
        binding.toolbarHeader.ivRefresh.setVisibility(View.GONE);
        binding.toolbarHeader.ivMenu.setImageResource(R.drawable.icon4);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onBackPressed() {
        HomeActivity.startHome(context);
        finish();
    }

    /**
     * This method is used to create a shutter effect while click an image
     */
    private void animateShutter() {
        binding.vShutter.setVisibility(View.VISIBLE);
        binding.vShutter.setAlpha(0.f);

        ObjectAnimator alphaInAnim = ObjectAnimator.ofFloat(binding.vShutter, "alpha", 0f, 0.8f);
        alphaInAnim.setDuration(100);
        alphaInAnim.setStartDelay(100);
        alphaInAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator alphaOutAnim = ObjectAnimator.ofFloat(binding.vShutter, "alpha", 0.8f, 0f);
        alphaOutAnim.setDuration(100);
        alphaOutAnim.setInterpolator(DECELERATE_INTERPOLATOR);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(alphaInAnim, alphaOutAnim);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.vShutter.setVisibility(View.GONE);
                binding.ivTakenPhoto.setVisibility(View.VISIBLE);
            }
        });
        animatorSet.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.cameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.cameraView.onPause();
    }
    String camImageName;
    @Override
    public CameraHost getCameraHost() {
        camImageName = CommonUtils.createProfileImageName(context, "jpg");
        return new MyCameraHost(context,camImageName);
    }

    @Override
    public String getTagName() {
        return CameraActivity.class.getSimpleName();
    }

    @Override
    public void setListeners() {
        binding.ivCam.setOnClickListener(this);
        binding.btnCashPlus.setOnClickListener(this);
        binding.btnCashMins.setOnClickListener(this);
        binding.btnBankPlus.setOnClickListener(this);
        binding.btnBankMins.setOnClickListener(this);
        binding.toolbarHeader.ivMenu.setOnClickListener(this);
        binding.toolbarHeader.ivGal.setOnClickListener(this);
        binding.toolbarHeader.ivDoc.setOnClickListener(this);
        binding.toolbarHeader.ivPlus.setOnClickListener(this);
        binding.toolbarHeader.ivCrop.setOnClickListener(this);
        binding.toolbarHeader.ivCross.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (isProgress) {
            switch (view.getId()) {
                case R.id.iv_cam:
                    isProgress = false;
                    flowName = _OTHER;
                    if (!pictureTaken) {
                        binding.cameraView.takePicture(true, true);
                        animateShutter();
                    }

                    break;
                case R.id.btn_cash_plus:
                    isProgress = false;
                    flowName = _CASH_PLUS;
                    if (!pictureTaken) {
                        binding.cameraView.takePicture(true, true);
                        animateShutter();
                    }

                    break;
                case R.id.btn_cash_mins:
                    isProgress = false;
                    flowName = _CASH_MINUS;
                    if (!pictureTaken) {
                        binding.cameraView.takePicture(true, true);
                        animateShutter();
                    }

                    break;
                case R.id.btn_bank_plus:
                    isProgress = false;
                    flowName = _BANK_PLUS;
                    if (!pictureTaken) {
                        binding.cameraView.takePicture(true, true);
                        animateShutter();
                    }

                    break;
                case R.id.btn_bank_mins:
                    isProgress = false;
                    flowName = _BANK_MINUS;
                    if (!pictureTaken) {
                        binding.cameraView.takePicture(true, true);
                        animateShutter();
                    }

                    break;
                case R.id.iv_cross:
                case R.id.iv_menu:
                    onBackPressed();
                    break;
                case R.id.iv_crop:
                    isProgress = true;
//                if (!TextUtils.isEmpty(filePath)) {
//                    TakePictureUtils.startCropImageFromPath(this, new File(filePath));
//                }else {
//                    showToast("Click photo first");
//                }
                    //showToast(_WORK_IN_PROGRESS);
                    break;
                case R.id.iv_plus:
                    isProgress = true;
                    showDialog(_MANUAL_GALLERY, "", "");
                    break;
                case R.id.iv_doc:
                    isProgress = true;
                    try {
                        initiatePopupWindow(binding.toolbarHeader.ivDoc);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("Unable to open file picker.");
                    }
                    break;
                case R.id.iv_gal:
                    isProgress = true;
                    imageName = CommonUtils.createProfileImageNameWithoutExtention(context);
                    TakePictureUtils.openGallery(CameraActivity.this);
                    break;
            }

        }
    }

    private void initiatePopupWindow(View view) {
        RelativeLayout customLayout = (RelativeLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.popup_menu_insert_doc, null);
        final QuickAction quickAction = new QuickAction(context, R.style.PopupAnimation, customLayout, customLayout);
        quickAction.show(view);

        TextView tvAttachFile = customLayout.findViewById(R.id.tv_attach_file);
        TextView tvInsertFromDrive = customLayout.findViewById(R.id.tv_insert_from_drive);

        tvAttachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.setType("application/*");

                startActivityForResult(intent, PICK_FILE_REQUEST_CODE);

                quickAction.dismiss();
            }
        });

        tvInsertFromDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSignedIn()) {
                    signIn();
                } else {
                    openDriveFile();
                }
                quickAction.dismiss();
            }
        });

    }

    /************************************for google drive***********************************/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ACCOUNT_NAME_KEY, mAccountName);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAccountName = savedInstanceState.getString(ACCOUNT_NAME_KEY);
    }

    public boolean isSignedIn() {
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        return mGoogleSignInClient != null
                && (signInAccount != null
                && signInAccount.getGrantedScopes().contains(Drive.SCOPE_FILE));
    }

    private void signIn() {
        Log.i(TAG, "Start sign-in.");
        mGoogleSignInClient = getGoogleSignInClient();
        // Attempt silent sign-in
        mGoogleSignInClient.silentSignIn()
                .addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        createDriveClients(googleSignInAccount);
                        openDriveFile();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Silent sign-in failed, display account selection prompt
                startActivityForResult(
                        mGoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
            }
        });
    }

    private GoogleSignInClient getGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE)
                        .build();
        return GoogleSignIn.getClient(this, signInOptions);
    }

    private void createDriveClients(GoogleSignInAccount googleSignInAccount) {
        Log.i(TAG, "Update view with sign-in account.");
        // Build a drive client.
        mDriveClient = Drive.getDriveClient(getApplicationContext(), googleSignInAccount);
        // Build a drive resource client.
        mDriveResourceClient =
                Drive.getDriveResourceClient(getApplicationContext(), googleSignInAccount);
    }


    /**
     * Launches an {@link Intent} to open an existing Drive file.
     */
    private void openDriveFile() {
        Log.i(TAG, "Open Drive file.");

        if (!isSignedIn()) {
            Log.w(TAG, "Failed to open file, user is not signed in.");
            return;
        }

        // Build activity options.
        final OpenFileActivityOptions openFileActivityOptions =
                new OpenFileActivityOptions.Builder()
                        .build();

        // Start a OpenFileActivityIntent
        mDriveClient.newOpenFileActivityIntentSender(openFileActivityOptions)
                .addOnSuccessListener(new OnSuccessListener<IntentSender>() {
                    @Override
                    public void onSuccess(IntentSender intentSender) {
                        try {
                            startIntentSenderForResult(
                                    intentSender,
                                    REQUEST_CODE_OPENER,
                            /* fillInIntent= */ null,
                            /* flagsMask= */ 0,
                            /* flagsValues= */ 0,
                            /* extraFlags= */ 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.w(TAG, "Unable to send intent.", e);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Unable to create OpenFileActivityIntent.", e);
            }
        });
    }

    /**
     * Retrieves the currently selected Drive file's metadata and contents.
     */
    private void loadCurrentFile() {
        Log.d(TAG, "Retrieving...");
        final DriveFile file = mCurrentDriveId.asDriveFile();

        // Retrieve and store the file metadata and contents.
        mDriveResourceClient.getMetadata(file)
                .continueWithTask(new Continuation<Metadata, Task<DriveContents>>() {
                    @Override
                    public Task<DriveContents> then(@NonNull Task<Metadata> task) {
                        if (task.isSuccessful()) {
                            mMetadata = task.getResult();
                            return mDriveResourceClient.openFile(file, DriveFile.MODE_READ_ONLY);
                        } else {
                            return Tasks.forException(task.getException());
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<DriveContents>() {
            @Override
            public void onSuccess(DriveContents driveContents) {

                mDriveContents = driveContents;
                refreshUiFromCurrentFile();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // FIXME: 1/6/18 Google drive rnd part, if file is private like not shareable, is it possible to add to our projects.
                //com.google.android.gms.common.api.ApiException: 10: This file is not openable.
                showAlert("Unable to retrieve file!","This file is not openable.");
                //Log.e(TAG, "Unable to retrieve file metadata and contents.", e);
            }
        });
    }

    /**
     * Refreshes the main content view with the current activity state.
     */
    private void refreshUiFromCurrentFile() {
        Log.d(TAG, "Refreshing...");
        if (mCurrentDriveId == null) {
            return;
        }
        if (mMetadata == null || mDriveContents == null) {
            return;
        }
        String realPath = DocUtils.writeStreamToFile(this, mDriveContents.getInputStream(), mMetadata.getTitle());

        if (mPrefManager.getFreeOrPaid())
            showDialog(_DOCUMENT, "", realPath);
        else {
            updateWithoutTagInfoDueToFreeUser(_DOCUMENT,realPath);
        }
    }

    private void updateWithoutTagInfoDueToFreeUser(String type,String url) {

        MyRoomDatabase db = Room.databaseBuilder(context, MyRoomDatabase.class, AppConstant.DATABASE_NAME).allowMainThreadQueries().build();

        String imageUrl,docUrl,fileType;

        if (type.equalsIgnoreCase(_CAMERA)||type.equalsIgnoreCase(_GALLERY)){
            imageUrl = url;
            docUrl = "";
            fileType = _GALLERY;
        }else{
            imageUrl = url;
            docUrl = url;
            fileType = _DOCUMENT;
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(c.getTime());

        SycnTags obj = new SycnTags("", "", _TAG_STATUS, "",
                date, "", "", imageUrl, imageUrl, docUrl, _AUTO,"pending",fileType);

        db.getDao().insertAll(obj);

        if (NetworkUtils.isOnline(context))
            ((BaseActivity)context).checkAndStartService();
    }

    /************************************end google drive***********************************/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_OPENER:
                    mCurrentDriveId = (DriveId) intent.getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                    loadCurrentFile();

                    break;

                case REQUEST_CODE_SIGN_IN:
                    Log.i(TAG, "Signed in successfully.");
                    // Create Drive clients now that account has been authorized access.
                    createDriveClients(GoogleSignIn.getLastSignedInAccount(this));
                    openDriveFile();
                    break;

                case TakePictureUtils.PICK_GALLERY:
                    try {
                        InputStream inputStream = this.getContentResolver().openInputStream(intent.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(this.getExternalFilesDir(_IMAGE_TEP_JPG), imageName + _IMAGE_EXTENSION_JPG));
                        TakePictureUtils.copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();
                        TakePictureUtils.startCropImage(this, imageName + _IMAGE_EXTENSION_JPG);

                    } catch (Exception e) {
                        showToast(getString(R.string.error_in_picture));
                    }
                    break;
                case TakePictureUtils.CROP_FROM_CAMERA:
                    String path = null;
                    if (intent != null) {
                        path = intent.getStringExtra(CropImage.IMAGE_PATH);
                    }
                    if (path == null) {
                        return;
                    }

                    if (mPrefManager.getFreeOrPaid()){
                        showDialog(_GALLERY, "", path);
                        Glide.with(context)
                                .load(new File(path))
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(binding.ivTakenPhoto);

                        binding.vShutter.setVisibility(View.GONE);
                        binding.ivTakenPhoto.setVisibility(View.VISIBLE);
                    }
                    else {
                        updateWithoutTagInfoDueToFreeUser(_GALLERY,path);
                        binding.vShutter.setVisibility(View.VISIBLE);
                        binding.ivTakenPhoto.setVisibility(View.GONE);
                    }

                    //uploadWithTransferUtility(path);
                    break;
                case PICK_FILE_REQUEST_CODE:
                    String name = ImageFilePath.getPath(context, intent.getData());

                    String newName = null;

                    File file = new File(name);
                    String fileN = file.getName();
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        newName = DocUtils.writeStreamToFile(context,fileInputStream,fileN);
                    } catch (FileNotFoundException mE) {
                        mE.printStackTrace();
                    }


                    if (newName!=null){
                        if (mPrefManager.getFreeOrPaid())
                            showDialog(_DOCUMENT, "", newName);
                        else {
                            updateWithoutTagInfoDueToFreeUser(_DOCUMENT,newName);
                        }
                    }

                    break;

            }
        }else{
            if(requestCode == TakePictureUtils.CROP_FROM_CAMERA){
                binding.vShutter.setVisibility(View.VISIBLE);
                binding.ivTakenPhoto.setVisibility(View.GONE);
            }

        }
    }


    void showDialog(String type, String flow, String imageName) {
        // Create the fragment and show it as a dialog.
        DialogFragment newFragment = AddTagDialog.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_DIALOG_CALLER_TYPE, type);
        bundle.putString(ARGS_DIALOG_FLOW_NAME, flow);
        bundle.putString(ARGS_DIALOG_IMAGE_PATH, imageName);
        AddTagDialog.setOnEventListener(this);
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onEvent(SycnTags er, int pos) {

    }

    @Override
    public void onEvent(String status) {
        if (status.equalsIgnoreCase("Cancle")) {
            binding.ivTakenPhoto.setVisibility(View.GONE);
        }
    }

    File file;
    class MyCameraHost extends SimpleCameraHost {

        private Camera.Size previewSize;
        Context context;
        public MyCameraHost(Context ctxt, String imageName) {
            super(ctxt, imageName);
            context = ctxt;
        }

        @Override
        public void onCameraFail(FailureReason reason) {
            super.onCameraFail(reason);
            showToast(getString(R.string.camera_error));
            finish();
        }

        @Override
        public boolean useFullBleedPreview() {
            return true;
        }

        @Override
        public Camera.Size getPictureSize(PictureTransaction xact, Camera.Parameters parameters) {
            return previewSize;
        }

        @Override
        public Camera.Parameters adjustPreviewParameters(Camera.Parameters parameters) {
            Camera.Parameters parameters1 = super.adjustPreviewParameters(parameters);
            previewSize = parameters1.getPreviewSize();
            return parameters1;
        }

        @Override
        public void saveImage(PictureTransaction xact, final Bitmap bitmap) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.ivTakenPhoto.setImageBitmap(bitmap);
                }
            });
        }

        @Override
        public void saveImage(PictureTransaction xact, byte[] image) {
            super.saveImage(xact, image);
            file = getFilePath();
            filePath = file.getPath();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isProgress = true;

                                if (mPrefManager.getFreeOrPaid())
                                    showDialog(_CAMERA, flowName, filePath);
                                else {
                                    startCropImageFromPath();
                                    //updateWithoutTagInfoDueToFreeUser(_CAMERA,filePath);
                                }
                        }
                    }, 500);
                }
            });
        }
    }

    public void startCropImageFromPath() {
        TakePictureUtils.startCropImageFromPath(this, file.getPath());
    }
}