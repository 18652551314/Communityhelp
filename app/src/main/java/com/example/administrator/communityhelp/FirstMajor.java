package com.example.administrator.communityhelp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.firstpage.FirstPage;
import com.example.administrator.communityhelp.my_other.DefaultTransformer;
import com.example.administrator.communityhelp.myadapter.MyAdapter;
import com.example.administrator.communityhelp.secondpage.SecondPage;
import com.example.administrator.communityhelp.thirdpage.ThirdPage;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 * 和随手记类似
 */
public class FirstMajor extends BaseActivity {
    int screenWidth;
    ImageView imageView, imageview1, imageview2, imageview3;
    TextView text2, text3;
    MyAdapter myAdapter;
    RelativeLayout relativeLayout, relativeLayout2, relativeLayout3;
    List<Fragment> list;
    View view_wangluo;
    ViewPager viewPager;
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_main);

        instart();
        //viewPager.setPageTransformer(true, new DefaultTransformer());

        FragmentManager manager = getSupportFragmentManager();
        myAdapter = new MyAdapter(manager, list);
        viewPager.setAdapter(myAdapter);
        viewPager.setOffscreenPageLimit(4);
        initTabLineWidth();
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                lp.leftMargin = screenWidth / 3 * position + positionOffsetPixels / 3;
                imageView.setLayoutParams(lp);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        View cv = getWindow().getDecorView();
        isweb(cv);
    }

    private void instart() {
        imageView = (ImageView) findViewById(R.id.id_tab_line_iv);
        viewPager = (ViewPager) findViewById(R.id.vvp);

        relativeLayout = (RelativeLayout) findViewById(R.id.rl_first_main);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.rl_second_main);
        relativeLayout3 = (RelativeLayout) findViewById(R.id.rl_third_main);

        list = new ArrayList<>();
        list.add(new FirstPage());
        list.add(new SecondPage());
        list.add(new ThirdPage());

        imageview1 = (ImageView) findViewById(R.id.iv_1);
        imageview2 = (ImageView) findViewById(R.id.iv_2);
        imageview3 = (ImageView) findViewById(R.id.iv_3);

        text1 = (TextView) findViewById(R.id.tv_main_1);
        text2 = (TextView) findViewById(R.id.tv_main_2);
        text3 = (TextView) findViewById(R.id.tv_main_3);
    }

    public void onClick1(View v) {
        setcolor();
        text1.setTextColor(Color.rgb(53, 187, 176));
        imageview1.setBackgroundResource(R.mipmap.sy_hover);
        viewPager.setCurrentItem(0);

    }

    public void onClick2(View v) {
        setcolor();
        text2.setTextColor(Color.rgb(53, 187, 176));
        imageview2.setBackgroundResource(R.mipmap.fx_hover);
        viewPager.setCurrentItem(1);
    }

    public void onClick3(View v) {
        setcolor();
        text3.setTextColor(Color.rgb(53, 187, 176));
        imageview3.setBackgroundResource(R.mipmap.wd_hover);
        viewPager.setCurrentItem(2);
    }

    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        lp.width = screenWidth / 3;
        imageView.setLayoutParams(lp);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    public void setcolor() {
        text1.setTextColor(Color.rgb(0, 0, 0));
        text2.setTextColor(Color.rgb(0, 0, 0));
        text3.setTextColor(Color.rgb(0, 0, 0));
        imageview1.setBackgroundResource(R.mipmap.sy);
        imageview2.setBackgroundResource(R.mipmap.fx);
        imageview3.setBackgroundResource(R.mipmap.wd);
    }
}
