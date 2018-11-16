package com.zoddl.utils.imageutils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;


import com.zoddl.utils.LogUtils;

import java.io.ByteArrayOutputStream;

/**
 * @author abhishek.tiwari on 17/2/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */
public class ImageUtils {

    /**
     *decodeSampledBitmapFromFile
     * @param filePath file path uri to string
     * @param reqWidth required bitmap width
     * @param reqHeight required bitmap height
     * @return bitmap of filePath
     */
    public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath,options);
    }

    /**
     *decodeSampledBitmapFromBitmap
     * @param bitmap bitmap to be compress
     * @param reqWidth required bitmap width
     * @param reqHeight required bitmap height
     * @return bitmap of bitmap
     */
    public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap, int reqWidth, int reqHeight) {
        return Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, true);
    }

    /**
     *decodeSampledBitmapFromResource
     * @param res application resources
     * @param resId resources id
     * @param reqWidth required bitmap width
     * @param reqHeight required bitmap height
     * @return bitmap of resource
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     *calculateInSampleSize
     * @param options BitmapFactory options
     * @param reqWidth required bitmap width
     * @param reqHeight required bitmap height
     * @return sample size in integer
     */
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     *convertPathToBase64
     * @param picturePath : image path uri to string
     * @return base64 image in string
     */
    public static String convertPathToBase64(String picturePath) {
        if (picturePath != null && picturePath.length() > 0) {
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            bitmap.recycle();
            bitmap = null;
            byte[] bytes = baos.toByteArray();
            LogUtils.infoLog(getTAG(), ":::convertPathToBase64:::"+Base64.encodeToString(bytes, Base64.DEFAULT));
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        }
        return getTAG();
    }

    /**
     *StringToBitMap
     * @param encodedString : convert encoded bitmap string to bitmap
     * @return bitmap of encoded string
     */
    public static Bitmap stringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    /**
     *bitMapToString
     * @param bitmap bitmap of image
     * @return retring encoded string for bitmap
     */
    public static String bitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        LogUtils.infoLog(getTAG(), ":::bitMapToString:::"+Base64.encodeToString(b, Base64.DEFAULT));
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * getTAG
     * @return : get class name
     */
    private static String getTAG(){
        return ImageUtils.class.getSimpleName();
    }

    /**
     *
     * @param context
     * @param layoutResId
     * @param width 384/576/570 for wifi
     * @param height
     * @return
     */
    public static Bitmap drawToBitmap(Context context, final int layoutResId, final int width, final int height){
        final Bitmap bmp=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        final Canvas canvas=new Canvas(bmp);
        final LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout=inflater.inflate(layoutResId,null);
        layout.setDrawingCacheEnabled(true);
        layout.measure(View.MeasureSpec.makeMeasureSpec(canvas.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(canvas.getHeight(), View.MeasureSpec.EXACTLY));
        layout.layout(0,0,layout.getMeasuredWidth(),layout.getMeasuredHeight());
        canvas.drawBitmap(layout.getDrawingCache(),0,0,new Paint());
        return bmp;
    }
}
