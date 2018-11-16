package com.zoddl.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.bumptech.glide.Glide;
import com.zoddl.NetworkStateReceiver;
import com.zoddl.R;
import com.zoddl.utils.S3FileUploadHelper;
import com.zoddl.utils.UploadTagDataResultReceiver;
import com.zoddl.UploadTagDataService;
import com.zoddl.database.MyRoomDatabase;
import com.zoddl.database.SycnTags;
import com.zoddl.interfaces.ILogger;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.PrefManager;

import java.util.List;
import static com.zoddl.UploadTagDataService.isServiceRunning;
import static com.zoddl.utils.AppConstant.LOCAL_BROADCAST_FOR_GALARY_UPDATE_UI;

/**
 * Created by Abhishek Tiwari on 3/7/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, ILogger,NetworkStateReceiver.NetworkStateReceiverListener,UploadTagDataResultReceiver.Receiver {

    private ProgressDialog progressDialog;
    private UploadTagDataResultReceiver mReceiver;

    public abstract void setListeners();
    private NetworkStateReceiver networkStateReceiver;
    private MyRoomDatabase db;

    public PrefManager getPrefManager() {
        return PrefManager.getInstance(BaseActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));


        mReceiver = new UploadTagDataResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        db = Room.databaseBuilder(getApplicationContext(), MyRoomDatabase.class, AppConstant.DATABASE_NAME).allowMainThreadQueries().build();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }

    /**
     * short long duration toast method
     *
     * @param message
     */
    public void showToast(String message) {
        try {
            Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(BaseActivity.this);
        progressDialog.setMessage("Please wait");
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void showAlertDialog(String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(BaseActivity.this).create();
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setCancelable(false);

        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, BaseActivity.this.getString(android.R.string.ok), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                //     dialog.dismiss();

            }
        });
        if (!((Activity) BaseActivity.this).isFinishing()) {

            alertDialog.show();
        }

    }

    public void showAlertNoInternet(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(BaseActivity.this).create();
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setCancelable(false);

        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, BaseActivity.this.getString(android.R.string.ok), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                     dialog.dismiss();

            }
        });

        alertDialog.show();

    }

    public void showAlert(String title, String messgae) {
        final AlertDialog alertDialog = new AlertDialog.Builder(BaseActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);

        alertDialog.setMessage(messgae);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, BaseActivity.this.getString(android.R.string.ok), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                //HomeActivity.startHome(BaseActivity.this);
                dialog.dismiss();

            }
        });
        if (!((Activity) BaseActivity.this).isFinishing()) {

            alertDialog.show();
        }
    }

    @Override
    public void networkAvailable() {
        //Toast.makeText(this, "available", Toast.LENGTH_SHORT).show();
        checkAndStartService();
    }

    public void checkAndStartService() {
        try {
            List<SycnTags> obj = db.getDao().getAll();
            Log.d("UploadTagDataService",isServiceRunning+",service-"+obj.size());
            if (obj.size()!=0){
                if (!isServiceRunning){
                    //Toast.makeText(this, "Uploading data...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_SYNC, null, this, UploadTagDataService.class);
                    intent.putExtra("receiver", mReceiver);
                    intent.putExtra("requestId", 1001);
                    startService(intent);
                }

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public synchronized void uploadImage(String path, String fileName) {

        progressDialog.show();
        S3FileUploadHelper transferHelper = new S3FileUploadHelper(getApplicationContext());
        transferHelper.upload(path,fileName);
        transferHelper.setFileTransferListener(new S3FileUploadHelper.FileTransferListener() {
            @Override
            public void onSuccess(int id, TransferState state, String fileName) {
                Log.d("S3FileUpload1", String.format("%s uploaded successfully!", fileName));
                progressDialog.dismiss();

            }
            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;
                Log.d("S3FileUpload1", "ID:" + id + " bytesCurrent: " + bytesCurrent + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }
            @Override
            public void onError(int id, Exception ex) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void networkUnavailable() {
        showAlertNoInternet("No Internet Connection!");
        //Toast.makeText(this, "unavailable", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case UploadTagDataService.STATUS_RUNNING:
                Log.d("UploadTagDataService", "Running");
                setProgressBarIndeterminateVisibility(true);
                isServiceRunning = true;
                break;
            case UploadTagDataService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */
                Log.d("UploadTagDataService", "Finished");
                setProgressBarIndeterminateVisibility(false);
                isServiceRunning = false;
               // String[] results = resultData.getStringArray("result");
                //Toast.makeText(this, "Finished backgroud process", Toast.LENGTH_SHORT).show();
                sendMessage();
                break;
            case UploadTagDataService.STATUS_ERROR:
                /* Handle the error */
                String error = resultData.getString(Intent.EXTRA_TEXT);
                isServiceRunning = false;
                Log.d("UploadTagDataService", error);
                break;
        }
    }

    private void sendMessage() {
        Log.d("UploadTagDataService", "Broadcasting message");
        Intent intent = new Intent(LOCAL_BROADCAST_FOR_GALARY_UPDATE_UI);
        intent.putExtra("message", "Success");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void clearGlideDiskCache()
    {
        Log.d("clearGlideDiskCache", " start");
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Glide.get(getApplicationContext()).clearDiskCache();
            }
        }).start();

        Glide.get(getApplicationContext()).clearMemory();
    }


}
