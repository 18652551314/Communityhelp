package com.example.administrator.communityhelp.myadapter;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.bianmin.activity.Image_Big;

import java.util.ArrayList;

public class MyPageAdapter extends PagerAdapter {
    Intent intent;
    private ArrayList<View> viewLists;

    public MyPageAdapter() {
    }

    public MyPageAdapter(ArrayList<View> viewLists) {
        super();
        this.viewLists = viewLists;
    }


    @Override
    public int getCount() {
        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewLists.get(position));

        return viewLists.get(position);
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewLists.get(position));
    }
}