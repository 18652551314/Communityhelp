package com.example.administrator.communityhelp.my_other;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2017/2/21.
 */

public class MyWebView extends WebView {
    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }
    //    @Override
//    public void onPageFinished(WebView view, String url) {
//        super.onPageFinished(view, url);
//        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        view.measure(w,h);
//    }
}
