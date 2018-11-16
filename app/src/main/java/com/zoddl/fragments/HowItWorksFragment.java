package com.zoddl.fragments;


import android.annotation.TargetApi;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.zoddl.R;
import com.zoddl.databinding.FragmentHowItWorksBinding;
import com.zoddl.utils.UrlConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class HowItWorksFragment extends Fragment {

private FragmentHowItWorksBinding binding;

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_how_it_works, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.faqWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        binding.faqWebview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(context, description, Toast.LENGTH_SHORT).show();
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.faqWebview.loadUrl("javascript:(function() { " +
                        "document.querySelector('[role=\"toolbar\"]').remove();})()");
                binding.faqProgressBar.setVisibility(View.GONE);
                binding.faqWebview.setVisibility(View.VISIBLE);
            }
        });

        binding.faqWebview.loadUrl(UrlConstants.ZODDL_MENU_HOW_IT_WORKS);
    }

}
