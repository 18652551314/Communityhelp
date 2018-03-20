package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.taoshihui.TaoShiHui_ShangPingXiangQingActivity;
import com.example.administrator.communityhelp.myholder.PingLunRecycleViewHolder;
import com.example.administrator.communityhelp.myholder.RecycleViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/14.
 */
public class TaoShiHuiPingLunRecycleViewAdapter extends RecyclerView.Adapter{
    private List<Map<String,Object>> list;
    private Context context;

    public void AddAll(List<Map<String,Object>> list) {
        this.list = list;
    }

    public TaoShiHuiPingLunRecycleViewAdapter(List<Map<String,Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PingLunRecycleViewHolder holder = new PingLunRecycleViewHolder(LayoutInflater.from(context).inflate(R.layout.taoshihui_pingjia_recyclerview_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((PingLunRecycleViewHolder) holder).textView_userName.setText(list.get(position).get("userName") + "");
        ((PingLunRecycleViewHolder) holder).textView_date.setText(list.get(position).get("date") + "");
        ((PingLunRecycleViewHolder) holder).textView_pinglun.setText(list.get(position).get("pinglun") + "");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
