package com.example.administrator.communityhelp.thirdpage.publication.bbs;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/1/6.
 */
public class PictureViewPagerAdapter extends PagerAdapter {
    List<View> list;

    public PictureViewPagerAdapter(List<View> list) {
        this.list = list;
    }

    public void refreshList(List<View> list) {
        this.list = list;
        getCount();
    }

    //删除页面重写该方法
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position));

        return list.get(position);
    }

    //删除页面重写该方法
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        try {
//            container.removeView(list.get(position));
//        } catch (Exception e) {
//        }
        (container).removeView((View)object);
    }
}
