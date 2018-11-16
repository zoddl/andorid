package com.zoddl.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.zoddl.R;
import com.zoddl.VolleyApiRequest;
import com.zoddl.activities.BaseActivity;
import com.zoddl.activities.DocWebViewActivity;
import com.zoddl.activities.EditPhotoActivity;
import com.zoddl.activities.HomeActivity;
import com.zoddl.database.MyRoomDatabase;
import com.zoddl.databinding.FragmentPostDetailBinding;
import com.zoddl.model.commommodel.SecondaryTag;
import com.zoddl.model.gallery.ImagesItem;
import com.zoddl.model.home.HomePrimaryTagPayload;
import com.zoddl.model.report.ReportSecondaryTag;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.CommonUtils;
import com.zoddl.utils.DatePickerFragment;
import com.zoddl.utils.DialogUtils;
import com.zoddl.utils.ImageUtils;
import com.zoddl.utils.PrefManager;
import com.zoddl.utils.S3FileUploadHelper;
import com.zoddl.interfaces.StringConstant;
import com.zoddl.utils.UrlConstants;
import com.zoddl.utils.compressor.Compressor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.hootsuite.nachos.validator.ChipifyingNachoValidator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.zoddl.activities.EditPhotoActivity.IMAGE_PATH_LOCATION;
import static com.zoddl.activities.EditPhotoActivity.INPUT_URL;
import static com.zoddl.utils.AppConstant.GALLERY_SUBMAIN_ADAPTER_EVENT;
import static com.zoddl.utils.CommonUtils.uniqueName;
import static com.zoddl.utils.UrlConstants.GOOGLE_URL_FOR_OPEN_DOC;

/**
 * Created by Avanish Singh on 11/05/18
 * for Apptology Pvt Ltd..
 */
public class PostDetailFragment extends BaseFragment implements StringConstant{

    private static final int CROP_FROM_CAMERA = 1001;
    private Context context;
    private FragmentPostDetailBinding binding;
    public TextView tv_tag_name;
    public TextView tv_cash;
    public  TextView tv_date;
    public  TextView  tv_price;
    public ImagesItem details;
    private PrefManager prefManager;
    private ProgressDialog progressDialog;

    private List<String> listOfTags;
    private List<String> listOfSecTags;
    private boolean theBoolean = false;

    String tagId;
    private List<String> savedPrimaryTag = new ArrayList<>();
    private ArrayList<String> savedSecondaryTag = new ArrayList<>();

    private MyRoomDatabase db;
    String fragName;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = PrefManager.getInstance(context);
        if (getArguments() != null) {
            tagId = getArguments().getString(GALLERY_SUBMAIN_ADAPTER_EVENT);
            //Log.e("AviName",details.getPrimeName());
        }

