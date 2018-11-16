package com.zoddl.interfaces;

import com.zoddl.database.SycnTags;

/**
 * Created by vipin on 27/4/18.
 */

public interface OnEventListenerTollbar {
    void onEvent(String val, int pos);
    //void onEvent(SycnTags er, int pos);
    void onEventForObject(SycnTags obj);

}
