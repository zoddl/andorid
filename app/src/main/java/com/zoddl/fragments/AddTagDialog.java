package com.zoddl.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.zoddl.R;
import com.zoddl.database.MyRoomDatabase;
import com.zoddl.database.SycnTags;
import com.zoddl.databinding.DialogAddTagBinding;
import com.zoddl.model.home.HomePrimaryTagPayload;
import com.zoddl.model.report.ReportSecondaryTag;
import com.zoddl.utils.AppConstant;
import com.zoddl.activities.BaseActivity;
import com.zoddl.utils.CommonUtils;
import com.zoddl.utils.DatePickerFragment;
import com.zoddl.utils.ImageUtils;
import com.zoddl.utils.NetworkUtils;
import com.zoddl.utils.PrefManager;
import com.zoddl.interfaces.StringConstant;
import com.google.gson.Gson;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.hootsuite.nachos.validator.ChipifyingNachoValidator;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Date;
import java.text.SimpleDateFormat;

import static com.zoddl.utils.CommonUtils.uniqueName;

/**
 * Created by avanish on 2/5/18.
 */

public class AddTagDialog extends DialogFragment implements StringConstant {

    public static final String ARGS_DIALOG_CALLER_TYPE = "ARGS_DIALOG_CALLER_TYPE";

    public static final String ARGS_DIALOG_FLOW_NAME = "ARGS_DIALOG_FLOW_NAME";
    public static final String ARGS_DIALOG_IMAGE_PATH = "ARGS_DIALOG_IMAGE_PATH";

    private Context context;
    private PrefManager prefManager;

    private View rootView;
    private DialogAddTagBinding binding;

    private List<String> savedPrimaryTag = new ArrayList<>();
    private List<String> savedSecondaryTag = new ArrayList<>();

    private List<String> listOfSecTags;

    private String callerType,imageOrFileUrl;
    private String flowName = "";

    MyRoomDatabase db;
    public static AddTagDialog newInstance() {
        return new AddTagDialog();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = context;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager=PrefManager.getInstance(context);
        if (getArguments() != null) {
            callerType = getArguments().getString(ARGS_DIALOG_CALLER_TYPE,"");
            flowName = getArguments().getString(ARGS_DIALOG_FLOW_NAME,"");
            imageOrFileUrl = getArguments().getString(ARGS_DIALOG_IMAGE_PATH,"");
        }

        db = Room.databaseBuilder(context, MyRoomDatabase.class, AppConstant.DATABASE_NAME).allowMainThreadQueries().build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.dialog_add_tag, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_tag, container, false);
        try {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        } catch (Throwable mE) {
            mE.printStackTrace();
        }

