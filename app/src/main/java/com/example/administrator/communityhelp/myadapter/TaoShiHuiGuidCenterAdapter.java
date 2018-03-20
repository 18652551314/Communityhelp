package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myholder.RecycleViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/3.
 */
public class TaoShiHuiGuidCenterAdapter extends BaseAdapter{
    private List<Map<String,Object>> list;
    private Context context;

    public TaoShiHuiGuidCenterAdapter(List<Map<String, Object>> list, Context context) {
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
            view=View.inflate(context, R.layout.taoshihui_gridview_center,null);
            holder.imageView= (ImageView) view.findViewById(R.id.TaoShiHui_gridView_image);
            holder.textView_name= (TextView) view.findViewById(R.id.TaoShiHui_gridView_text);
            holder.textView_price= (TextView) view.findViewById(R.id.TaoShiHui_gridView_price);
            holder.textView_oldPrice= (TextView) view.findViewById(R.id.TaoShiHui_gridView_oldPrice);
            view.setTag(holder);
        }else{
            holder= (MyHolder) view.getTag();
        }
        new GlideLoader().displayImage(context,list.get(position).get("picPath")+"",holder.imageView);
        holder.textView_name.setText(list.get(position).get("goodsName") + "");
        holder.textView_price.setText(list.get(position).get("discountPrice") + "");
        holder.textView_oldPrice.setText(list.get(position).get("oldPrice") + "");
        holder.textView_oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        return view;
    }

    class MyHolder{
        ImageView imageView;
        TextView textView_name,textView_price,textView_oldPrice;
    }
}