        db = Room.databaseBuilder(context, MyRoomDatabase.class, AppConstant.DATABASE_NAME).allowMainThreadQueries().build();

    }

    private void getData() {
        progressDialog = DialogUtils.getProgressDialog(getActivity(), "Please wait . . .");
        progressDialog.show();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        JSONArray mJSONArray = object.getJSONArray("Payload");
                        Gson gson = new Gson();
                        Type type = new TypeToken<ImagesItem>() {
                        }.getType();

                        JSONObject mObject = (JSONObject) mJSONArray.get(0);
                        details = gson.fromJson(mObject.toString(), type);

                        setUpData(details);
                        listener();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //Log.e("error", error.toString());
            }
        };

        String url = UrlConstants.ZODDL_GALLERY_USER_ITEM_DATA;
        String fragName = ((HomeActivity) getActivity()).getFragmentName();
        if (fragName.equalsIgnoreCase(_DOCUMENT)){
            url = UrlConstants.ZODDL_DOC_USER_ITEM_DATA;
        }

        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        params.put("Id",tagId);
        VolleyApiRequest request = new VolleyApiRequest(url, params, stringListener, errorListener);
        Volley.newRequestQueue(getActivity()).add(request);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_post_detail, container, false);
        getData();
        setListeners();
        fragName = ((HomeActivity) getActivity()).getFragmentName();
        return binding.getRoot();
    }


    private void setPrimaryTag() {
        List<String> chips = new ArrayList<>();
        chips.add(details.getPrimeName());
        binding.tvPrimaryTagNamePostDetails.setText(chips);
    }

    private void setSecondaryTag() {
        List<String> chips = new ArrayList<>();
        for (SecondaryTag item: details.getSecondaryTag()){
            chips.add(item.getSecondaryName());
        }
        binding.rvTag2.setText(chips);

    }

    @Override
    public void setListeners() {
        binding.ivShare.setOnClickListener(this);
        binding.ivDownload.setOnClickListener(this);
        binding.ivDelete.setOnClickListener(this);
        binding.imgCrop.setOnClickListener(this);

        setUPEditableData(theBoolean);

        ((HomeActivity)getActivity()).findViewById(R.id.iv_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
                setUPEditableData(theBoolean);
                if(!theBoolean){
                    getDataCurrentView();
                    postUserData();
                }
            }
        });
    }

    private void postUserData() {
        progressDialog = DialogUtils.getProgressDialog(getActivity(), "Please wait . . .");
        progressDialog.show();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        Toast.makeText(context, object.getString("ResponseMessage"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //Log.e("error", error.toString());
            }
        };

        String url = UrlConstants.ZODDL_GALLERY_USER_ITEM_DATA_EDIT;
        fragName = ((HomeActivity) getActivity()).getFragmentName();
        if (fragName.equalsIgnoreCase(_DOCUMENT)){
            url = UrlConstants.ZODDL_DOC_USER_ITEM_DATA_EDIT;
        }

        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        params.put("Id", tagId);

        List<String> listPrimary = binding.tvPrimaryTagNamePostDetails.getChipValues();
        if (listPrimary.size()!=0)
            params.put("Prime_Name",listPrimary.get(0));
        else{
            params.put("Prime_Name","Untagged");
        }


        params.put("Amount", details.getAmount());
        params.put("Tag_Send_Date", details.getTagSendDate());

        List<String> list = binding.rvTag2.getChipValues();
        String json = new Gson().toJson(list);
        params.put("Secondary_Tag", json);

        params.put("Description", details.getDescription());
        params.put("Image_Url", details.getImageUrl());
        params.put("Image_Url_Thumb", details.getImageUrlThumb());
        params.put("CGST", details.getCGST());
        params.put("SGST", details.getSGST());
        params.put("IGST", details.getIGST());

        VolleyApiRequest request = new VolleyApiRequest(url, params, stringListener, errorListener);
        Volley.newRequestQueue(getActivity()).add(request);
    }

    private void toggle() {theBoolean = !theBoolean;}
    private void setUPEditableData(boolean mB) {
        ((HomeActivity)getActivity()).changeImageIcon(theBoolean);


        binding.tvPrice.setFocusableInTouchMode(mB);
        binding.tvPrice.setFocusable(mB);

        binding.tvPrimaryTagNamePostDetails.setFocusableInTouchMode(mB);
        binding.tvPrimaryTagNamePostDetails.setFocusable(mB);
        binding.tvPrimaryTagNamePostDetails.setClickable(mB);

        binding.tvDescriptionPostDetails.setFocusableInTouchMode(mB);
        binding.tvDescriptionPostDetails.setFocusable(mB);

        binding.rvTag2.setFocusableInTouchMode(mB);
        binding.rvTag2.setFocusable(mB);
        binding.rvTag2.setClickable(mB);

        binding.tvCgstPostDetails.setFocusableInTouchMode(mB);
        binding.tvCgstPostDetails.setFocusable(mB);

        binding.tvSgstPostDetails.setFocusableInTouchMode(mB);
        binding.tvSgstPostDetails.setFocusable(mB);

        binding.tvIgstPostDetails.setFocusableInTouchMode(mB);
        binding.tvIgstPostDetails.setFocusable(mB);


    }

    private void getDataCurrentView(){

        details.setTagSendDate(binding.tvDate.getText().toString());
        details.setAmount(binding.tvPrice.getText().toString());

        details.setPrimeName(binding.tvPrimaryTagNamePostDetails.getText().toString());

        String temp = binding.tvDescriptionPostDetails.getText().toString();
        String des = temp.replace("\n", "<br>");

        details.setDescription(des);

        details.setCGST(binding.tvCgstPostDetails.getText().toString());

        details.setSGST(binding.tvSgstPostDetails.getText().toString());

        details.setIGST(binding.tvIgstPostDetails.getText().toString());

        listOfTags = binding.tvPrimaryTagNamePostDetails.getChipValues();
        if (listOfTags.size()!=0){
            boolean isExist;

            for (String l: listOfTags){
                isExist = savedPrimaryTag.contains(l);

                if (!isExist){
                    String time = uniqueName(context);
                    HomePrimaryTagPayload data = new HomePrimaryTagPayload(time,fragName,
                            "",l,"");
                    db.getPrimaryTagsDao().insert(data);
                }

            }

        }

        listOfSecTags = binding.rvTag2.getChipValues();
        if (listOfSecTags.size()!=0){
            boolean isExist;

            for (String l: listOfSecTags){
                isExist = savedSecondaryTag.contains(l);

                if (!isExist){
                    String time = uniqueName(context);
                    ReportSecondaryTag data = new ReportSecondaryTag(time,fragName,
                            "",l,"");

                    db.getSecTagsDao().insertAll(data);
                }

            }
        }

/*
        if(savedPrimaryTag !=null){
            listOfTags = binding.tvPrimaryTagNamePostDetails.getChipValues();
            if (listOfTags.size()!=0){
                savedPrimaryTag.addAll(listOfTags);
                Set<String> valueOfTags = new HashSet<>(savedPrimaryTag);
                prefManager.setKeyOldTagList(valueOfTags);
            }
        }

        if (savedSecondaryTag!=null){
            listOfSecTags = binding.rvTag2.getChipValues();
            if (listOfSecTags.size()!=0){
                savedSecondaryTag.addAll(listOfSecTags);
                Set<String> valueOfTags = new HashSet<>(savedSecondaryTag);
                prefManager.setKeySecOldTagList(valueOfTags);
            }
        }*/

    }

    Bitmap icon;
    private void setUpData(final ImagesItem mDetails) {

        setPrimaryTag();
        setSecondaryTag();

        binding.tvTagName.setText(mDetails.getPrimeName());
        binding.tvDescriptionPostDetails.setText(Html.fromHtml(mDetails.getDescription()));

        binding.tvCash.setText(mDetails.getTagType());

        if (mDetails.getTagType().equalsIgnoreCase(_OTHER)){
            binding.tvCash.setBackground(context.getResources().getDrawable(R.drawable.button_tag_gray));
            binding.tvTagName.setBackground(context.getResources().getDrawable(R.drawable.button_tag_gray));
        }else if (mDetails.getTagType().equalsIgnoreCase(_CASH_PLUS)){
            binding.tvCash.setBackground(context.getResources().getDrawable(R.drawable.button_camera_blue));
            binding.tvTagName.setBackground(context.getResources().getDrawable(R.drawable.button_camera_blue));
        }else if (mDetails.getTagType().equalsIgnoreCase(_CASH_MINUS)){
            binding.tvCash.setBackground(context.getResources().getDrawable(R.drawable.yellow_tag));
            binding.tvTagName.setBackground(context.getResources().getDrawable(R.drawable.yellow_tag));
        }else if (mDetails.getTagType().equalsIgnoreCase(_BANK_PLUS)){
            binding.tvCash.setBackground(context.getResources().getDrawable(R.drawable.button_camera_green));
            binding.tvTagName.setBackground(context.getResources().getDrawable(R.drawable.button_camera_green));
        }else if (mDetails.getTagType().equalsIgnoreCase(_BANK_MINUS)){
            binding.tvCash.setBackground(context.getResources().getDrawable(R.drawable.button_camera_red));
            binding.tvTagName.setBackground(context.getResources().getDrawable(R.drawable.button_camera_red));
        }


        //String newFormattedDate = CommonUtils.checkRangeAndFindFormattedDate();
        binding.tvDate.setText(mDetails.getTagSendDate());

        binding.tvPrice.setText(mDetails.getAmount());

        Glide.with(this)
                .load(mDetails.getImageUrl())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.app_placeholder)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        //binding.postDetailsLibViewer.loadUrl(GOOGLE_URL_FOR_OPEN_DOC +mDetails.getImageUrl());
                        binding.postDetailsLibViewer.getSettings().setJavaScriptEnabled(true); // enable javascript
                        binding.postDetailsLibViewer.setWebViewClient(new WebViewClient() {
                            @SuppressWarnings("deprecation")
                            @Override
                            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                Toast.makeText(context, description, Toast.LENGTH_SHORT).show();
                            }
                            @TargetApi(android.os.Build.VERSION_CODES.M)
                            @Override
                            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                                // Redirect to deprecated method, so you can use it in all SDK versions
                                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                                binding.postDetailsLibViewer.loadUrl("javascript:(function() { " +
                                        "document.querySelector('[role=\"toolbar\"]').remove();})()");
                                binding.progressBar.setVisibility(View.GONE);
                                binding.postDetailsLibViewer.setVisibility(View.VISIBLE);
                            }
                        });

                        binding.postDetailsLibViewer.loadUrl(GOOGLE_URL_FOR_OPEN_DOC+mDetails.getImageUrl());

                        binding.postDetailsLibViewer.setOnTouchListener(new View.OnTouchListener(){

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (event.getAction()==MotionEvent.ACTION_MOVE){
                                    return false;
                                }

                                if (event.getAction()==MotionEvent.ACTION_UP){
                                    Intent mIntent = new Intent(context, DocWebViewActivity.class);
                                    mIntent.putExtra("DocUrl",mDetails.getImageUrl());
                                    startActivity(mIntent);

                                }

                                return false;
                            }
                        });


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
                        binding.progressBar.setVisibility(View.GONE);
                        icon = resource;
                        binding.ivItemImage.setImageBitmap(resource);
                    }
                });

        binding.tvCgstPostDetails.setText(mDetails.getCGST());
        binding.tvSgstPostDetails.setText(mDetails.getSGST());
        binding.tvIgstPostDetails.setText(mDetails.getIGST());
    }


    public void showAlert(String messgae) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Tag: "+details.getPrimeName());
        alertDialog.setMessage(messgae);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                callApiForDeleteThisTag();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
    private void listener() {
        binding.ivItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (icon!=null)
                    editImage();
                else
                    Toast.makeText(context, _IMAGE_STILL_LOADING, Toast.LENGTH_SHORT).show();
            }
        });

        binding.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (icon!=null)
                    shareImage();
                else
                    Toast.makeText(context, _IMAGE_STILL_LOADING, Toast.LENGTH_SHORT).show();
            }
        });

        binding.ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (icon!=null)
                    downloadImage();
                else
                    Toast.makeText(context, _IMAGE_STILL_LOADING, Toast.LENGTH_SHORT).show();
            }
        });

        binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert("Are you sure to delete?");
            }
        });

        setupChipTextViewPrimary(binding.tvPrimaryTagNamePostDetails);

        setupChipTextView(binding.rvTag2);

        binding.tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (theBoolean){
                    DatePickerFragment.getInstance(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                            binding.tvDate.setText(String.format(Locale.getDefault(), "%d-%02d-%02d", year,month + 1,dayOfMonth));
                        }
                    }).show(getChildFragmentManager(), "Dailog");
                }

            }
        });

    }

    private void editImage() {
        //Uri u = CommonUtils.getOutPutMediaFileUri(1);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String filePath = ImageUtils.createImageFileForBitmap(getContext(),bytes);


        /*File direct = new File(Environment.getExternalStorageDirectory() + LOCAL_FOLDER_LOCATION_DATA);
        if(!direct.exists()) {
            direct.mkdir();
        }

        File file = new File(Environment.getExternalStorageDirectory() + LOCAL_FOLDER_LOCATION_DATA+"temp.jpg");

        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

            /*Intent intent = new Intent(getActivity(), CropImage.class);
            intent.putExtra(CropImage.IMAGE_PATH, f.getPath());
            intent.putExtra(CropImage.SCALE, true);
            intent.putExtra(CropImage.ASPECT_X, 1);
            intent.putExtra(CropImage.ASPECT_Y, 1);
            intent.putExtra(CropImage.OUTPUT_X, 600);
            intent.putExtra(CropImage.OUTPUT_Y, 600);
            startActivityForResult(intent, CROP_FROM_CAMERA);*/

            //EditPhotoActivity.start(context,f.getPath());
            Intent intent = new Intent(context,EditPhotoActivity.class);
            intent.putExtra(INPUT_URL,filePath);
            startActivityForResult(intent, CROP_FROM_CAMERA);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CROP_FROM_CAMERA:
                    String path = null;
                    if (intent != null) {
                        path = intent.getStringExtra(IMAGE_PATH_LOCATION);
                    }
                    if (path == null) {
                        return;
                    }

                   // if (prefManager.getEditAnyImage())


                    Glide.with(this)
                            .load(new File(path))
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.app_placeholder)
                            .into(new SimpleTarget<Bitmap>(300,300) {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                    icon = resource;
                                    binding.ivItemImage.setImageBitmap(resource);
                                }
                            });

                    uploadImage(path);
                    break;

            }
        }
    }


    private void callApiForDeleteThisTag() {
        progressDialog = DialogUtils.getProgressDialog(getActivity(), "Please wait . . .");
        progressDialog.show();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        Toast.makeText(context, object.getString("ResponseMessage"), Toast.LENGTH_SHORT).show();
                        ((HomeActivity)getActivity()).onBackPressed();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //Log.e("error", error.toString());
            }
        };

        String url = UrlConstants.ZODDL_GALLERY_USER_ITEM_DATA_DELETE;
        fragName = ((HomeActivity) getActivity()).getFragmentName();
        if (fragName.equalsIgnoreCase(_DOCUMENT)){
            url = UrlConstants.ZODDL_DOC_USER_ITEM_DATA_DELETE;
        }

        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        params.put("Id", tagId);
        VolleyApiRequest request = new VolleyApiRequest(url, params, stringListener, errorListener);
        Volley.newRequestQueue(getActivity()).add(request);
    }


    private void downloadImage() {
        String fileName = CommonUtils.createProfileImageName(getContext(),"jpg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + fileName);
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            Toast.makeText(context, "Download Successfully...", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shareImage() {
        StringBuilder sb = new StringBuilder();
        for (int i=0;i< details.getSecondaryTag().size(); i++){
            if (i == 0)
                sb.append(details.getSecondaryTag().get(i).getSecondaryName());
            else
                sb.append(","+details.getSecondaryTag().get(i).getSecondaryName());
        }

        String temp = details.getDescription();
        String des = temp.replace("<br>", "\n");

        String data = "Primary Tag: "+details.getPrimeName()+"\n"+
                "Secondary Tag: "+sb.toString()+"\n"+
                "Amount: "+details.getAmount()+"\n"+
                "Description: "+des+"\n"+
                "SGST: "+details.getSGST()+"\n"+
                "CGST: "+details.getCGST()+"\n"+
                "IGST: "+details.getIGST();

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String filePath = ImageUtils.createImageFileForBitmap(getContext(),bytes);

       /* File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "temp_share.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            Toast.makeText(context, "Download Successfully...", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
       /* File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File image = File.createTempFile(
                    "temp",  *//* prefix *//*
                    ".jpg",         *//* suffix *//*
                    storageDir      *//* directory *//*
                                            );

            try {
                FileOutputStream fo = new FileOutputStream(image);
                fo.write(bytes.toByteArray());
                Toast.makeText(context, "Download Successfully...", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException mE) {
            mE.printStackTrace();
        }*/

       // String imagePath =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator
        //        + "/temp_share.jpg";

        File imageFileToShare = new File(filePath);

        Uri uri = Uri.fromFile(imageFileToShare);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TEXT,data);

        startActivity(Intent.createChooser(share, "Share Image!"));

    }

    @Override
    public String getTagName() {
        return PostDetailFragment.class.getSimpleName();
    }

    //private static String[] SUGGESTIONS = new String[]{"Office", "Personal", "Other"};
    private void setupChipTextViewPrimary(NachoTextView nachoTextView) {
        ArrayAdapter<String> adapter;
       /* if (prefManager.getKeySecOldTagList()!=null){
            savedPrimaryTag = new ArrayList<>(prefManager.getKeySecOldTagList());
            adapter = new ArrayAdapter<>(context, R.layout.row_spinner_item_field_item, savedPrimaryTag);
            nachoTextView.setAdapter(adapter);
        }else savedPrimaryTag = new ArrayList<>();*/

        List<HomePrimaryTagPayload> secSaveList = db.getPrimaryTagsDao().getAll();
        for (HomePrimaryTagPayload obj: secSaveList){
            savedPrimaryTag.add(obj.getPrime_Name());
        }
        adapter = new ArrayAdapter<>(context, R.layout.row_spinner_item_field_item, savedPrimaryTag);
        nachoTextView.setAdapter(adapter);


        binding.tvPrimaryTagNamePostDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence mCharSequence, int mI, int mI1, int mI2) {}
            @Override
            public void onTextChanged(CharSequence mCharSequence, int mI, int mI1, int mI2) {}
            @Override
            public void afterTextChanged(Editable mEditable) {
                listOfTags = binding.tvPrimaryTagNamePostDetails.getChipValues();
                if (listOfTags.size()>=1){
                    binding.tvPrimaryTagNamePostDetails.dismissDropDown();
                    binding.rvTag2.requestFocus();
                }

                if (listOfTags.size()>1){
                    int s = listOfTags.size();
                    listOfTags.remove(s-1);
                    binding.tvPrimaryTagNamePostDetails.setText(listOfTags);

                }

            }
        });

        nachoTextView.setIllegalCharacters('\"');
        nachoTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        nachoTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        nachoTextView.addChipTerminator(';', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_CURRENT_TOKEN);
        nachoTextView.setNachoValidator(new ChipifyingNachoValidator());
        nachoTextView.enableEditChipOnTouch(true, true);
        nachoTextView.setOnChipClickListener(new NachoTextView.OnChipClickListener() {
            @Override
            public void onChipClick(Chip chip, MotionEvent motionEvent) {
            }
        });
        nachoTextView.setOnChipRemoveListener(new NachoTextView.OnChipRemoveListener() {
            @Override
            public void onChipRemove(Chip chip) {
            }
        });
    }


    //private static String[] SUGGESTIONS = new String[]{"Office", "Personal", "Other"};
    private void setupChipTextView(NachoTextView nachoTextView) {
        ArrayAdapter<String> adapter;
     /*   if (prefManager.getKeySecOldTagList()!=null){
            savedSecondaryTag = new ArrayList<>(prefManager.getKeySecOldTagList());
            adapter = new ArrayAdapter<>(context, R.layout.row_spinner_item_field_item, savedSecondaryTag);
            nachoTextView.setAdapter(adapter);
        }else savedSecondaryTag = new ArrayList<>();
*/

        List<ReportSecondaryTag> secSaveList = db.getSecTagsDao().getAll();
        for (ReportSecondaryTag obj: secSaveList){
            savedSecondaryTag.add(obj.getSecondaryName());
        }
        adapter = new ArrayAdapter<>(context, R.layout.row_spinner_item_field_item, savedSecondaryTag);
        nachoTextView.setAdapter(adapter);

        binding.rvTag2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence mCharSequence, int mI, int mI1, int mI2) {}
            @Override
            public void onTextChanged(CharSequence mCharSequence, int mI, int mI1, int mI2) {}
            @Override
            public void afterTextChanged(Editable mEditable) {
                listOfSecTags = binding.rvTag2.getChipValues();
                if (listOfSecTags.size()>=3){
                    binding.rvTag2.dismissDropDown();
                    binding.tvSgstPostDetails.requestFocus();
                }

                if (listOfSecTags.size()>3){
                    int s = listOfSecTags.size();
                    listOfSecTags.remove(s-1);
                    binding.rvTag2.setText(listOfSecTags);
                    Toast.makeText(context, "Secondary Tag limit is 3.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        nachoTextView.setIllegalCharacters('\"');
        nachoTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        nachoTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        nachoTextView.addChipTerminator(';', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_CURRENT_TOKEN);
        nachoTextView.setNachoValidator(new ChipifyingNachoValidator());
        nachoTextView.enableEditChipOnTouch(true, true);
        nachoTextView.setOnChipClickListener(new NachoTextView.OnChipClickListener() {
            @Override
            public void onChipClick(Chip chip, MotionEvent motionEvent) {
               // Log.d("avi", "onChipClick: " + chip.getText());
            }
        });
        nachoTextView.setOnChipRemoveListener(new NachoTextView.OnChipRemoveListener() {
            @Override
            public void onChipRemove(Chip chip) {
                //Log.d("avi", "onChipRemoved: " + chip.getText());

            }
        });
    }


    private synchronized void uploadImage(final String path) {
        String fileName = details.getImageUrl().substring(details.getImageUrl().lastIndexOf('/') + 1);
        progressDialog.show();
        S3FileUploadHelper transferHelper = new S3FileUploadHelper(context);
        transferHelper.upload(path,fileName);
        transferHelper.setFileTransferListener(new S3FileUploadHelper.FileTransferListener() {
            @Override
            public void onSuccess(int id, TransferState state, String fileName) {
               // Log.d("S3FileUpload1", String.format("%s uploaded successfully!", fileName));

                progressDialog.dismiss();
                try {
                    File yourFile = new File(path);
                    try {
                        File compressedImageFile = new Compressor(context).compressToFile(yourFile);
                        uploadImageThumbnail(compressedImageFile,fileName);
                    } catch (IOException mE) {
                        mE.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //uploadImageThumbnail(path,fileName);
            }
            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;
               // Log.d("S3FileUpload1", "ID:" + id + " bytesCurrent: " + bytesCurrent + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }
            @Override
            public void onError(int id, Exception ex) {
                progressDialog.dismiss();
            }
        });
    }

    private synchronized void uploadImageThumbnail(File path,String originalFileName) {
        progressDialog.show();
        String fileN = "thumb_"+originalFileName;
        S3FileUploadHelper transferHelper = new S3FileUploadHelper(context);
        transferHelper.upload(path,fileN);
        transferHelper.setFileTransferListener(new S3FileUploadHelper.FileTransferListener() {
            @Override
            public void onSuccess(int id, TransferState state, String fileName) {
                //Log.d("S3FileUpload2", String.format("%s uploaded successfully!", fileName));

                assert ((BaseActivity)getActivity()) != null;
                ((BaseActivity)getActivity()).clearGlideDiskCache();

                progressDialog.dismiss();
            }
            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            }
            @Override
            public void onError(int id, Exception ex) {
                progressDialog.dismiss();
            }
        });
    }

    /*public void clearGlideDiskCache()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Glide.get(context.getApplicationContext()).clearDiskCache();
            }
        }).start();

        // Glide.get(getApplicationContext()).clearMemory();
    }*/

    private synchronized void uploadImageThumbnail(File mFile) {
        String fileName = details.getImageUrlThumb().substring(details.getImageUrlThumb().lastIndexOf('/') + 1);

        S3FileUploadHelper transferHelper = new S3FileUploadHelper(context);
        //transferHelper.upload(obj.getImageUrl(), CommonUtils.createProfileImageName(this));
        transferHelper.upload(mFile,fileName);
        transferHelper.setFileTransferListener(new S3FileUploadHelper.FileTransferListener() {
            @Override
            public void onSuccess(int id, TransferState state, String fileName) {
               // Log.d("S3FileUpload2", String.format("%s uploaded successfully!", fileName));

            }
            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;
                //Log.d("S3FileUpload2", "ID:" + id + " bytesCurrent: " + bytesCurrent + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }
            @Override
            public void onError(int id, Exception ex) {
            }
        });
    }

    @Override
    public void onClick(View v) {}
}
