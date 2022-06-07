package com.i2donate.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.widget.Toolbar;

import com.i2donate.CommonActivity.CommonMenuActivity;
import com.i2donate.R;

public class HelpSupportActivity extends CommonMenuActivity {

    private String TAG = "HelpSupportActivity";
    Toolbar toolbar;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_help_support,TAG);
        setTitle("");
        toolbar = findViewById(R.id.commonMenuActivityToolbar);
        init();
    }

    private void init() {
        webView=(WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings();
        webView.setScrollbarFadingEnabled(true);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl("https://admin.i2-donate.com/help_and_support.html");

        final WebSettings webSettings = webView.getSettings();
        webSettings.setTextZoom(webSettings.getTextZoom() - 68);



    }
}
