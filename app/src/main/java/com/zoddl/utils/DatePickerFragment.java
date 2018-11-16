package com.zoddl.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment {

    private Context context;
    private int theme;
    private long selectedDate, minDate, maxDate;
    private DatePickerDialog.OnDateSetListener listener;
    public static final int NO_THEME =-1;

    public static DatePickerFragment getInstance(Context context, DatePickerDialog.OnDateSetListener listener){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.context = context;
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current date as the default date in the date picker
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

//        - Does not work correctly with
//        AlertDialog.THEME_DEVICE_DEFAULT_LIGHT
//        AlertDialog.THEME_DEVICE_DEFAULT_DARK

//        -Works fine with those Theme
//        AlertDialog.THEME_HOLO_LIGHT
//        AlertDialog.THEME_HOLO_DARK
//        AlertDialog.THEME_TRADITIONAL

        //Get the DatePicker instance from DatePickerDialog
        DatePickerDialog dpd = new DatePickerDialog(context, listener, year, month, day);

        //setting date
//        dpd.getDatePicker().setMinDate(minDate);
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());

        dpd.show();
        return dpd; //Return the DatePickerDialog in app window
    }
}