package com.zoddl.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zoddl.R;
import com.zoddl.VolleyApiRequest;
import com.zoddl.activities.HomeActivity;
import com.zoddl.adapters.report.ReportTagAdapter1;
import com.zoddl.adapters.report.ReportTagAdapter2;
import com.zoddl.database.SycnTags;
import com.zoddl.databinding.FragmentReportBinding;
import com.zoddl.interfaces.OnEventListener;
import com.zoddl.interfaces.OnEventListenerTollbar;
import com.zoddl.model.report.ReportPayload;
import com.zoddl.model.report.ReportPrimaryTag;
import com.zoddl.model.report.ReportSecondaryTag;
import com.zoddl.utils.CommonUtils;
import com.zoddl.utils.NetworkUtils;
import com.zoddl.utils.PrefManager;
import com.zoddl.interfaces.StringConstant;
import com.zoddl.utils.UrlConstants;
//import com.github.mikephil.charting.formatter.IAxisValueFormatter;
//import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.zoddl.utils.AppConstant.HOME_SENDING_REPORT_AT_HOME;
import static com.zoddl.utils.AppConstant.HOME_TOLLBAR_REPORT_REFRESH_BUTTON;
import static com.zoddl.utils.AppConstant.HOME_TOLLBAR_REPORT_SHARE_BUTTON;

/**
 * Created by Abhishek Tiwari on 10/7/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */
public class ReportFragment extends BaseFragment implements OnEventListenerTollbar, StringConstant, OnEventListener {

    private Context context;
    private FragmentReportBinding binding;
    private PrefManager prefManager;

    ArrayList<ReportPrimaryTag> primarytagtype = new ArrayList<>();
    ArrayList<ReportSecondaryTag> secondarytagtype = new ArrayList<>();

    ArrayList<ReportPayload> payload = new ArrayList<>();

    ReportTagAdapter1 reportTagAdapter1;
    ReportTagAdapter2 reportTagAdapter2;
    ArrayList<String> staticList = new ArrayList<>();

    //for intent
    private String jsonData;
    private JSONObject mIntentJsonObject;

    //for prepare json data for api call
    int selectedItem = 0;
    JSONArray mJSONArray = new JSONArray();
    JSONObject mJSONObject = new JSONObject();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert ((HomeActivity) getActivity()) != null;
        ((HomeActivity) getActivity()).setOnEventListenerTollbar(ReportFragment.this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_report, container, false);
        prefManager = PrefManager.getInstance(context);

        setRecyclerView();
        setListeners();

        getprimarytag();


