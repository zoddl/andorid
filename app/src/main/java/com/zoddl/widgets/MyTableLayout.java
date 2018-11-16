package com.zoddl.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.zoddl.R;

/**
 * Created by Abhishek Tiwari on 7/7/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */
public class MyTableLayout extends TableLayout {

    private int noOfColumns;

    public MyTableLayout(Context context) {
        super(context);
    }

    public MyTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setHeaderLayout(Context context, TableLayout tableLayout, String[] headerData) {
        if (getNoOfColumns() > 0) {
            Typeface tfHeading = Typeface.createFromAsset(context.getAssets(), "fonts/helvetica_neue_normal.ttf");
            TableRow row = new TableRow(context);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT));
            for (int i = 0; i < getNoOfColumns(); i++) {
                TextView tv = new TextView(context);
                tv.setTypeface(tfHeading);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                tv.setTextColor(ContextCompat.getColor(context, R.color.colorDarkGray));
                tv.setMaxLines(1);
                tv.setPadding(12, 12, 12, 12);
                tv.setGravity(Gravity.CENTER);
                if (headerData.length > i) {
                    tv.setText(headerData[i]);
                }
                if (i == getNoOfColumns() - 1) {
                    tv.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBluishGray));
                } else {
                    tv.setBackground(ContextCompat.getDrawable(context, R.drawable.tab_header_bg));
                }
                row.addView(tv);
            }
            tableLayout.addView(row, 0);
        }
    }

    public void setFooterLayout(Context context, TableLayout tableLayout, int rowIndex, String[] footerData) {
        if (getNoOfColumns() > 0) {
            Typeface tfHeading = Typeface.createFromAsset(context.getAssets(), "fonts/helvetica_neue_normal.ttf");
            TableRow row = new TableRow(context);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT));
            for (int i = 0; i < getNoOfColumns(); i++) {
                TextView tv = new TextView(context);
                tv.setTypeface(tfHeading);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                tv.setTextColor(ContextCompat.getColor(context, R.color.colorDarkGray));
                tv.setMaxLines(1);
                tv.setPadding(12, 12, 12, 12);
                tv.setGravity(Gravity.CENTER);
                if (footerData.length > i) {
                    tv.setText(footerData[i]);
                }
                if (i % 2 == 0) {
                    tv.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBluishGrayTransparent));
                } else {
                    tv.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBluishGray));
                }
                row.addView(tv);
            }
            tableLayout.addView(row, rowIndex);
        }
    }

    public void setRowLayout(Context context, TableLayout tableLayout, int rowIndex, String[] rowData) {
        if (getNoOfColumns() > 0) {
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helvetica_neue_light.ttf");
            TableRow row = new TableRow(context);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT));
            for (int i = 0; i < getNoOfColumns(); i++) {
                TextView tv = new TextView(context);
                tv.setTypeface(tf);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                tv.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
                tv.setMaxLines(1);
                tv.setPadding(12, 12, 12, 12);
                tv.setGravity(Gravity.CENTER);
                if (rowData.length > i) {
                    tv.setText(rowData[i]);
                }
                if (rowIndex % 2 == 0) {
                    if (i % 2 == 0) {
                        tv.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBluishGrayTransparent));
                    } else {
                        tv.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBluishGray));
                    }
                } else {
                    if (i % 2 == 0) {
                        tv.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                    } else {
                        tv.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBluishGrayTransparent));
                    }
                }
                row.addView(tv);
            }
            tableLayout.addView(row, rowIndex);
        }
    }

    /**
     * @return Gets the value of noOfColumns and returns noOfColumns
     */
    public int getNoOfColumns() {
        return noOfColumns;
    }

    /**
     * Sets the noOfColumns
     * You can use getNoOfColumns() to get the value of noOfColumns
     */
    public void setNoOfColumns(int noOfColumns) {
        this.noOfColumns = noOfColumns;
    }
}
