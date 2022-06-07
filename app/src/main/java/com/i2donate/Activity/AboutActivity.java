package com.i2donate.Activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.widget.Toolbar;

import com.i2donate.CommonActivity.CommonMenuActivity;
import com.i2donate.Model.ChangeActivity;
import com.i2donate.R;

public class AboutActivity extends CommonMenuActivity {
    private String TAG = "AboutActivity";
    Toolbar toolbar;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_about,TAG);
        setTitle("");
        toolbar = findViewById(R.id.commonMenuActivityToolbar);
        init();
    }

    @SuppressLint("JavascriptInterface")
    private void init() {
        webView=(WebView)findViewById(R.id.webView);
        webView.getSettings();
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setScrollbarFadingEnabled(true);
        WebSettings webSettings = webView.getSettings();
       // webSettings.setTextSize(WebSettings.TextSize.SMALLEST);
        webSettings.setJavaScriptEnabled(true);
       // webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        //webView.loadUrl("https://admin.i2-donate.com/about_us.html");
        webView.loadUrl("https://admin.i2-donate.com/i2D-Publish-Docs/i2D-App-About.html");
        webSettings.setTextZoom(webSettings.getTextZoom() - 40);
        //webView.addJavascriptInterface(new MyJavaScriptInterface(), "Please");
        webView.setWebViewClient(new MyWebViewClient());
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("response_html11",""+url);
            if ("https://admin.i2-donate.com/i2D-Publish-Docs/i2-Donate%20Terms%20and%20Conditions.html".equalsIgnoreCase(url)) {
                Log.e("response_html123",""+Uri.parse(url).getHost());
                ChangeActivity.changeActivity(AboutActivity.this, TermsAndConditionActivity.class);
                // This is my website, so do not override; let my WebView load the page
                return true;
            }else if ("https://admin.i2-donate.com/i2D-Publish-Docs/i2-Donate%20Privacy%20Policy.html".equalsIgnoreCase(url)){
                ChangeActivity.changeActivity(AboutActivity.this, PrivacyPolicyActivity.class);
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
           /* Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);*/
            return true;
        }
    }

}
