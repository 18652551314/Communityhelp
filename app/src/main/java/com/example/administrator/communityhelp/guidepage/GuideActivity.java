package com.example.administrator.communityhelp.guidepage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.Community_Choice;
import com.example.administrator.communityhelp.FirstMajor;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.FirstPage;
import com.example.administrator.communityhelp.myadapter.GuideViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity {
    private ViewPager viewPager;
    private List<View> list;
    private GuideViewAdapter adapter;
    private ImageView imageView1, imageView2, imageView3, imageView4;
    private RelativeLayout layout;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_guide);
        buildSqlData();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        View view1 = getLayoutInflater().inflate(R.layout.guide_item_first, null);
        View view2 = getLayoutInflater().inflate(R.layout.guide_item_second, null);
        View view3 = getLayoutInflater().inflate(R.layout.guide_item_third, null);
        View view4 = getLayoutInflater().inflate(R.layout.guide_item_four, null);
        list = new ArrayList<>();
        list.add(view1);
        list.add(view2);
        list.add(view3);
        list.add(view4);
        adapter = new GuideViewAdapter(list, this);
        //viewPager.setAdapter(adapter);
        layout = (RelativeLayout) findViewById(R.id.layout);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        getBackground();
        imageView1.setBackground(getResources().getDrawable(R.drawable.circlebig));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if ((position + 1) % 4 == 1) {
                    getBackground();
                    imageView1.setBackground(getResources().getDrawable(R.mipmap.image_indicator_focus));
                } else if ((position + 1) % 4 == 2) {
                    getBackground();
                    imageView2.setBackground(getResources().getDrawable(R.mipmap.image_indicator_focus));
                } else if ((position + 1) % 4 == 3) {
                    getBackground();
                    imageView3.setBackground(getResources().getDrawable(R.mipmap.image_indicator_focus));
                } else {
                    getBackground();
                    imageView4.setBackground(getResources().getDrawable(R.mipmap.image_indicator_focus));
                    layout.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        preferences = getSharedPreferences("Guide", MODE_PRIVATE);
        boolean isFirst=preferences.getBoolean("isFirst", true);
        //判断是不是首次登录，
        if (isFirst) {
            editor = preferences.edit();
            //将登录标志位设置为false，下次登录时不在显示首次登录界面
            editor.putBoolean("isFirst", false);
            editor.commit();
            viewPager.setAdapter(adapter);
            //点击第四个引导页，跳转
            view4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GuideActivity.this, Community_Choice.class);
                    startActivity(intent);
                    finish();
                }
            });
        }else{
            Intent intent = new Intent(this,FirstMajor.class);
            startActivity(intent);
            finish();
        }
    }

    private void buildSqlData() {



    }

    public void getBackground() {
        imageView1.setBackground(getResources().getDrawable(R.mipmap.image_indicator));
        imageView2.setBackground(getResources().getDrawable(R.mipmap.image_indicator));
        imageView3.setBackground(getResources().getDrawable(R.mipmap.image_indicator));
        imageView4.setBackground(getResources().getDrawable(R.mipmap.image_indicator));
    }
}
