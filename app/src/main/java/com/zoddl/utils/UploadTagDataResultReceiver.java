package com.zoddl.utils;

/**
 * Created by Neel on 9/14/14.
 */
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class UploadTagDataResultReceiver extends ResultReceiver {
    private Receiver mReceiver;

    public UploadTagDataResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}

