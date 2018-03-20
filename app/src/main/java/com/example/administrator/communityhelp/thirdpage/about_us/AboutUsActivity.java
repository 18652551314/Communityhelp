package com.example.administrator.communityhelp.thirdpage.about_us;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.example.administrator.communityhelp.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class AboutUsActivity extends AppCompatActivity {
    ImageView infoOperatingIV;
    Animation operatingAnim;
    WebView webview;
    ZoomControls zoomctrl;
    LinearLayout linearlayout;
    int control_state = 0;
    Intent intent_uri;
    String uri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        init();
        zoomctrl.hide();
        intent_uri = getIntent();
        uri = "http://120.27.5.22:8080/mo/client/recommend.mo";
        //设置动画
        infoOperatingIV = (ImageView) findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.progress_rotate);
//        LinearInterpolator lin = new LinearInterpolator();
        infoOperatingIV.setAnimation(operatingAnim);
        if (operatingAnim != null) {
            infoOperatingIV.startAnimation(operatingAnim);
        }
        //开启动画
        operatingAnim.start();
        //监听设置WebView加载完毕后结束动画
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //当进度走到100的时候做自己的操作
                if (progress == 100) {
                    infoOperatingIV.clearAnimation();
                    infoOperatingIV.setVisibility(View.GONE);
//                    Toast.makeText(AboutUsActivity.this, "加载完毕", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //设置触碰事件
        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if(control_state<=2){
//                            zoomctrl.setAnimation(new AlphaAnimation(0.1f, 1.0f));
//                            zoomctrl.show();
                        }
                        control_state = 4;
                        break;
                }
                return false;
            }
        });
//        zoomctrl.setOnZoomInClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                webview.zoomIn();
//                control_state = 4;
//            }
//        });
//        zoomctrl.setOnZoomOutClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                webview.zoomOut();
//                control_state = 4;
//            }
//        });
        linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //设置多点触控
        webview.getSettings().setBuiltInZoomControls(true);
    }

    private void init() {
        webview = (WebView) findViewById(R.id.webView_aboutus);
        zoomctrl = (ZoomControls) findViewById(R.id.zoomCtrl);
        linearlayout = (LinearLayout) findViewById(R.id.back_layout_aboutUs);
    }

    @Override
    protected void onStart() {
        super.onStart();
        infoOperatingIV.setAnimation(operatingAnim);
        operatingAnim.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
       new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   Thread.sleep(500);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      //加载网页
                      webview.loadUrl(uri);
                  }
              });
           }
       }).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(1000);
//                        control_state--;
//                        if (control_state < 2 && control_state >= 1) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    zoomctrl.setAnimation(new AlphaAnimation(1.0f, 0f));
//                                    zoomctrl.hide();
//                                }
//                            });
//                        }
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
    }
}
