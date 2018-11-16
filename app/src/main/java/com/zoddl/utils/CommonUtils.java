package com.zoddl.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Common methods used within the application
 *
 * @author abhishek.tiwari
 */
@SuppressWarnings("ALL")
public class CommonUtils {

    public static String checkRangeAndFindFormattedDate(String mIndate_time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date startDate = null;
        try {
            startDate = df.parse(mIndate_time);
        } catch (ParseException mE) {
            mE.printStackTrace();
        }

        DateFormat format2 = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);
        String finalDateNDay = format2.format(startDate);
        return String.valueOf(finalDateNDay);

    }

        public static Uri getOutPutMediaFileUri(int mediaTypeImage) {
        //if(isExternalAvailable()){
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Snapshot");

            if(!mediaStorageDir.exists()){
                if(!mediaStorageDir.mkdirs()){
                   // Log.e("Dir", "Failed to create directory");
                    //Log.d("MAKE DIR", mediaStorageDir.mkdir() + "" +  mediaStorageDir.getParentFile() + "");
                    return null;
                }
            }
            File mediaFile;
            Date date = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(date);
            String path = mediaStorageDir.getPath() + File.separator;

            if(mediaTypeImage == 1){
                mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
            }else if(mediaTypeImage == 2){
                mediaFile = new File(path + "VID_" + timestamp + ".mp4");
            }else{
                return null;
            }
            return Uri.fromFile(mediaFile);

//        }else{
//            return null;
//        }

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String createProfileImageName(Context context,String docEx){
        if (context == null){
            return null;
        }

        String preFix = "tags_img_";
        String ts=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        String custId = PrefManager.getInstance(context).getKeyCustomerId();
        if (custId == null){
           custId = PrefManager.getInstance(context).getKeyAuthtoken();
        }
        return preFix + custId + "_" + ts + "."+docEx;
    }

    public static String createProfileImageNameWithoutExtention(Context context){
        if (context == null){
            return null;
        }

        String ts=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        String preFix = "tags_img_";
        //long currentTimeInMilli = Calendar.getInstance().getTimeInMillis();

        String custId = PrefManager.getInstance(context).getKeyCustomerId();
        if (custId == null){
            custId = PrefManager.getInstance(context).getKeyAuthtoken();
        }
        return preFix + custId + "_" + ts;
    }


    public static String createNameWithoutExtention(Context context){
        if (context == null){
            return null;
        }

        String ts=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        String preFix = "doc_";
        //long currentTimeInMilli = Calendar.getInstance().getTimeInMillis();

        String custId = PrefManager.getInstance(context).getKeyCustomerId();
        if (custId == null){
            custId = PrefManager.getInstance(context).getKeyAuthtoken();
        }
        return preFix + custId + "_" + ts;
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth() , v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    public static String getMonthName(String mPrimaryDate) {
        String month="";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(mPrimaryDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
            month = dateFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return month;
    }
    public static int getDisplayWidth(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

      /* this method is used for creating price format*/

    public static String getFormatPrice(String price) {
        Double dPrice = 0.0;
        try {
            dPrice = Double.parseDouble(price);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format(Locale.getDefault(), "%.2f", dPrice);
    }

    public static String getFormatPrice(float price) {
        if (price == -1.0f) {
            return "";
        } else {
            return String.format(Locale.getDefault(), "%.2f", price);
        }
    }

    public static void showToast(Context context, String s) {
        try {
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String uniqueName(Context context){
        if (context == null){
            return null;
        }

        String ts=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        return ts;
    }
}