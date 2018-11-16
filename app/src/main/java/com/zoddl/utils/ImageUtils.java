package com.zoddl.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.style.ImageSpan;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by avanish.info.
 */

public class ImageUtils {

         public static void saveImage(Uri sourceUri, String targetPath) {
        if (sourceUri == null){
            throw new IllegalArgumentException("sourceUri can not be null");
        }
        if (targetPath == null){
            throw new IllegalArgumentException("targetPath can not be null");
        }

        String sourceFilename = sourceUri.getPath();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {

            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(targetPath, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void renameFile(String sourcePath, String targetPath) {
        if (sourcePath == null){
            throw new IllegalArgumentException("sourcePath can not be null");
        }
        if (targetPath == null){
            throw new IllegalArgumentException("targetPath can not be null");
        }

        String sourceFilename = sourcePath;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {

            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(targetPath, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String createImageFileForBitmap(Context mContext,ByteArrayOutputStream bytes){
        String fileName = CommonUtils.createProfileImageNameWithoutExtention(mContext);
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        File image = null;
        FileOutputStream fo;
        try {
            image = File.createTempFile(
                      /* prefix */fileName,
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
                                       );

            try {
                fo = new FileOutputStream(image);
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException mE) {
            mE.printStackTrace();
        }

        return image.getPath();
    }

    public static String createImageFileForBitmap(Context mContext,ByteArrayOutputStream bytes,String fileName){
       // String fileName = CommonUtils.createProfileImageNameWithoutExtention(mContext);
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        File image = null;
        FileOutputStream fo;
        try {
            image = File.createTempFile(
                      /* prefix */fileName,
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
                                       );

            try {
                fo = new FileOutputStream(image);
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException mE) {
            mE.printStackTrace();
        }

        return image.getPath();
    }



    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
                                        );
        return image;
    }

    public static File createImageFileCache(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalCacheDir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
                                        );
        return image;
    }

    public static boolean deleteImage(String path){
        File file = new File(path);
        if (file.exists()){
            return  file.delete();
        }
        return false;
    }
    public static byte[] getFileDataFromPath(Context context, String path) {

        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            Uri uri = Uri.fromFile(new File(path));
            bitmap = rotateImageIfRequired(bitmap, context, uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    /**
     * Turn drawable into byte array.
     *
     * @param drawable data
     * @return byte array
     */
    public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {

        if (selectedImage.getScheme().equals("content")) {
            String[] projection = { MediaStore.Images.ImageColumns.ORIENTATION };
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    public static ImageSpan getImageSpan(int imageId,Context context){
        Drawable d = context.getResources().getDrawable(imageId);
        d.setBounds(0, 0, 64, 64);
        return new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
    }

    public static File[] getIStickerLocalGalleryImages(Context context) {
        File folderName = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = folderName.listFiles();
        if (files != null && files.length > 0) {
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File object1, File object2) {
                    return (int) ((object1.lastModified() > object2.lastModified())
                            ? object1.lastModified() : object2.lastModified());
                }
            });
        }

        return files;
    }

    public static Boolean saveBitmap(String output, Bitmap bitmap) {
        FileOutputStream out = null;
        boolean success = true;
        try {
            out = new FileOutputStream(output);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            if (!bitmap.isRecycled()) bitmap.recycle();
        } catch (Exception e) {
            success = false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignored) {
            }
        }
        return success;
    }

    public static Bitmap scaleDown(Bitmap realImage) {
        float ratio = Math.min(1000.0F / (float)realImage.getWidth(), 1000.0F / (float)realImage.getHeight());
        int width = Math.round(ratio * (float)realImage.getWidth());
        int height = Math.round(ratio * (float)realImage.getHeight());
        return Bitmap.createScaledBitmap(realImage, width, height, true);
    }

    public static Bitmap getBitmapSdcard(String url) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(url, options);
//        if (bitmap.getWidth() > bitmap.getHeight()) {
//            bitmap = rotate(bitmap);
//        }
        return bitmap;
    }

    public static Bitmap rotate(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static void rateApp(Context context) {
        final Uri uri = Uri.parse("market://details?id=" + context.getApplicationContext().getPackageName());
        final Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);
        if (context.getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0) {
            context.startActivity(rateAppIntent);
        }
    }


    public static void shareFile(Context context, String url) {
        try {
            File myFile = new File(url);
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String ext = myFile.getName().substring(myFile.getName().lastIndexOf(".") + 1);
            String type = mime.getMimeTypeFromExtension(ext);
            Intent sharingIntent = new Intent("android.intent.action.SEND");
            sharingIntent.setType(type);
            sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(myFile));
            context.startActivity(Intent.createChooser(sharingIntent, "Share using"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readAllPhoto(Activity activity, PhotoCallback photoCallback) {
        new PhotoTask(activity, photoCallback).execute();
    }

    public static Bitmap brightBitmap(Bitmap bitmap, int brightness) {
        float[] colorTransform = {
                1, 0, 0, 0, brightness,
                0, 1, 0, 0, brightness,
                0, 0, 1, 0, brightness,
                0, 0, 0, 1, 0};

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f);
        colorMatrix.set(colorTransform);

        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);


        Bitmap resultBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);

        return resultBitmap;
    }

    public static Bitmap contrastBitmap(Bitmap bitmap, int contrast) {
        float[] colorTransform = {
                contrast, 0, 0, 0, 1,
                0, contrast, 0, 0, 1,
                0, 0, contrast, 0, 1,
                0, 0, 0, 1, 0};

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f);
        colorMatrix.set(colorTransform);

        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);


        Bitmap resultBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);

        return resultBitmap;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {

        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        bitmap.recycle();

        Bitmap newBitmap = Bitmap.createBitmap(resultBitmap.getWidth(), resultBitmap.getHeight(), resultBitmap.getConfig());
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(resultBitmap, 0, 0, null);

        resultBitmap.recycle();

        return newBitmap;
    }

    private static class PhotoTask extends AsyncTask<String, String, Object> {

        @SuppressLint("StaticFieldLeak")
        private Activity activity;
        private PhotoCallback callback;

        PhotoTask(Activity activity, PhotoCallback callback) {
            this.activity = activity;
            this.callback = callback;
        }

        @Override
        protected Object doInBackground(String... params) {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media._ID;
            Cursor imagecursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            assert imagecursor != null;
            for (int i = 0; i < imagecursor.getCount(); i++) {
                imagecursor.moveToPosition(i);
                int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                publishProgress(imagecursor.getString(dataColumnIndex));
            }
            imagecursor.close();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            callback.onProgressUpdateListPhoto(values[0]);
        }
    }

    public interface PhotoCallback {
        void onProgressUpdateListPhoto(String url);
    }
}
