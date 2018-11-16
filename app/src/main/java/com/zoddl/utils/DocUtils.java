/**
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zoddl.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * avanish.info
 * Provides utility functions.
 */
public class DocUtils {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String writeStreamToFile(Context mContext, InputStream inputStream,String fileName) {
        String uniqueFileName = createName();

        if (inputStream == null) {
            return "";
        }
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        try {

            File file = File.createTempFile(uniqueFileName, findDocExe(fileName), storageDir);
            uniqueFileName = file.getPath();

            try (OutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                output.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return uniqueFileName;
    }

    public static String findDocExe(String fileName){
        String extension = null;
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        if (extension!=null)
            return "."+extension;

        return "";
    }

    public static String createName(){

        String preFix = "tags_doc";
        String ts=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());


        return preFix + "_" + ts;
    }


/*    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void writeStreamToFile(Context mContext, Uri mUri,String ext) {
        if (mUri == null) {
            return;
        }

        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = mContext.getContentResolver().query(mUri, filePathColumn, null, null, null);
        String mCurrentPath = null;
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mCurrentPath = cursor.getString(columnIndex);
            cursor.close();
        }

        String fileName = CommonUtils.createProfileImageNameWithoutExtention(mContext);
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        InputStream inputStream = null;
        try {
            inputStream = mContext.getContentResolver().openInputStream(mUri);

            File file = File.createTempFile(fileName, ext, storageDir);

            try (OutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                output.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}