        return binding.getRoot();
    }

    private void setUpArgumentedDataRequest() {

        if (getArguments() != null) {
            jsonData = getArguments().getString(HOME_SENDING_REPORT_AT_HOME);
            try {
                mIntentJsonObject = new JSONObject(jsonData);
                //Toast.makeText(context, "In", Toast.LENGTH_SHORT).show();
                try {
                    setUpIntentDataForApiCall();
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
            } catch (JSONException mE) {
                mE.printStackTrace();
            }

        }
    }

    private void setUpIntentDataForApiCall() throws JSONException {
        JSONArray mArray = mIntentJsonObject.getJSONArray("reportjson");
        JSONObject rJsonArrayObj = mArray.getJSONObject(0);

        try {
            Iterator<String> keys = rJsonArrayObj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject innerJObject = rJsonArrayObj.getJSONObject(key);
                //mJSONArray.put(innerJObject);
                mJSONObject.put(key, innerJObject);

                for (ReportPrimaryTag obj : primarytagtype) {
                    if (innerJObject.getString("Prime_Name").equalsIgnoreCase(obj.getPrimeName())) {
                        obj.setSelected(true);
                        selectedItem++;
                        //setUpPrimeDataForApiCallViaIntent(innerJObject);
                    }
                }

                for (ReportSecondaryTag obj : secondarytagtype) {
                    if (innerJObject.getString("Prime_Name").equalsIgnoreCase(obj.getSecondaryName())) {
                        obj.setSelected(true);
                        selectedItem++;
                        //setUpSecDataForApiCallViaIntent(obj);
                    }
                }

            }

            reportTagAdapter1.notifyDataSetChanged();
            reportTagAdapter2.notifyDataSetChanged();

        } catch (JSONException mE) {
            mE.printStackTrace();
        }


        //String requestData = "[" + rJsonArrayObj.toString() + "]";
        //callServierForReport(false, requestData);
        callServierForReport(false);
    }

    private void setRecyclerView() {

        reportTagAdapter1 = new ReportTagAdapter1(getContext(), primarytagtype);
        binding.rvTag1.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        binding.rvTag1.setAdapter(reportTagAdapter1);
        reportTagAdapter1.setOnEventListenerTollbar(this);
        binding.rvTag1.setNestedScrollingEnabled(false);

        reportTagAdapter1.setOnEventListenerTollbar(new OnEventListener() {
            @Override
            public void onEvent(int pos, View v) {
                if (selectedItem < 5) {
                    selectedItem++;
                    primarytagtype.get(pos).setSelected(true);
                    v.setVisibility(View.VISIBLE);

                    setUpPrimeDataForApiCall(pos);
                } else {
                    showAlertDialog("Alert!", "You can't select more then five.");
                }
            }
        });

        reportTagAdapter2 = new ReportTagAdapter2(getContext(), secondarytagtype);
        binding.rvTag2.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        reportTagAdapter2.setOnEventListenerTollbar(this);
        binding.rvTag2.setAdapter(reportTagAdapter2);
        binding.rvTag2.setNestedScrollingEnabled(false);

        reportTagAdapter2.setOnEventListenerTollbar(new OnEventListener() {
            @Override
            public void onEvent(int pos, View v) {
                if (selectedItem < 5) {
                    selectedItem++;
                    secondarytagtype.get(pos).setSelected(true);
                    v.setVisibility(View.VISIBLE);
                    setUpSecDataForApiCall(pos);

                } else {
                    showAlertDialog("Alert!", "You can't select more then five.");
                }

            }
        });

    }


    private void setUpPrimeDataForApiCall(int mPos) {
        String column = "column" + selectedItem;
        if (primarytagtype.get(mPos).getId() != null) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("Tag_Type", primarytagtype.get(mPos).getTagType());
                obj.put("Id", primarytagtype.get(mPos).getId());
                obj.put("Source_Type", primarytagtype.get(mPos).getSourceType());
                mJSONObject.put(column, obj);
            } catch (JSONException e) {
            }
        } else {
            JSONObject obj = new JSONObject();
            try {
                obj.put("Tag_Type", "master");
                obj.put("Id", primarytagtype.get(mPos).getPrimeName());
                obj.put("Source_Type", "universal");
                mJSONObject.put(column, obj);
            } catch (JSONException e) {
            }
        }

        callServierForReport(false);

    }

    private void setUpSecDataForApiCall(int mPos) {
        String column = "column" + selectedItem;
        if (secondarytagtype.get(mPos).getId() != null) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("Tag_Type", secondarytagtype.get(mPos).getTag_Type());
                obj.put("Id", secondarytagtype.get(mPos).getId());
                obj.put("Source_Type", secondarytagtype.get(mPos).getSourceType());
                mJSONObject.put(column, obj);
            } catch (JSONException e) {
            }
        }

        callServierForReport(false);

    }

    private void callServierForReport(final boolean isSave) {

        if (payload.size() != 0) {
            payload.clear();
        }
        if (mJSONArray.length()!=0){
            mJSONArray = new JSONArray(new ArrayList<String>());
        }
        String mRequestData;
        mJSONArray.put(mJSONObject);
        mRequestData = mJSONArray.toString();

        if (!NetworkUtils.isOnline(context)) {
            Toast.makeText(context, context.getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            return;
        }

        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        payload.addAll(VolleyApiRequest.parseReport(object.getJSONArray("Payload")));
                        binding.webReport.setVisibility(View.VISIBLE);
                        binding.webReport.loadDataWithBaseURL(null, payload.get(0).getHtml(), "text/html", "UTF-8", null);

                        if (isSave){
                            //if (selectedItem>=5)
                                refreshPrimaryAndSecondary();
                        }

                    }
                    if (object.getInt("ResponseCode") == 400) {
                        showAlertDialog("Alert!", object.getString("ResponseMessage"));
                    }
                } catch (Exception e) {
                    //progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressDialog.dismiss();
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        params.put("reportjson", mRequestData);
        if (isSave)
            params.put("savereport", "1");

        VolleyApiRequest request = new VolleyApiRequest(UrlConstants.ZODDL_REPORT_REPORT_DATA, params, stringListener, errorListener);
        Volley.newRequestQueue(context).add(request);
    }

    private void refreshPrimaryAndSecondary() {

        for (ReportPrimaryTag primaryTag: primarytagtype){
            primaryTag.setSelected(false);
        }
        for (ReportSecondaryTag secondaryTag: secondarytagtype){
            secondaryTag.setSelected(false);
        }

        reportTagAdapter1.notifyDataSetChanged();
        reportTagAdapter2.notifyDataSetChanged();
        selectedItem = 0;
        mJSONArray = new JSONArray(new ArrayList<String>());
        mJSONObject = new JSONObject();

    }

    public void getprimarytag() {
        if (primarytagtype.size() != 0) {
            primarytagtype.clear();
        }
        if (!NetworkUtils.isOnline(context)) {
            Toast.makeText(context, context.getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            return;
        }

        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        primarytagtype.addAll(VolleyApiRequest.parseReportPrimaryList(object.getJSONArray("Primary_Tag")));
                        //Toast.makeText(context, primarytagtype.size()+"", Toast.LENGTH_SHORT).show();
                        staticListSetup();
                        reportTagAdapter1.notifyDataSetChanged();

                        getsecondarytag();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };

        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        VolleyApiRequest request = new VolleyApiRequest(UrlConstants.ZODDL_REPORT_PRIMARY_TAG, params, stringListener, errorListener);
        Volley.newRequestQueue(context).add(request);
    }

    private void staticListSetup() {

        staticList.add("other");
        staticList.add("bank-");
        staticList.add("bank+");
        staticList.add("cash-");
        staticList.add("cash+");
        staticList.add("amount");
        staticList.add("count");
        staticList.add("day");
        staticList.add("all");
        staticList.add("month");
        staticList.add("year");

        for (String item : staticList) {
            ReportPrimaryTag obj = new ReportPrimaryTag(item);
            primarytagtype.add(0, obj);
        }
    }

    public void getsecondarytag() {
        if (secondarytagtype.size() != 0) {
            secondarytagtype.clear();
        }

        if (!NetworkUtils.isOnline(context)) {
            Toast.makeText(context, context.getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            return;
        }

        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        secondarytagtype.addAll(VolleyApiRequest.parseReportSecondaryList(object.getJSONArray("Secondary_Tag")));
                        reportTagAdapter2.notifyDataSetChanged();


                        setUpArgumentedDataRequest();
                    }
                } catch (Exception e) {
                    //progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressDialog.dismiss();
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        VolleyApiRequest request = new VolleyApiRequest(UrlConstants.ZODDL_REPORT_SECONDARY_TAG, params, stringListener, errorListener);
        Volley.newRequestQueue(context).add(request);
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public String getTagName() {
        return ReportFragment.class.getSimpleName();
    }

    @Override
    public void onEvent(String er, int pos) {
        switch (pos) {
            case HOME_TOLLBAR_REPORT_REFRESH_BUTTON:
                if (mJSONArray.length() != 0)
                    callServierForReport(true);

                //getprimarytag();
                //getsecondarytag();
                //binding.webReport.setVisibility(View.GONE);
                //binding.reportStatus.setText("Report Generated");
                //((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_WEB);
                break;
            case HOME_TOLLBAR_REPORT_SHARE_BUTTON:
                if (mJSONArray.length() != 0)
                    takeScreenShotForShare();
                else
                    showAlertDialog("Alert!", "Please generate report first.");
                break;
        }
    }

    @Override
    public void onEventForObject(SycnTags obj) {

    }

    private void takeScreenShotForShare() {

        Bitmap bitmap = CommonUtils.loadBitmapFromView(binding.flWebWebviewActivity);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        String path = storageDir.getAbsolutePath();

        String targetPdf = path + "/share_report.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));

        } catch (IOException e) {
            e.printStackTrace();
        }

        shareIntent();
    }

    private void shareIntent() {

        File outputFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "share_report.pdf");
        Uri uri = Uri.fromFile(outputFile);

        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(share, "Share Image!"));
    }

    @Override
    public void onEvent(int val, View v) {

    }
}
