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
 * Created by Administrator on 2017/1/9.
 */
public class Carpooling_Detaile_Information_Adapter extends BaseAdapter {
    private List<HashMap<String,Object >> list;
    private Context context;

    public Carpooling_Detaile_Information_Adapter (List<HashMap<String,Object >> list, Context context) {
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
            convertView=View.inflate(context, R.layout.carpooling_detaile_information_item,null);
            holder.imageView= (ImageView) convertView.findViewById(R.id.carpooling_details_IDpicture);
            holder.title= (TextView) convertView.findViewById(R.id.carpooling_details_information_title);
            holder.details= (TextView) convertView.findViewById(R.id.carpooling_details_information_context);
            holder.time= (TextView) convertView.findViewById(R.id.carpooling_details_information_time);

            convertView.setTag(holder);
        }else{
            holder= (MyHolder) convertView.getTag();
        }

        holder.title.setText(list.get(position).get("title")+"");
        holder.details.setText(list.get(position).get("datails")+"");
        holder.time.setText(list.get(position).get("time")+"");
        new GlideLoader().displayImage(context,list.get(position).get("image")+"",holder.imageView);
        return convertView;
    }
    class MyHolder{

        ImageView imageView;
        TextView title,time,details;

    }
}
