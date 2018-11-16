package com.zoddl.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zoddl.interfaces.ILogger;


public abstract class BaseFragment extends Fragment implements View.OnClickListener, ILogger {
    public abstract void setListeners();


    public void showAlertDialog(String title, String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(title);
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



}
