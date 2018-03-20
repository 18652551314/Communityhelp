package com.example.administrator.communityhelp.myadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.administrator.communityhelp.thirdpage.publication.MySecond_Fragment_1;
import com.example.administrator.communityhelp.thirdpage.publication.MySecond_Fragment_2;

import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */
public class MyAdapter extends FragmentPagerAdapter {
    FragmentManager manager;
    List<Fragment> fragments;
    public MyAdapter(FragmentManager manager, List<Fragment> fragments) {
        super(manager);
        this.manager=manager;
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


}