        listener();
        rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (callerType.equalsIgnoreCase(_CAMERA)){
            if (flowName.equalsIgnoreCase(_OTHER)){
                binding.flowName.setText(flowName);
                binding.flowName.setBackgroundColor(getResources().getColor(R.color.colorGray));
            }else if (flowName.equalsIgnoreCase(_CASH_PLUS)){
                binding.flowName.setText(flowName);
                binding.flowName.setBackgroundColor(getResources().getColor(R.color.colorCamBlue));
            }else if (flowName.equalsIgnoreCase(_CASH_MINUS)){
                binding.flowName.setText(flowName);
                binding.flowName.setBackgroundColor(getResources().getColor(R.color.colorCamYellow));
            }else if (flowName.equalsIgnoreCase(_BANK_PLUS)){
                binding.flowName.setText(flowName);
                binding.flowName.setBackgroundColor(getResources().getColor(R.color.colorCamGreen));
            }else if (flowName.equalsIgnoreCase(_BANK_MINUS)){
                binding.flowName.setText(flowName);
                binding.flowName.setBackgroundColor(getResources().getColor(R.color.colorCamRed));
            }
        } else{
            binding.cashBankFlowLayoutHeader.setVisibility(View.VISIBLE);
            binding.tvDialogText.setText("Add Manual Tag");
            binding.flowName.setVisibility(View.GONE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        binding.tvDate.setText(date);
    }

    private boolean checkValidation() {
        if (flowName.equalsIgnoreCase("")) {
            showAlertDialog("Please select tag type.");
            return false;
        }else {
            return true;
        }


    }

    public void showAlertDialog(String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setCancelable(false);

        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,getContext().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        if (!((Activity) getContext()).isFinishing()) {

            alertDialog.show();
        }

    }

    private void listener() {

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkValidation()) {

                    SycnTags obj = getSycnTagsData();
                    db.getDao().insertAll(obj);

                        if (binding.etTagName.getText().toString().length()!=0){
                            String pName = binding.etTagName.getText().toString();
                            boolean isExist = false;

                            for (String hData : savedPrimaryTag){
                                if (pName.equalsIgnoreCase(hData)){
                                    isExist = true;
                                }
                            }

                            if (!isExist){
                                String time = uniqueName(context);
                                HomePrimaryTagPayload data = new HomePrimaryTagPayload(time,obj.getTypeOfFile(),
                                        "",pName,"");
                                db.getPrimaryTagsDao().insert(data);
                            }


                        }

                        listOfSecTags = binding.etSecTabName.getChipValues();
                        if (listOfSecTags.size()!=0){
                            boolean isExist;

                            for (String l: listOfSecTags){
                                isExist = savedSecondaryTag.contains(l);

                                if (!isExist){
                                    String time = uniqueName(context);
                                    ReportSecondaryTag data = new ReportSecondaryTag(time,obj.getTypeOfFile(),
                                            "",l,"");

                                    db.getSecTagsDao().insertAll(data);
                                }

                            }
                        }


                    if (mOnEventListener != null)
                        mOnEventListener.onEvent(obj,1);

                    if (mOnEventListener != null)
                        mOnEventListener.onEvent("Cancle");

                    if (NetworkUtils.isOnline(context))
                        ((BaseActivity)getActivity()).checkAndStartService();

                    dismiss();
                }


            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnEventListener != null)
                    mOnEventListener.onEvent("Cancle");
                dismiss();
            }
        });

        binding.btnCashPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TEXT ALIGNMENT RIGHT AFTER CLICK
                binding.btnCashPlus.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                binding.btnCashMins.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                binding.btnBankPlus.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                binding.btnBankMins.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                binding.btnOther.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                //IMAGE VISIBLE AFETR CLICK ON BUTTON
                binding.imgCashPlus.setVisibility(View.VISIBLE);
                binding.imgCashMins.setVisibility(View.INVISIBLE);
                binding.imgBankPlus.setVisibility(View.INVISIBLE);
                binding.imgBankMins.setVisibility(View.INVISIBLE);
                binding.imgOther.setVisibility(View.INVISIBLE);

                flowName = _CASH_PLUS;

            }
        });

        binding.btnCashMins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TEXT ALIGNMENT RIGHT AFTER CLICK
                binding.btnCashPlus.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                binding.btnCashMins.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                binding.btnBankPlus.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                binding.btnBankMins.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                binding.btnOther.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                //IMAGE VISIBLE AFETR CLICK ON BUTTON
                binding.imgCashPlus.setVisibility(View.INVISIBLE);
                binding.imgCashMins.setVisibility(View.VISIBLE);
                binding.imgBankPlus.setVisibility(View.INVISIBLE);
                binding.imgBankMins.setVisibility(View.INVISIBLE);
                binding.imgOther.setVisibility(View.INVISIBLE);

                flowName = _CASH_MINUS;

            }
        });

        binding.btnBankPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TEXT ALIGNMENT RIGHT AFTER CLICK
                binding.btnCashPlus.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                binding.btnCashMins.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                binding.btnBankPlus.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                binding.btnBankMins.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                binding.btnOther.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                //IMAGE VISIBLE AFETR CLICK ON BUTTON
                binding.imgCashPlus.setVisibility(View.INVISIBLE);
                binding.imgCashMins.setVisibility(View.INVISIBLE);
                binding.imgBankPlus.setVisibility(View.VISIBLE);
                binding.imgBankMins.setVisibility(View.INVISIBLE);
                binding.imgOther.setVisibility(View.INVISIBLE);

                flowName = _BANK_PLUS;
            }
        });

        binding.btnBankMins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //TEXT ALIGNMENT RIGHT AFTER CLICK
                binding.btnCashPlus.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                binding.btnCashMins.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                binding.btnBankPlus.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                binding.btnBankMins.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                binding.btnOther.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                //IMAGE VISIBLE AFETR CLICK ON BUTTON
                binding.imgCashPlus.setVisibility(View.INVISIBLE);
                binding.imgCashMins.setVisibility(View.INVISIBLE);
                binding.imgBankPlus.setVisibility(View.INVISIBLE);
                binding.imgBankMins.setVisibility(View.VISIBLE);
                binding.imgOther.setVisibility(View.INVISIBLE);

                flowName = _BANK_MINUS;
            }
        });

        binding.btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TEXT ALIGNMENT RIGHT AFTER CLICK
                binding.btnCashPlus.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                binding.btnCashMins.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                binding.btnBankPlus.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                binding.btnBankMins.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                binding.btnOther.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                //IMAGE VISIBLE AFETR CLICK ON BUTTON
                binding.imgCashPlus.setVisibility(View.INVISIBLE);
                binding.imgCashMins.setVisibility(View.INVISIBLE);
                binding.imgBankPlus.setVisibility(View.INVISIBLE);
                binding.imgBankMins.setVisibility(View.INVISIBLE);
                binding.imgOther.setVisibility(View.VISIBLE);

                flowName = _OTHER;
            }
        });


        setupChipTextView(binding.etSecTabName);

        List<HomePrimaryTagPayload> homeList = db.getPrimaryTagsDao().getAll();
        for (HomePrimaryTagPayload ob: homeList){
            savedPrimaryTag.add(ob.getPrime_Name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.row_spinner_item_field_item, savedPrimaryTag);
        binding.etTagName.setAdapter(adapter);

        binding.tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerFragment.getInstance(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        binding.tvDate.setText(String.format(Locale.getDefault(), "%d-%02d-%02d", year,month + 1,dayOfMonth));
                    }
                }).show(getChildFragmentManager(), "Dailog");
            }
        });

    }
    @NonNull
    private SycnTags getSycnTagsData() {
        String primaryTag = binding.etTagName.getText().toString();

        List<String> list = binding.etSecTabName.getChipValues();
        String secTag = new Gson().toJson(list);

        String amount = binding.etAmount.getText().toString();
        String date = binding.tvDate.getText().toString();

        String temp = binding.etDescription.getText().toString();
        String dispcription = temp.replace("\n", "<br>");

        /*File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String fileName = CommonUtils.createProfileImageNameWithoutExtention(getActivity());

        File image = null;
        FileOutputStream fo;
        try {
            image = File.createTempFile(
                      *//* prefix *//*fileName,
                    _IMAGE_EXTENSION_JPG,         *//* suffix *//*
                    storageDir      *//* directory *//*
                                            );

            try {
                fo = new FileOutputStream(image);
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException mE) {
            mE.printStackTrace();
        }*/


        String autoManual,imageUrl,docUrl,fileType;
        if (callerType.equalsIgnoreCase(_CAMERA)||callerType.equalsIgnoreCase(_GALLERY)){
            autoManual = _AUTO;
            imageUrl = imageOrFileUrl;
            docUrl = "";
            fileType = _GALLERY;
        }else if (callerType.equalsIgnoreCase(_DOCUMENT)){
            autoManual = _AUTO;
            imageUrl = imageOrFileUrl;
            docUrl = imageOrFileUrl;
            fileType = _DOCUMENT;
        }else{
            Bitmap bitmap = CommonUtils.loadBitmapFromView(binding.parentAddTagView);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String filePath = ImageUtils.createImageFileForBitmap(getContext(),bytes);
            autoManual = _MANUAL;

            imageUrl = filePath;
            docUrl = filePath;

            if (callerType.equalsIgnoreCase(_MANUAL_GALLERY)){
                fileType = _GALLERY;
            }else{
                fileType = _DOCUMENT;
            }

        }


        return new SycnTags(primaryTag, secTag, _TAG_STATUS, amount, date, dispcription, flowName, imageUrl, imageUrl, docUrl, autoManual,"pending",fileType);
    }

   // private static String[] SUGGESTIONS = new String[]{""};
    ArrayAdapter<String> adapter;
    private void setupChipTextView(final NachoTextView nachoTextView) {

        //binding.etSecTabName.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        //binding.etSecTabName.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);

        List<ReportSecondaryTag> secSaveList = db.getSecTagsDao().getAll();
        for (ReportSecondaryTag obj: secSaveList){
            savedSecondaryTag.add(obj.getSecondaryName());
        }
        adapter = new ArrayAdapter<>(context, R.layout.row_spinner_item_field_item, savedSecondaryTag);
        nachoTextView.setAdapter(adapter);

        /*else {
            adapter = new ArrayAdapter<>(context, R.layout.row_spinner_item_field_item, SUGGESTIONS);
            nachoTextView.setAdapter(adapter);
            savedSecondaryTag = new ArrayList<>();
        }*/

       /* nachoTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    nachoTextView.showDropDown();

            }
        });*/


        nachoTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence mCharSequence, int mI, int mI1, int mI2) {
            }
            @Override
            public void onTextChanged(CharSequence mCharSequence, int mI, int mI1, int mI2) {
            }
            @Override
            public void afterTextChanged(Editable mEditable) {

                listOfSecTags = nachoTextView.getChipValues();
                if (listOfSecTags.size()>=3){
                    nachoTextView.dismissDropDown();
                    binding.etAmount.requestFocus();
                }

                if (listOfSecTags.size()>3){
                    int s = listOfSecTags.size();
                    listOfSecTags.remove(s-1);
                    nachoTextView.setText(listOfSecTags);
                    binding.etAmount.requestFocus();
                    Toast.makeText(context, _SECONDARY_LIMITS, Toast.LENGTH_SHORT).show();

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

    public static OnEventListener mOnEventListener;

    public static void setOnEventListener(OnEventListener listener) {
        mOnEventListener = listener;
    }

    public interface OnEventListener {
        void onEvent(SycnTags er,int pos);
        void onEvent(String status);
    }

}
