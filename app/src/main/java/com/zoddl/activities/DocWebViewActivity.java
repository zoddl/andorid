package com.zoddl.activities;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zoddl.R;

import static com.zoddl.utils.UrlConstants.GOOGLE_URL_FOR_OPEN_DOC;

public class DocWebViewActivity extends BaseActivity {

    WebView mWebView;
    String urlDoc;
    private TextView title;
    private ImageView back;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_web_view);

        Toast.makeText(this, "Wait a moment...", Toast.LENGTH_SHORT).show();

        title = findViewById(R.id.tv_header);
        back = findViewById(R.id.iv_left);
        mProgressBar = findViewById(R.id.activity_doc_progress_bar);

        title.setText("Documents Details");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (getIntent()!=null){
            urlDoc = getIntent().getExtras().getString("DocUrl");
        }

        mWebView = findViewById(R.id.doc_webview);
        mWebView.getSettings().setJavaScriptEnabled(true); // enable javascript
        mWebView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(DocWebViewActivity.this, description, Toast.LENGTH_SHORT).show();
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
                mWebView.loadUrl("javascript:(function() { " +
                        "document.querySelector('[role=\"toolbar\"]').remove();})()");
                mProgressBar.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
            }
        });

        mWebView.loadUrl(GOOGLE_URL_FOR_OPEN_DOC+urlDoc);


    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public String getTagName() {
        return null;
    }
}
