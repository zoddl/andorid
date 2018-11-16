package com.zoddl.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.zoddl.R;


public class MyTextView extends AppCompatTextView {
    public MyTextView(Context context) {
        super(context);
        init(null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/helvetica_neue_normal.ttf");
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyTextView);
            String fontName = a.getString(R.styleable.MyTextView_fontName);

            if (fontName != null) {
                tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
            }
            a.recycle();
        }
        setTypeface(tf);
    }

    @Override
    public boolean isInEditMode() {
        return super.isInEditMode();
    }
}
