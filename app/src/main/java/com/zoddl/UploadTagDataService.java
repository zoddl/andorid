package com.zoddl;

import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zoddl.database.MyRoomDatabase;
import com.zoddl.database.SycnTags;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.CommonUtils;
import com.zoddl.utils.PrefManager;
import com.zoddl.utils.S3FileUploadHelper;
import com.zoddl.utils.UrlConstants;
import com.zoddl.utils.compressor.Compressor;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.zoddl.interfaces.StringConstant._DOCUMENT;
import static com.zoddl.interfaces.StringConstant._GALLERY;
import static com.zoddl.utils.UrlConstants.S3_BUCKET_DEV_URL;

public class UploadTagDataService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    public static boolean isServiceRunning;
    private static final String TAG = "UploadTagDataService";

    private MyRoomDatabase db;
    private Bundle bundle;
    private ResultReceiver receiver;
    private PrefManager prefManager;

    public UploadTagDataService() { super("UploadTagDataService"); }

    @Override
    public void onCreate() {
        super.onCreate();
        db = Room.databaseBuilder(getApplicationContext(), MyRoomDatabase.class, AppConstant.DATABASE_NAME).allowMainThreadQueries().build();
        prefManager = PrefManager.getInstance(this);
    }
    List<SycnTags> mObj;
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service Started!");

        receiver = intent.getParcelableExtra("receiver");
        int requestId = intent.getIntExtra("requestId",0);

        bundle = new Bundle();

        if (requestId!=0) {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);
            isServiceRunning = true;
            try {
                mObj = db.getDao().getAll();
                loopForDBRecursively();

            } catch (Exception e) {
                /* Sending error message back to activity */
                isServiceRunning = false;
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
        }
        this.stopSelf();
    }

    int count = 0;
    String docEx;
    private void loopForDBRecursively() {
        docEx = "";
        if (count<mObj.size()){
            if (mObj.get(count).getTypeOfFile().equalsIgnoreCase(_GALLERY) && validateImage(mObj.get(count).getImageUrl())){
                    docEx = findDocExe(mObj.get(count).getImageUrl());
                    uploadImage(mObj.get(count));
            } else if (mObj.get(count).getTypeOfFile().equalsIgnoreCase(_DOCUMENT) && mObj.get(count).getDocumentUrl().length()!=0){
                    docEx = findDocExe(mObj.get(count).getDocumentUrl());
                    uploadImage(mObj.get(count));
            }else{
                addTags(mObj.get(count),"","");
            }


            count++;
            return;
        }

        bundle.putString("result", "Success");
        receiver.send(STATUS_FINISHED, bundle);
        isServiceRunning = false;
        Log.d(TAG, "Service Stopping!");

    }

    boolean validateImage(String fileName){
        String extension = null;
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        if (extension!=null)
            if(extension.equalsIgnoreCase("jpg") ||extension.equalsIgnoreCase("JPEG")
                    ||extension.equalsIgnoreCase("png")||extension.equalsIgnoreCase("BMP")){
                return true;
            }

        return false;
    }

    String findDocExe(String fileName){
        String extension = null;
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        if (extension!=null)
           return extension;

        return "";
    }

    private synchronized void uploadImage(final SycnTags obj) {
        String imgUrl = "";
        String imageName = "";

        imgUrl = obj.getImageUrl();
        if (imgUrl.equalsIgnoreCase("")){
            imgUrl = obj.getDocumentUrl();
        }

        File f = new File(imgUrl);
        imageName = f.getName();
        if (imageName.equalsIgnoreCase("")){
            imageName = CommonUtils.createProfileImageName(this,docEx);
        }


        S3FileUploadHelper transferHelper = new S3FileUploadHelper(this);
        transferHelper.upload(imgUrl,imageName);

        transferHelper.setFileTransferListener(new S3FileUploadHelper.FileTransferListener() {
            @Override
            public void onSuccess(int id, TransferState state, String fileName) {
                Log.d(TAG, String.format("%s uploaded successfully!", fileName));

                if (obj.getTypeOfFile().equalsIgnoreCase(_GALLERY)){
                    File yourFile = new File(obj.getImageUrl());
                    try {
                        File compressedImageFile = new Compressor(getApplicationContext()).compressToFile(yourFile);
                        uploadImageThumbnail(compressedImageFile,obj,fileName);
                    } catch (IOException mE) {
                        mE.printStackTrace();
                    }
                }else{
                    addTags(obj,fileName,fileName);
                }

            }
            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                //float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                //int percentDone = (int) percentDonef;
                // Log.d(TAG, "ID:" + id + " bytesCurrent: " + bytesCurrent + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }
            @Override
            public void onError(int id, Exception ex) {
                isServiceRunning = false;
                bundle.putString(Intent.EXTRA_TEXT, ex.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
        });
    }

    private synchronized void uploadImageThumbnail(File mFile, final SycnTags obj, final String originalFileName) {
        String fileN = "thumb_"+originalFileName;

        S3FileUploadHelper transferHelper = new S3FileUploadHelper(this);
        transferHelper.upload(mFile,fileN);
        transferHelper.setFileTransferListener(new S3FileUploadHelper.FileTransferListener() {
            @Override
            public void onSuccess(int id, TransferState state, String fileName) {
                Log.d(TAG, String.format("%s uploaded successfully!", fileName));
                addTags(obj,originalFileName,fileName);
            }
            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            }
            @Override
            public void onError(int id, Exception ex) {
            }
        });
    }

    public synchronized void addTags(final SycnTags mObj, String fileName,String fileThumbNail) {
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        Log.d(TAG, response);

                        db.getDao().delete(mObj);
                        loopForDBRecursively();
                        /*bundle.putString("result", "Success");
                        receiver.send(STATUS_FINISHED, bundle);
                        isServiceRunning = false;
                        Log.d(TAG, "Service Stopping!");*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isServiceRunning = false;
                bundle.putString(Intent.EXTRA_TEXT, error.toString());
                receiver.send(STATUS_ERROR, bundle);

            }
        };

        String url;
        Map<String, String> params = new LinkedHashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        params.put("Tag_Send_Date", mObj.getDate());
        params.put("Prime_Name", mObj.getPrimaryTag());

        params.put("Secondary_Tag", mObj.getSecondaryTag());

        params.put("Amount", mObj.getAmount());
        params.put("Description", mObj.getDescription());
        params.put("Tag_Type", mObj.getFlow());
        params.put("Tag_Status", mObj.getTagStatus());

        if (mObj.getTypeOfFile().equalsIgnoreCase(_GALLERY)){
            url = UrlConstants.ZODDL_POST_IMAGE_ADD_TAG;
            params.put("Image_Url", S3_BUCKET_DEV_URL+fileName);
            params.put("Image_Url_Thumb", S3_BUCKET_DEV_URL+fileThumbNail);
            params.put("Doc_Url", "");
        }else{
            url = UrlConstants.ZODDL_DOC_ADD_TAG;
            params.put("Image_Url", S3_BUCKET_DEV_URL+fileName);
            params.put("Image_Url_Thumb", S3_BUCKET_DEV_URL+fileThumbNail);
            params.put("Doc_Url", S3_BUCKET_DEV_URL+fileName);
        }


        VolleyApiRequest request = new VolleyApiRequest(url, params, stringListener, errorListener);
        Volley.newRequestQueue(this).add(request);
    }
}
