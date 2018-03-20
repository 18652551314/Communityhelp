package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 */
public class Firstpageviewpager extends PagerAdapter {
    private Context context;
    private List<ImageView> list;

    public Firstpageviewpager(Context context, List<ImageView> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        ImageView imageView=new ImageView(context);
//        ViewGroup.LayoutParams params=imageView.getLayoutParams();
//        params.width= ViewGroup.LayoutParams.MATCH_PARENT;
//        params.height= ViewGroup.LayoutParams.MATCH_PARENT;
//        imageView.setLayoutParams(params);
//        if(list.size()!=1){
//
//        }
//        list.add()

        container.addView(list.get(position));
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(position>list.size()-1){
            return;
        }
        container.removeView(list.get(position));
    }
}
