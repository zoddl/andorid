package com.zoddl.model;


import com.zoddl.R;

/**
 * Created by Hoang Anh Tuan on 11/29/2017.
 */

public enum EditType {
    Crop(R.drawable.ic_crop),
    Rotate(R.drawable.ic_rotate),
    Saturation(R.drawable.ic_saturation),
    Brightness(R.drawable.ic_brightness),
    Contrast(R.drawable.ic_contrast),
    Filter(R.drawable.ic_filter);

    public int VALUE;

    EditType(int VALUE) {
        this.VALUE = VALUE;
    }
}
