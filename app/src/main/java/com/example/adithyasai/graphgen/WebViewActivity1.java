package com.example.adithyasai.graphgen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewActivity1 extends AppCompatActivity {

    private WebView wv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view1);
        Toast.makeText(this,"Loading... Please wait",Toast.LENGTH_LONG).show();
    wv1=(WebView)findViewById(R.id.webview);
    wv1.setWebViewClient(new MyBrowser());

    String url = "http://manojsenguttuvan.com/html/";
    wv1.getSettings().setLoadsImagesAutomatically(true);
    wv1.getSettings().setJavaScriptEnabled(true);
    wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    wv1.loadUrl(url);

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
