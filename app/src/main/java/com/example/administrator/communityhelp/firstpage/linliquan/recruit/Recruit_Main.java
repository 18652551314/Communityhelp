package com.example.administrator.communityhelp.firstpage.linliquan.recruit;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.Login_Main;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.linliquan.second_hand.Second_Fragment_1;
import com.example.administrator.communityhelp.firstpage.linliquan.second_hand.Second_Fragment_2;
import com.example.administrator.communityhelp.firstpage.linliquan.tourism.Tourism_Details_Main;
import com.example.administrator.communityhelp.myadapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */
public class Recruit_Main extends BaseActivity {
    View view;
    int screenWidth;
    ViewPager viewPager;
    TextView textView1, textView2, textView3;
    ImageView imageView;
    Intent intent;
    MyAdapter myAdapter;
    List<Fragment> list;
    int position1;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_carpooling__main);
        view = findViewById(R.id.view_pin_1);
        textView1 = (TextView) findViewById(R.id.first_tit);
        textView2 = (TextView) findViewById(R.id.first_mass_1);
        textView3 = (TextView) findViewById(R.id.first_mass_2);
        imageView = (ImageView) findViewById(R.id.first_iv_1);
        textView1.setText("求职招聘");
        textView2.setText("招聘");
        textView3.setText("求职");


        instart();
        viewPager = (ViewPager) findViewById(R.id.carpooling_vp);
        FragmentManager manager = getSupportFragmentManager();
        myAdapter = new MyAdapter(manager, list);
        viewPager.setAdapter(myAdapter);

        initTabLineWidth();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
                lp.leftMargin = screenWidth / 2 * position + positionOffsetPixels / 2;
                view.setLayoutParams(lp);
                position1 = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        lp.width = screenWidth / 2;
        view.setLayoutParams(lp);
    }

    public void carpool_click_1(View v) {

        viewPager.setCurrentItem(0);
    }

    public void carpool_click_2(View v) {

        viewPager.setCurrentItem(1);
    }

    public void iv_suppor_1(View v) {
        if (isLoad()) {
            if (position1 == 0) {
                intent = new Intent(this, Recruit_Details_main.class);
                startActivity(intent);
            } else if (position1 == 1) {
                intent = new Intent(this, Recruit_Details_main_1.class);
                startActivity(intent);
            }
        } else {
            intent=new Intent(this, Login_Main.class);
            startActivity(intent);
        }
    }

    public void ll_suppor_1(View v) {
        finish();
    }

    private void instart() {
        list = new ArrayList<>();
        list.add(new Recruit_Fragment_1());
        list.add(new Recruit_Fragment_2());
    }
}
