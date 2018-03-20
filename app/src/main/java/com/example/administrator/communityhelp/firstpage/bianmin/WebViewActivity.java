package com.example.administrator.communityhelp.firstpage.bianmin;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.my_other.ProgressDialogAon;

public class WebViewActivity extends BaseActivity {
    private WebView webView;
    private TextView textView_back, textView_title;
    private ProgressDialogAon aon;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.travel_paymenthall_webview);
        webView = (WebView) findViewById(R.id.webView);
        textView_back = (TextView) findViewById(R.id.textView_back);
        textView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textView_title = (TextView) findViewById(R.id.textView_title);
        textView_title.setText(this.getIntent().getStringExtra("name"));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        aon = new ProgressDialogAon(this);
        //让进度条显示出来
        aon.show();
        //1秒钟后停止动画
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                aon.dismiss();
            }
        }, 500);
        webView.loadUrl(this.getIntent().getStringExtra("url"));
    }
}
