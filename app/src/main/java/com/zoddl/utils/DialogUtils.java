package com.zoddl.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.zoddl.R;

public class DialogUtils {

    /**
     * create custom dialog
     *
     * @param context      current application context
     * @param dialogLayout dialog layout view
     * @return dialog
     */
    public static Dialog createDialog(Context context, int dialogLayout) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(dialogLayout);

        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.7f;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();
        return dialog;
    }

    /**
     * default alert dialog with two positive and negative buttons
     *
     * @param context            current application context
     * @param message            dialog message to be displayed
     * @param positiveButtonText text to be displayed
     * @param negativeButtonText text to be displayed
     * @param dialogListener     dialogListener
     */
    public static void defaultAlertDialog(Context context, String message, String positiveButtonText,
                                          String negativeButtonText, final DialogListener dialogListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        dialogListener.onPositiveButtonClicked();
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        dialogListener.onNegativeButtonClicked();
                    }
                });
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * default alert dialog with one button
     *
     * @param context current application context
     * @param message dialog message to be displayed
     */
    public static void defaultAlertDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setNeutralButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * default dialog
     *
     * @param context        current application context
     * @param message        dialog message to be displayed
     * @param dialogListener dialogListener
     */
    public static void defaultDialog(Context context, String message, final DialogListener dialogListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setNeutralButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        dialogListener.onPositiveButtonClicked();
                    }
                });
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get progress dialog
     *
     * @param = current application context
     * @param      // message to be diaplayed
     * @return dialog
     */
//    public static Dialog getProgressDialog(Context context, String msg) {
//        return ProgressDialog.show(context, "", msg);
//    }

    public interface DialogListener {
        void onPositiveButtonClicked();

        void onNegativeButtonClicked();
    }

    public static ProgressDialog getProgressDialog(Context context, String msg) {
        ProgressDialog progressdialog = new ProgressDialog(context);
        progressdialog.setMessage(msg);
        progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressdialog.setIndeterminate(true);
        progressdialog.setCancelable(false);
        return progressdialog;
    }
}
