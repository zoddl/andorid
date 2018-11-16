package com.zoddl.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zoddl.AppController;
import com.zoddl.R;
import com.zoddl.VolleyApiRequest;
import com.zoddl.database.MyRoomDatabase;
import com.zoddl.database.SycnTags;
import com.zoddl.databinding.ActivityHomeBinding;
import com.zoddl.fragments.AddTagDialog;
import com.zoddl.fragments.AlertFragment;
import com.zoddl.fragments.ContactUsFragment;
import com.zoddl.fragments.DocumentFragment;
import com.zoddl.fragments.FAQFragment;
import com.zoddl.fragments.GalleryFragment;
import com.zoddl.fragments.GalleryPrimaryOrSecondaryTagSearchList;
import com.zoddl.fragments.HomeFragment;
import com.zoddl.fragments.HomePostFragment;
import com.zoddl.fragments.HomePostSortFragment;
import com.zoddl.fragments.HowItWorksFragment;
import com.zoddl.fragments.MakePaymentFragment;
import com.zoddl.fragments.MySettingsFragment;
import com.zoddl.fragments.PostDetailFragment;
import com.zoddl.fragments.PostFragment;
import com.zoddl.fragments.PostSortFragment;
import com.zoddl.fragments.PrimaryOrSecondarySeeAllFragment;
import com.zoddl.fragments.ProfileFragment;
import com.zoddl.fragments.ReportFragment;
import com.zoddl.fragments.HomeSeeAllFragment;
import com.zoddl.helper.CustomRequest;
import com.zoddl.interfaces.OnEventListenerTollbar;
import com.zoddl.interfaces.StringConstant;
import com.zoddl.model.gallery.GalleryDetails;
import com.zoddl.model.gallery.TaglistItem;
import com.zoddl.model.postdetials.Payload;
import com.zoddl.navigationdrawer.DrawerFragment;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.CheckPermission;
import com.zoddl.utils.CommonUtils;
import com.zoddl.utils.DialogUtils;
import com.zoddl.utils.DocUtils;
import com.zoddl.utils.ImageFilePath;
import com.zoddl.utils.ImageUtils;
import com.zoddl.utils.LogUtils;
import com.zoddl.utils.NetworkUtils;
import com.zoddl.utils.PrefManager;
import com.zoddl.utils.UrlConstants;
import com.zoddl.utils.imageutils.TakePictureUtils;
import com.zoddl.widgets.QuickAction;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eu.janmuller.android.simplecropimage.CropImage;

import static com.zoddl.fragments.AddTagDialog.ARGS_DIALOG_CALLER_TYPE;
import static com.zoddl.fragments.AddTagDialog.ARGS_DIALOG_FLOW_NAME;
import static com.zoddl.fragments.AddTagDialog.ARGS_DIALOG_IMAGE_PATH;
import static com.zoddl.utils.AppConstant.GALLERY_SUBMAIN_ADAPTER_EVENT;
import static com.zoddl.utils.AppConstant.HOME_BOTTOM_MENU_SELECTION_FROM;
import static com.zoddl.utils.AppConstant.HOME_BOTTOM_MENU_SELECTION_FROM_DOCUMENT;
import static com.zoddl.utils.AppConstant.HOME_BOTTOM_MENU_SELECTION_FROM_GALLERY;
import static com.zoddl.utils.AppConstant.HOME_POST_SORT_DETAILS_DATA;
import static com.zoddl.utils.AppConstant.HOME_POST_SORT_NEXT_NAV_DETAILS_DATE;
import static com.zoddl.utils.AppConstant.HOME_POST_SORT_NEXT_NAV_DETAILS_ID;
import static com.zoddl.utils.AppConstant.HOME_POST_SORT_SEEALL_DETAILS_ID;
import static com.zoddl.utils.AppConstant.HOME_POST_SORT_SEEALL_DETAILS_OBJECT;
import static com.zoddl.utils.AppConstant.HOME_POST_SORT_SEEALL_DETAILS_SOURCE_TYPE;
import static com.zoddl.utils.AppConstant.HOME_POST_SORT_SEEALL_DETAILS_TYPE;
import static com.zoddl.utils.AppConstant.HOME_SENDING_REPORT_AT_HOME;
import static com.zoddl.utils.AppConstant.HOME_TAG_DETAILS_ID;
import static com.zoddl.utils.AppConstant.HOME_TAG__DETAILS_FROM;
import static com.zoddl.utils.AppConstant.HOME_TOLLBAR_FILLTER_BY_ALPHABET;
import static com.zoddl.utils.AppConstant.HOME_TOLLBAR_FILLTER_BY_AMOUNT;
import static com.zoddl.utils.AppConstant.HOME_TOLLBAR_FILLTER_BY_COUNT;
import static com.zoddl.utils.AppConstant.HOME_TOLLBAR_FILLTER_BY_USES;
import static com.zoddl.utils.AppConstant.HOME_TOLLBAR_FILLTER_NONE;
import static com.zoddl.utils.AppConstant.HOME_TOLLBAR_REPORT_REFRESH_BUTTON;
import static com.zoddl.utils.AppConstant.HOME_TOLLBAR_REPORT_SHARE_BUTTON;
import static com.zoddl.utils.UrlConstants.ZODDL_LOGOUT;

public class HomeActivity extends BaseActivity implements DrawerFragment.FragmentDrawerListener,StringConstant,AddTagDialog.OnEventListener {

