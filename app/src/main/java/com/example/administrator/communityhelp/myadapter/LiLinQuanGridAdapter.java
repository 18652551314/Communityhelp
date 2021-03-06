package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.my_other.GlideLoader;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 */
public class LiLinQuanGridAdapter extends BaseAdapter {

    private List<HashMap<String,Object >> list;
    private Context context;

    public LiLinQuanGridAdapter (List<HashMap<String,Object >> list, Context context) {
        this.list=list;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        MyHolder holder=null;
        if(convertView==null){
            holder=new MyHolder();
            convertView=View.inflate(context, R.layout.linli_main_major_item,null);
            holder.imageView= (ImageView) convertView.findViewById(R.id.image_linliquan);
            holder.textView= (TextView) convertView.findViewById(R.id.text_linliquan_item);

            convertView.setTag(holder);
        }else{
            holder= (MyHolder) convertView.getTag();
        }
        new GlideLoader().displayImage(context,list.get(position).get("image")+"",holder.imageView);

        holder.textView.setText(list.get(position).get("text")+"");
        return convertView;
    }
    class MyHolder{

        ImageView imageView;
        TextView textView;

    }
}
