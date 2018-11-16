package com.zoddl;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by garuv on 2/5/18.
 */

public class BaseStringRequest extends StringRequest {
    private static final int TIMEOUT_MS = 5000;
    private static final int MAX_RETRY = 0;

    public BaseStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
        RetryPolicy myRetryPolycy = new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return super.setRetryPolicy(myRetryPolycy);
    }
}