    private static final int PICK_FILE_REQUEST_CODE = 110;
    private ActivityHomeBinding binding;
    private Context context;
    private String[] titles;
    private Fragment fragment;
    private AppController caApplication;
    private int section = 0;
    private PrefManager prefManager;
    private ProgressDialog progressDialog;
    private String imageName="";

    //********************for google drive****************************
    private static final String TAG = "BaseDriveActivity";

    protected static final int NEXT_AVAILABLE_REQUEST_CODE = 111;
    protected GoogleSignInClient mGoogleSignInClient;
    protected static final int REQUEST_CODE_SIGN_IN = 101;
    protected DriveResourceClient mDriveResourceClient;

    protected DriveClient mDriveClient;
    private static final int REQUEST_CODE_OPENER = NEXT_AVAILABLE_REQUEST_CODE + 1;
    private DriveId mCurrentDriveId;
    private Metadata mMetadata;
    private DriveContents mDriveContents;

    public static void startHome(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        context = HomeActivity.this;

        titles = context.getResources().getStringArray(R.array.drawer_array_text);
        caApplication = (AppController) getApplicationContext();
        prefManager = PrefManager.getInstance(context);


        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        setListeners();
        drawerSetting();

        onDrawerItemSelected(null, AppConstant.FRAGMENT_HOME);

        if (CheckPermission.checkIsMarshMallowVersion() && !CheckPermission.checkCameraPermission(context)) {
            CheckPermission.requestCameraPermission(HomeActivity.this, CheckPermission.RC_CAMERA_PERMISSION);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (caApplication.getCurrentSection() == AppConstant.FRAGMENT_CAMERA) {
            onDrawerItemSelected(null, AppConstant.FRAGMENT_HOME);
        }

    }
    DrawerFragment drawerFragment;

    private void drawerSetting() {
        binding.toolbarHeader.toolbar.setNavigationIcon(null);
        setSupportActionBar(binding.toolbarHeader.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        drawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setDrawerListener(this);
    }

    @Override
    public void setListeners() {
        //header toolbar listener
        binding.toolbarHeader.ivGal.setOnClickListener(this);
        binding.toolbarHeader.ivShare.setOnClickListener(this);
        binding.toolbarHeader.ivDoc.setOnClickListener(this);
        binding.toolbarHeader.ivPlus.setOnClickListener(this);
        binding.toolbarHeader.ivFilter.setOnClickListener(this);
        binding.toolbarHeader.ivRefresh.setOnClickListener(this);
        binding.toolbarHeader.ivSearch.setOnClickListener(this);
        binding.toolbarHeader.ivCrop.setOnClickListener(this);
        binding.toolbarHeader.ivCross.setOnClickListener(this);
        binding.toolbarHeader.ivEdit.setOnClickListener(this);
        binding.toolbarHeader.ivMenu.setOnClickListener(this);

        //footer toolbar listener
        binding.layoutFooter.ivHome.setOnClickListener(this);
        binding.layoutFooter.ivGallery.setOnClickListener(this);
        binding.layoutFooter.ivCamera.setOnClickListener(this);
        binding.layoutFooter.ivWeb.setOnClickListener(this);
        binding.layoutFooter.ivDocument.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                if (fragment != null && (fragment instanceof PostDetailFragment || fragment instanceof PostFragment || fragment instanceof PrimaryOrSecondarySeeAllFragment
                        || fragment instanceof PostSortFragment || fragment instanceof GalleryPrimaryOrSecondaryTagSearchList || fragment instanceof HomeSeeAllFragment || fragment instanceof HomePostSortFragment || fragment instanceof HomePostFragment)) {
                    onBackPressed();
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.iv_edit:
                Intent editProf = new Intent(context, EditProfileActivity.class);
                editProf.putExtra("ProfileData",profileD);
                editProf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(editProf);
                break;
            case R.id.iv_cross:
                //showToast(_WORK_IN_PROGRESS);
                break;
            case R.id.iv_crop:
                //showToast(_WORK_IN_PROGRESS);
                break;
            case R.id.iv_filter:
                initiatePopupWindow(binding.toolbarHeader.ivFilter);
                break;
            case R.id.iv_plus:
                if (getFragmentName().equalsIgnoreCase(_DOCUMENT)){
                    showDialog(_MANUAL_DOCUMENT,"","");
                }else{
                    showDialog(_MANUAL_GALLERY, "", "");
                }

                break;
            case R.id.iv_refresh:
                if (mOnEventListenerTollbar != null)
                    mOnEventListenerTollbar.onEvent(_REPORT,HOME_TOLLBAR_REPORT_REFRESH_BUTTON);
                //onDrawerItemSelected(null, AppConstant.FRAGMENT_WEB);
                break;
            case R.id.iv_search:
                loadToolBarSearch();
                break;
            case R.id.iv_doc:
                initiatePopup(binding.toolbarHeader.ivDoc);

               /* try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(intent,PICK_FILE_REQUEST_CODE);

                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Unable to open file picker.");
                }*/

                break;
            case R.id.iv_share:
                if (mOnEventListenerTollbar != null)
                    mOnEventListenerTollbar.onEvent(_REPORT,HOME_TOLLBAR_REPORT_SHARE_BUTTON);
                break;
            case R.id.iv_gal:
                imageName = CommonUtils.createProfileImageNameWithoutExtention(context);
                TakePictureUtils.openGallery(this);
                break;
            case R.id.iv_home:
                onDrawerItemSelected(null, AppConstant.FRAGMENT_HOME);
                break;
            case R.id.iv_gallery:
                onDrawerItemSelected(null, AppConstant.FRAGMENT_GALLERY);
                break;
            case R.id.iv_camera:
                onDrawerItemSelected(null, AppConstant.FRAGMENT_CAMERA);
                break;
            case R.id.iv_web:
                onDrawerItemSelected(null, AppConstant.FRAGMENT_WEB);
                break;
            case R.id.iv_document:
                onDrawerItemSelected(null, AppConstant.FRAGMENT_DOC);
                break;
        }
    }


    private void initiatePopup(View val) {
        RelativeLayout customLayout = (RelativeLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.popup_menu_insert_doc, null);
        final QuickAction quickAction = new QuickAction(context, R.style.PopupAnimation, customLayout, customLayout);

        quickAction.show(val);

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

    void showDialog(String type, String flow,String imageName) {
        // Create the fragment and show it as a dialog.
        DialogFragment newFragment = AddTagDialog.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_DIALOG_CALLER_TYPE,type);
        bundle.putString(ARGS_DIALOG_FLOW_NAME, flow);
        bundle.putString(ARGS_DIALOG_IMAGE_PATH, imageName);
        AddTagDialog.setOnEventListener(this);
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void loadToolBarSearch() {
        View view = this.getLayoutInflater().inflate(R.layout.view_toolbar_search, null);
        LinearLayout parentToolbarSearch = view.findViewById(R.id.parent_toolbar_search);
        ImageView imgToolBack = view.findViewById(R.id.img_tool_back);
        final EditText edtToolSearch = view.findViewById(R.id.edt_tool_search);
        ImageView imgToolMic = view.findViewById(R.id.img_tool_mic);
        final ListView listSearch = view.findViewById(R.id.list_search);
        final TextView txtEmpty = view.findViewById(R.id.txt_empty);
        CommonUtils.setListViewHeightBasedOnChildren(listSearch);
        edtToolSearch.setHint("Search your group");
        final Dialog toolbarSearchDialog = new Dialog(this, R.style.MaterialSearch);
        toolbarSearchDialog.setContentView(view);
        toolbarSearchDialog.setCancelable(true);
        toolbarSearchDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        toolbarSearchDialog.getWindow().setGravity(Gravity.BOTTOM);
        toolbarSearchDialog.show();
        toolbarSearchDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ((HomeActivity) context).callForTagClick(_PRIMARY,searchListOfTag.get(position).getPrimeName(),searchListOfTag.get(position).getPrimaryTag());
                ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_GALLERY_Click);

                toolbarSearchDialog.dismiss();
            }
        });
        edtToolSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence searchVal, int start, int before, int count) {
                if (searchVal.length() > 0) {
                    getSearchData(searchVal.toString(),listSearch, txtEmpty);
                } else {
                    listSearch.setVisibility(View.GONE);
                    txtEmpty.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarSearchDialog.dismiss();
            }
        });
        imgToolMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtToolSearch.setText("");
            }
        });
    }

    List<TaglistItem> searchListOfTag = new ArrayList<>();

    public void getSearchData(final String searchVal, final ListView mListSearch, final TextView mTxtEmpty) {
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                searchListOfTag.clear();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {

                        GalleryDetails details= VolleyApiRequest.parseGalleryDetails(object);

                        List<String> list = new ArrayList<>();
                        searchListOfTag.addAll(details.getTaglist());

                        for (TaglistItem item: details.getTaglist()){
                            if (searchVal.equalsIgnoreCase(item.getPrimeName())){
                                list.add(item.getPrimeName());
                            }else {
                                list.add(item.getImages().get(0).getSecondaryTag().get(0).getSecondaryName());
                            }
                        }
                        boolean isNodata = false;
                        if (list.size()>0){
                            mListSearch.setVisibility(View.VISIBLE);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,list);
                            mListSearch.setAdapter(adapter);
                            isNodata = true;
                            mTxtEmpty.setVisibility(View.GONE);
                        }
                        if (!isNodata) {
                            mListSearch.setVisibility(View.GONE);
                            mTxtEmpty.setVisibility(View.VISIBLE);
                            mTxtEmpty.setText("No data found");
                        }
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("search _Tag_Error", error.toString());
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        params.put("Searchdata",searchVal);
        VolleyApiRequest request = new VolleyApiRequest(UrlConstants.ZODDL_GALLERY_TAG_SEARCH_GLOBAL, params, stringListener, errorListener);
        Volley.newRequestQueue(context).add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, intent);
        }

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TakePictureUtils.PICK_GALLERY:
                    try {
                        InputStream inputStream = context.getContentResolver().openInputStream(intent.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(context.getExternalFilesDir(_IMAGE_TEP_JPG), imageName + _IMAGE_EXTENSION_JPG));
                        TakePictureUtils.copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();
                        TakePictureUtils.startCropImage(this, imageName + _IMAGE_EXTENSION_JPG);

                    } catch (Exception e) {
                        showAlert(getString(R.string.app_name),getString(R.string.error_in_picture));
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


                    if (prefManager.getFreeOrPaid())
                        showDialog(_GALLERY, "", path);
                    else {
                        updateWithoutTagInfoDueToFreeUser(_GALLERY,path);
                    }

                    //uploadWithTransferUtility(path);
                    break;
                case REQUEST_CODE_OPENER:
                    mCurrentDriveId = (DriveId) intent.getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                    loadCurrentFile();

                    break;

                case REQUEST_CODE_SIGN_IN:
                    Log.i(TAG, "Signed in successfully.");
                    // Create Drive clients now that account has been authorized access.
                    createDriveClients(GoogleSignIn.getLastSignedInAccount(context));
                    openDriveFile();
                    break;

                case PICK_FILE_REQUEST_CODE:
                    String name = ImageFilePath.getPath(context, intent.getData());

                    String fileEx = DocUtils.findDocExe(name);

                    File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                    String newFileName = dir.getPath()+"/"+CommonUtils.createNameWithoutExtention(context)+
                            fileEx;

                    ImageUtils.renameFile(name,newFileName);

                    if (prefManager.getFreeOrPaid())
                        showDialog(_DOCUMENT, "", newFileName);
                    else {
                        updateWithoutTagInfoDueToFreeUser(_DOCUMENT,newFileName);
                    }

                    break;
            }


    }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

        Bundle bundle = new Bundle();

        /*if (fragment != null && fragment instanceof MySettingsFragment) {

        }*/

        //binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);

        if (position != AppConstant.FRAGMENT_POST_DETAIL)
            caApplication.setCurrentSection(position);

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        fragment = null;
        switch (position) {
            case AppConstant.FRAGMENT_CONTACT_US:
                binding.toolbarHeader.tvTitle.setText(titles[position]);
                fragment = new ContactUsFragment();
                break;
            case AppConstant.FRAGMENT_MY_SETTINGS:
                binding.toolbarHeader.tvTitle.setText(titles[position]);
                fragment = new MySettingsFragment();
                break;
            case AppConstant.FRAGMENT_MY_PROFILE:
                binding.toolbarHeader.tvTitle.setText("Profile");
                fragment = new ProfileFragment();
                break;
            case AppConstant.FRAGMENT_ALERT_NOTIFICATION:
                binding.toolbarHeader.tvTitle.setText(titles[position]);
                fragment = new AlertFragment();
                break;
            case AppConstant.FRAGMENT_LOGOUT:
                logoutFromApp();
                break;
            /*case AppConstant.FRAGMENT_PLAN_SUBSCRIPTION:
                binding.toolbarHeader.tvTitle.setText(titles[position]);
                fragment = new PlanSubscriptionFragment();
                break;*/
            case AppConstant.FRAGMENT_MAKE_PAYMENT:
                binding.toolbarHeader.tvTitle.setText(titles[position]);
                fragment = new MakePaymentFragment();
                break;
            case AppConstant.FRAGMENT_HOW_ITS_WORK:
                binding.toolbarHeader.tvTitle.setText(titles[position]);
                fragment = new HowItWorksFragment();
                break;
            case AppConstant.FRAGMENT_FAQ:
                binding.toolbarHeader.tvTitle.setText(titles[position]);
                fragment = new FAQFragment();
                break;

            case AppConstant.FRAGMENT_HOME:
                section = AppConstant.FRAGMENT_HOME;
                binding.toolbarHeader.tvTitle.setText("Home");
                setFragmentName(_HOME);
                fragment = new HomeFragment();
                break;
            case AppConstant.FRAGMENT_GALLERY:
                section = AppConstant.FRAGMENT_GALLERY;
                binding.toolbarHeader.tvTitle.setText("Gallery");
                setFragmentName(_GALLERY);
                bundle.putString(HOME_BOTTOM_MENU_SELECTION_FROM, HOME_BOTTOM_MENU_SELECTION_FROM_GALLERY);
                fragment = new GalleryFragment();
                fragment.setArguments(bundle);
                break;

            case AppConstant.FRAGMENT_GALLERY_Click:
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                binding.toolbarHeader.tvTitle.setText(tagName);
                bundle.putString(HOME_TAG__DETAILS_FROM, fromWhere);
                bundle.putString(HOME_TAG_DETAILS_ID, tagId);
                fragment = new GalleryPrimaryOrSecondaryTagSearchList();
                fragment.setArguments(bundle);
                break;

            case AppConstant.FRAGMENT_CAMERA:
                if (CheckPermission.checkIsMarshMallowVersion() && !CheckPermission.checkCameraPermission(context)) {
                    CheckPermission.requestCameraPermission(HomeActivity.this, CheckPermission.RC_CAMERA_PERMISSION);
                } else {
                    startActivity(new Intent(context, CameraActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                break;
            case AppConstant.FRAGMENT_WEB:
                binding.toolbarHeader.tvTitle.setText("Report");
                setFragmentName(_REPORT);
                fragment = new ReportFragment();
                if (!TextUtils.isEmpty(mReportjson)){
                    bundle.putString(HOME_SENDING_REPORT_AT_HOME, mReportjson);
                    fragment.setArguments(bundle);
                }
                mReportjson = "";
                break;
            case AppConstant.FRAGMENT_DOC:
                //mSearchView.setVisibility(View.VISIBLE);
                section = AppConstant.FRAGMENT_DOC;
                binding.toolbarHeader.tvTitle.setText("Documents");
                setFragmentName(_DOCUMENT);
                bundle.putString(HOME_BOTTOM_MENU_SELECTION_FROM, HOME_BOTTOM_MENU_SELECTION_FROM_DOCUMENT);
                fragment = new DocumentFragment();
                fragment.setArguments(bundle);
                break;
            case AppConstant.FRAGMENT_POST:
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                bundle.putString(HOME_POST_SORT_NEXT_NAV_DETAILS_ID, postDetailsPrimaryTagId);
                bundle.putString(HOME_POST_SORT_NEXT_NAV_DETAILS_DATE, getPostDetailsPrimaryTagDate);
                binding.toolbarHeader.tvTitle.setText(postDetailsPrimaryTag);
                fragment = new PostFragment();
                fragment.setArguments(bundle);
                break;
            case AppConstant.FRAGMENT_POST_PRIMARY_SECONDARY:
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                bundle.putString(HOME_POST_SORT_NEXT_NAV_DETAILS_ID, postDetailsPrimaryTagId);
                bundle.putString(HOME_POST_SORT_NEXT_NAV_DETAILS_DATE, getPostDetailsPrimaryTagDate);
                binding.toolbarHeader.tvTitle.setText(postDetailsPrimaryTag);
                fragment = new PrimaryOrSecondarySeeAllFragment();
                fragment.setArguments(bundle);
                break;

            case AppConstant.FRAGMENT_POST_DETAIL:
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                binding.toolbarHeader.tvTitle.setText("Details");
                bundle.putString(GALLERY_SUBMAIN_ADAPTER_EVENT,mImagesItem);
                if (mImagesItem==null){
                    showAlert(getString(R.string.app_name),_DETAILS_ARE_UPLOADING);
                    return;
                }

                fragment = new PostDetailFragment();
                fragment.setArguments(bundle);

                break;

            case AppConstant.FRAGMENT_POST_SORT:
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                if (postPrimaryTagId==null){
                    showAlert(getString(R.string.app_name),_DETAILS_ARE_UPLOADING);
                    return;
                }
                bundle.putString(HOME_POST_SORT_DETAILS_DATA, postPrimaryTagId);
                binding.toolbarHeader.tvTitle.setText(postPrimaryTag);
                fragment = new PostSortFragment();
                fragment.setArguments(bundle);
                break;

            case AppConstant.FRAGMENT_HOME_SEEALL_POST_DETAILS:
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                bundle.putString(HOME_POST_SORT_SEEALL_DETAILS_TYPE, hSeeAllByMonthTagType);
                binding.toolbarHeader.tvTitle.setText(hSeeAllByMonthTagType.toUpperCase());
                fragment = new HomeSeeAllFragment();
                fragment.setArguments(bundle);
                break;
            case AppConstant.FRAGMENT_HOME_SEEALL_POST_DETAILS_BY_MONTH:
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                bundle.putString(HOME_POST_SORT_SEEALL_DETAILS_TYPE, hSeeAllByMonthTagType);
                bundle.putString(HOME_POST_SORT_SEEALL_DETAILS_ID, hSeeAllByMonthTagId);
                bundle.putString(HOME_POST_SORT_SEEALL_DETAILS_SOURCE_TYPE, hSeeAllByMonthSourceType);

                binding.toolbarHeader.tvTitle.setText(hSeeAllByMonthTagType.toUpperCase());

                fragment = new HomePostSortFragment();
                fragment.setArguments(bundle);
                break;
            case AppConstant.FRAGMENT_HOME_SEEALL_POST_DETAILS_BY_MONTH_DETAILS:
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                bundle.putParcelable(HOME_POST_SORT_SEEALL_DETAILS_OBJECT, mPayload);
                binding.toolbarHeader.tvTitle.setText(mPayload.getPrimeName());
                fragment = new HomePostFragment();
                fragment.setArguments(bundle);
                break;
            /*case AppConstant.FRAGMENT_HOME_BANK:
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                bundle.putString(HOME_POST_SORT_NEXT_NAV_DETAILS_ID, postDetailsPrimaryTagId);
                bundle.putString(HOME_POST_SORT_NEXT_NAV_DETAILS_DATE, getPostDetailsPrimaryTagDate);
                binding.toolbarHeader.tvTitle.setText(postDetailsPrimaryTag);
                fragment = new BankFragment();
                fragment.setArguments(bundle);
                break;*/
            /*case AppConstant.FRAGMENT_HOME_BANK_POST_SORT_DETAILS:
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                bundle.putString(HOME_POST_SORT_DETAILS_DATA, postPrimaryTagId);
                binding.toolbarHeader.tvTitle.setText(postPrimaryTag);
                fragment = new BanksortFragment();
                fragment.setArguments(bundle);
                break;*/
        }

        //setting fragment
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit);
            fragmentTransaction.replace(binding.containerBody.getId(), fragment);
            fragmentTransaction.commit();
        }

        bottomTabSetting(position);
        settingToolbarIcons(position);
    }

    @Override
    public void onBackPressed() {
        if (fragment != null && fragment instanceof PostDetailFragment) {
            onDrawerItemSelected(null, caApplication.getCurrentSection());
        }else if (fragment != null && fragment instanceof PrimaryOrSecondarySeeAllFragment) {
            onDrawerItemSelected(null, AppConstant.FRAGMENT_GALLERY_Click);
        } else if (fragment != null && fragment instanceof PostFragment) {
            onDrawerItemSelected(null, AppConstant.FRAGMENT_POST_SORT);
        } else if (fragment != null && fragment instanceof GalleryPrimaryOrSecondaryTagSearchList) {
            onDrawerItemSelected(null, section);
        }else if (fragment != null && fragment instanceof PostSortFragment) {
            onDrawerItemSelected(null, section);
        }else if (fragment != null && fragment instanceof HomeSeeAllFragment) {
            onDrawerItemSelected(null, section);
        } else if (fragment != null && fragment instanceof HomePostSortFragment) {
            onDrawerItemSelected(null, AppConstant.FRAGMENT_HOME_SEEALL_POST_DETAILS);
        }else if (fragment != null && fragment instanceof HomePostFragment) {
            onDrawerItemSelected(null, AppConstant.FRAGMENT_HOME_SEEALL_POST_DETAILS_BY_MONTH);
        } else {
           if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
               binding.drawerLayout.closeDrawers();
           }else{
               exitFromApp();
           }

        }
    }

    /**
     * exit from app dialog box
     */
    private void exitFromApp() {
        final Dialog dialog = DialogUtils.createDialog(context, R.layout.dialog_logout);
        TextView tvDialogText = (TextView) dialog.findViewById(R.id.tv_dialog_text);
        TextView tvYes = (TextView) dialog.findViewById(R.id.tv_yes);
        TextView tvNo = (TextView) dialog.findViewById(R.id.tv_no);
        tvDialogText.setText(R.string.exit_alert_dialog_text);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finishAffinity();
            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**
     * logout from app
     */
    private void logoutFromApp() {
        final Dialog dialog = DialogUtils.createDialog(context, R.layout.dialog_logout);
        TextView tvDialogText = (TextView) dialog.findViewById(R.id.tv_dialog_text);
        TextView tvYes = (TextView) dialog.findViewById(R.id.tv_yes);
        TextView tvNo = (TextView) dialog.findViewById(R.id.tv_no);
        tvDialogText.setText(R.string.logout_alert_dialog_text);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                apiLogout(ZODDL_LOGOUT,prefManager.getKeyAuthtoken(), prefManager.getKeyCustomerId());

            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**
     * bottom tab selection
     *
     * @param pos : selected position
     */
    private void bottomTabSetting(int pos) {
        if (pos == AppConstant.FRAGMENT_CAMERA) {
            return;
        }
        if (fragment != null && (fragment instanceof PostFragment || fragment instanceof PostDetailFragment || fragment instanceof PrimaryOrSecondarySeeAllFragment
                || fragment instanceof PostSortFragment || fragment instanceof GalleryPrimaryOrSecondaryTagSearchList || fragment instanceof HomeSeeAllFragment || fragment instanceof HomePostSortFragment|| fragment instanceof HomePostFragment )) {
            return;
        } else {
            binding.layoutFooter.ivHome.setImageResource(R.drawable.ic_home);
            binding.layoutFooter.ivHome.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            binding.layoutFooter.ivGallery.setImageResource(R.drawable.ic_gallery);
            binding.layoutFooter.ivGallery.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            binding.layoutFooter.ivCamera.setImageResource(R.drawable.ic_camera);
            binding.layoutFooter.ivCamera.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            binding.layoutFooter.ivWeb.setImageResource(R.drawable.ic_report);
            binding.layoutFooter.ivWeb.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            binding.layoutFooter.ivDocument.setImageResource(R.drawable.ic_documents);
            binding.layoutFooter.ivDocument.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        switch (pos) {
            case AppConstant.FRAGMENT_HOME:
                binding.layoutFooter.ivHome.setImageResource(R.drawable.home_h);
                binding.layoutFooter.ivHome.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightBlue));
                break;
            case AppConstant.FRAGMENT_GALLERY:
                binding.layoutFooter.ivGallery.setImageResource(R.drawable.picture_h);
                binding.layoutFooter.ivGallery.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightBlue));
                break;
            case AppConstant.FRAGMENT_WEB:
                binding.layoutFooter.ivWeb.setImageResource(R.drawable.chart_h);
                binding.layoutFooter.ivWeb.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightBlue));
                break;
            case AppConstant.FRAGMENT_DOC:
                binding.layoutFooter.ivDocument.setImageResource(R.drawable.note_h);
                binding.layoutFooter.ivDocument.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightBlue));
                break;
        }
    }

    private void settingToolbarIcons(int pos) {
        if (pos == AppConstant.FRAGMENT_CAMERA/* || pos == AppConstant.FRAGMENT_MY_PROFILE*/)
            return;
        binding.toolbarHeader.ivMenu.setImageResource(R.drawable.icon8);
        binding.toolbarHeader.ivGal.setVisibility(View.GONE);
        binding.toolbarHeader.ivShare.setVisibility(View.GONE);
        binding.toolbarHeader.ivDoc.setVisibility(View.GONE);
        binding.toolbarHeader.ivPlus.setVisibility(View.GONE);
        binding.toolbarHeader.ivFilter.setVisibility(View.GONE);
        binding.toolbarHeader.ivRefresh.setVisibility(View.GONE);
        binding.toolbarHeader.ivSearch.setVisibility(View.GONE);
        binding.toolbarHeader.ivCrop.setVisibility(View.GONE);
        binding.toolbarHeader.ivCross.setVisibility(View.GONE);
        binding.toolbarHeader.ivEdit.setVisibility(View.GONE);
        switch (pos) {
            case AppConstant.FRAGMENT_HOME:
                binding.toolbarHeader.ivPlus.setVisibility(View.VISIBLE);
                binding.toolbarHeader.ivSearch.setVisibility(View.VISIBLE);
                break;
            case AppConstant.FRAGMENT_GALLERY:
                binding.toolbarHeader.ivGal.setVisibility(View.VISIBLE);
                binding.toolbarHeader.ivPlus.setVisibility(View.VISIBLE);
                binding.toolbarHeader.ivFilter.setVisibility(View.VISIBLE);
                break;
            case AppConstant.FRAGMENT_WEB:
                binding.toolbarHeader.ivShare.setVisibility(View.VISIBLE);
                binding.toolbarHeader.ivRefresh.setVisibility(View.VISIBLE);
                break;
            case AppConstant.FRAGMENT_DOC:
                binding.toolbarHeader.ivDoc.setVisibility(View.VISIBLE);
                binding.toolbarHeader.ivPlus.setVisibility(View.VISIBLE);
                binding.toolbarHeader.ivFilter.setVisibility(View.VISIBLE);
                break;
            case AppConstant.FRAGMENT_MY_PROFILE:
                binding.toolbarHeader.ivEdit.setVisibility(View.VISIBLE);
                break;

            case AppConstant.FRAGMENT_GALLERY_Click:
            case AppConstant.FRAGMENT_POST_PRIMARY_SECONDARY:
            case AppConstant.FRAGMENT_POST_SORT:
            case AppConstant.FRAGMENT_POST:
            case AppConstant.FRAGMENT_HOME_SEEALL_POST_DETAILS:
            case AppConstant.FRAGMENT_HOME_SEEALL_POST_DETAILS_BY_MONTH:
            case AppConstant.FRAGMENT_HOME_SEEALL_POST_DETAILS_BY_MONTH_DETAILS:
                if (getFragmentName().equalsIgnoreCase(_DOCUMENT)){
                    binding.toolbarHeader.ivDoc.setVisibility(View.VISIBLE);
                }else{
                    binding.toolbarHeader.ivGal.setVisibility(View.VISIBLE);
                }
                binding.toolbarHeader.ivMenu.setImageResource(R.drawable.icon4);
                binding.toolbarHeader.ivPlus.setVisibility(View.VISIBLE);
                binding.toolbarHeader.ivSearch.setVisibility(View.VISIBLE);
                break;

            case AppConstant.FRAGMENT_POST_DETAIL:
                if (getFragmentName().equalsIgnoreCase(_DOCUMENT)){
                    binding.toolbarHeader.ivDoc.setVisibility(View.VISIBLE);
                }else{
                    binding.toolbarHeader.ivGal.setVisibility(View.VISIBLE);
                }
                binding.toolbarHeader.ivMenu.setImageResource(R.drawable.icon4);
                binding.toolbarHeader.ivPlus.setVisibility(View.VISIBLE);
                binding.toolbarHeader.ivEdit.setVisibility(View.VISIBLE);
                break;
            default:
                binding.toolbarHeader.ivPlus.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void apiLogout(String Url,String authToken, String customerId) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("Device_Token", authToken);
        params.put("Customerid", customerId);

        AppController.getInstance().addToRequestQueue(new CustomRequest(Request.Method.POST, Url, params, logoutResponseListener, logoutResponseError), "tag_logout_req");
    }

    Response.Listener<JSONObject> logoutResponseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                progressDialog.hide();
                if (response.getInt("ResponseCode") == 200) {

                    boolean isRem = false;
                    String email="", password="";
                    if (prefManager.getKeyRememberMe()) {
                        isRem = true;
                        email = prefManager.getKeyRememberMeUsername();
                        password = prefManager.getKeyRememberMePassword();
                    }

                    prefManager.setKeyIsLoggedIn(false);
                    prefManager.clearPrefs();

                    if (isRem){
                        prefManager.setKeyRememberMe(true);
                        prefManager.setKeyRememberMeUsername(email);
                        prefManager.setKeyRememberMePassword(password);
                    }
                    MyRoomDatabase db = Room.databaseBuilder(getApplicationContext(), MyRoomDatabase.class,
                            AppConstant.DATABASE_NAME).allowMainThreadQueries().build();

                    db.getDao().deleteAll();
                    db.getMessageDao().deleteAll();
                    db.getPrimaryTagsDao().deleteAll();
                    db.getSecTagsDao().deleteAll();

                    startActivity(new Intent(context, LoginActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finishAffinity();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    Response.ErrorListener logoutResponseError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            progressDialog.hide();
            //Log.d("response", error.toString());


        }
    };



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CheckPermission.RC_CAMERA_PERMISSION) {
            // Received permission result for camera permission.
            LogUtils.infoLog(getTagName(), "Received response for Camera permission request.");
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // onDrawerItemSelected(null, AppConstant.FRAGMENT_CAMERA);
            } else {
            //((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_HOME);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void initiatePopupWindow(View view) {
        RelativeLayout customLayout = (RelativeLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.popup_menu_filter, null);
        final QuickAction quickAction = new QuickAction(context, R.style.PopupAnimation, customLayout, customLayout);
        quickAction.show(view);

        TextView tvNone = (TextView) customLayout.findViewById(R.id.tv_none);
        TextView tvAmount = (TextView) customLayout.findViewById(R.id.tv_amount);
        TextView tvCount = (TextView) customLayout.findViewById(R.id.tv_count);
        TextView tvUses = (TextView) customLayout.findViewById(R.id.tv_uses);
        TextView tvAlphabet = (TextView) customLayout.findViewById(R.id.tv_alphabet);

        tvNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnEventListenerTollbar != null)
                    mOnEventListenerTollbar.onEvent(_BY_NONE,HOME_TOLLBAR_FILLTER_NONE);
                quickAction.dismiss();
            }
        });

        tvAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnEventListenerTollbar != null)
                    mOnEventListenerTollbar.onEvent(_BY_AMOUNT,HOME_TOLLBAR_FILLTER_BY_AMOUNT);
                quickAction.dismiss();
            }
        });

        tvCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnEventListenerTollbar != null)
                    mOnEventListenerTollbar.onEvent(_BY_COUNT,HOME_TOLLBAR_FILLTER_BY_COUNT);
                quickAction.dismiss();
            }
        });

        tvUses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnEventListenerTollbar != null)
                    mOnEventListenerTollbar.onEvent(_BY_USES,HOME_TOLLBAR_FILLTER_BY_USES);
                quickAction.dismiss();
            }
        });

        tvAlphabet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnEventListenerTollbar != null)
                    mOnEventListenerTollbar.onEvent(_BY_ALPHABET,HOME_TOLLBAR_FILLTER_BY_ALPHABET);
                quickAction.dismiss();
            }
        });
    }

    private OnEventListenerTollbar mOnEventListenerTollbar;

    public void setOnEventListenerTollbar(OnEventListenerTollbar listener) {
        mOnEventListenerTollbar = listener;
    }


    @Override
    public String getTagName() {
        return HomeActivity.class.getSimpleName();
    }

    public void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.containerBody.getId(), fragment);
        fragmentTransaction.commit();
    }

    public void changeImageIcon(boolean mTheBoolean) {
        if(mTheBoolean){
            ((ImageView) findViewById(R.id.iv_edit)).setImageResource(R.drawable.ic_check);
        }else{
            ((ImageView) findViewById(R.id.iv_edit)).setImageResource(R.drawable.icon39);
        }
    }

    //for profile
    String profileD;
    public void forProfileData(JSONObject profileData) {
        profileD = profileData.toString();
    }

    String mReportjson;
    public void callReportFragment(String mReportjson) {
        this.mReportjson = mReportjson;
    }

    String mImagesItem;
    public void getImagesData(String mItem){
        mImagesItem = mItem;
    }

    String postPrimaryTag;
    String postPrimaryTagId;
    public void callPostSortFragment(String value,String id) {
        postPrimaryTag = value;
        postPrimaryTagId = id;
    }

    String postDetailsPrimaryTag;
    String postDetailsPrimaryTagId;
    String getPostDetailsPrimaryTagDate;
    public void callPostSortFragment(String value,String id,String date) {
        postDetailsPrimaryTag = value;
        postDetailsPrimaryTagId = id;
        getPostDetailsPrimaryTagDate = date;
    }

    String fromWhere,tagName,tagId;
    public void callForTagClick(String fromWhere,String tagName,String tagId){
        this.fromWhere = fromWhere;
        this.tagName = tagName;
        this.tagId = tagId;
    }

    String fragmentName;
    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String mFragmentName) {
        fragmentName = mFragmentName;
    }

    @Override
    public void onEvent(SycnTags er, int pos) {
        if (mOnEventListenerTollbar != null)
            mOnEventListenerTollbar.onEventForObject(er);
    }

    @Override
    public void onEvent(String status) {
    }

    /************************************FOR HOME SEE ALL CLICKED***********************************/

    String hSeeAllByMonthTagType;
    public void callHomeSortFragment(String type) {
        hSeeAllByMonthTagType = type;
    }

    String hSeeAllByMonthTagId;
    String hSeeAllByMonthSourceType;
    public void callHomeSortFragment(String id,String source) {
        hSeeAllByMonthTagId = id;
        hSeeAllByMonthSourceType = source;
    }

    Payload mPayload;
    public void callPostSortFragment(Payload mPayload) {
        this.mPayload = mPayload;
    }

    /************************************for google drive***********************************/

    public boolean isSignedIn() {
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(context);
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
        return GoogleSignIn.getClient(context, signInOptions);
    }

    private void createDriveClients(GoogleSignInAccount googleSignInAccount) {
        Log.i(TAG, "Update view with sign-in account.");
        // Build a drive client.
        mDriveClient = Drive.getDriveClient(context, googleSignInAccount);
        // Build a drive resource client.
        mDriveResourceClient =
                Drive.getDriveResourceClient(context, googleSignInAccount);
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
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onSuccess(IntentSender intentSender) {
                        try {
                            Bundle mBundle = new Bundle();
                            startIntentSenderForResult(
                                    intentSender,
                                    REQUEST_CODE_OPENER,
                            /* fillInIntent= */ null,
                            /* flagsMask= */ 0,
                            /* flagsValues= */ 0,
                            /* extraFlags= */ 0,
                                    mBundle);
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
        String realPath = DocUtils.writeStreamToFile(context, mDriveContents.getInputStream(), mMetadata.getTitle());

        if (prefManager.getFreeOrPaid())
            showDialog(_DOCUMENT, "", realPath);
        else {
            updateWithoutTagInfoDueToFreeUser(_DOCUMENT,realPath);
        }
    }
    /************************************end google drive***********************************/

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

        if (mOnEventListenerTollbar != null)
            mOnEventListenerTollbar.onEventForObject(obj);

        if (NetworkUtils.isOnline(context))
            ((BaseActivity)context).checkAndStartService();
    }
}
