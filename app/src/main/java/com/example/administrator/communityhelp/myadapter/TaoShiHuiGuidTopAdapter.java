package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.my_other.GlideLoader;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/3.
 */
public class TaoShiHuiGuidTopAdapter extends BaseAdapter{
    private List<Map<String,Object>> list;
    private Context context;

    public TaoShiHuiGuidTopAdapter(List<Map<String, Object>> list, Context context) {
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
            view=View.inflate(context, R.layout.taoshihui_gridview_top,null);
            holder.imageView= (ImageView) view.findViewById(R.id.TaoShiHui_gridView_image);
            holder.textView= (TextView) view.findViewById(R.id.TaoShiHui_gridView_text);
            view.setTag(holder);
        }else{
            holder= (MyHolder) view.getTag();
        }
        new GlideLoader().displayImage(context,list.get(position).get("columnImgPath")+"",holder.imageView);
        holder.textView.setText(list.get(position).get("name")+"");
        return view;
    }

    class MyHolder{
        ImageView imageView;
        TextView textView;
    }
}
