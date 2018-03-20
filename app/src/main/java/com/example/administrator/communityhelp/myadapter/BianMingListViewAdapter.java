package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.my_other.GlideLoader;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/7.
 */
public class BianMingListViewAdapter extends BaseAdapter{
    private List<Map<String,Object>> list;
    private Context context;

    public BianMingListViewAdapter(List<Map<String,Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        MyHolder holder=null;
        if(view==null){
            holder=new MyHolder();
            view=View.inflate(context, R.layout.activity_bianming_listview,null);
            holder.textView_title= (TextView) view.findViewById(R.id.textView_title);
            holder.textView_content= (TextView) view.findViewById(R.id.textView_content);
            holder.imageView_icon= (ImageView) view.findViewById(R.id.imageView_icon);
            view.setTag(holder);
        }else{
            holder= (MyHolder) view.getTag();
        }
        holder.textView_title.setText(list.get(position).get("name")+"");
        holder.textView_content.setText(list.get(position).get("desc")+"");
        //new GlideLoader().displayImage(context,list.get(position).get("bmColumnImgPath")+"",holder.imageView_icon);
        Glide.with(context)
                .load(list.get(position).get("bmColumnImgPath")+"")
                .placeholder(R.mipmap.loading)
                .dontAnimate()
                .into(holder.imageView_icon);
        return view;
    }

    class MyHolder{
        TextView textView_title,textView_content;
        ImageView imageView_icon;
    }
}
