package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.taoshihui.TaoShiHui_ShangPingLieBiaoActivity;
import com.example.administrator.communityhelp.firstpage.taoshihui.TaoShiHui_ShangPingXiangQingActivity;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myholder.RecycleViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/14.
 */
public class TaoShiHuiRecycleViewAdapter extends RecyclerView.Adapter{
    private List<Map<String,Object>> list;
    private Context context;

    public void AddAll(List<Map<String,Object>> list) {
        this.list = list;
    }

    public TaoShiHuiRecycleViewAdapter(List<Map<String,Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecycleViewHolder holder = new RecycleViewHolder(LayoutInflater.from(context).inflate(R.layout.taoshihui_recycleview_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        new GlideLoader().displayImage(context,list.get(position).get("picPath")+"",((RecycleViewHolder) holder).imageView);
        ((RecycleViewHolder) holder).textView_name.setText(list.get(position).get("goodsName") + "");
        ((RecycleViewHolder) holder).textView_referral.setText(list.get(position).get("summary") + "");
        ((RecycleViewHolder) holder).textView_price.setText(list.get(position).get("discountPrice") + "");
        ((RecycleViewHolder) holder).textView_oldPrice.setText(list.get(position).get("oldPrice") + "");
        ((RecycleViewHolder) holder).textView_sold.setText(list.get(position).get("peopleBuy")+"");
        ((RecycleViewHolder) holder).textView_oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,TaoShiHui_ShangPingXiangQingActivity.class);
                intent.putExtra("id",list.get(position).get("id")+"");
                context.startActivity(intent);
            }
        });
        //callback.sendHolder(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
