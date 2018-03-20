package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.taoshihui.TaoShiHui_ShangPingXiangQingActivity;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myholder.RecycleViewHolder;
import com.example.administrator.communityhelp.myholder.RecycleViewXiangQingHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/14.
 */
public class TaoShiHuiXiangQingAdapter extends RecyclerView.Adapter{
    private List<Map<String,Object>> list;
    private Context context;

    public TaoShiHuiXiangQingAdapter(List<Map<String,Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecycleViewXiangQingHolder holder = new RecycleViewXiangQingHolder(LayoutInflater.from(context).inflate(R.layout.taoshihui_shangpingxiangqing_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((RecycleViewXiangQingHolder) holder).textView_title.setText(list.get(position).get("title") + "");
        ((RecycleViewXiangQingHolder) holder).textView_context.setText(Html.fromHtml(list.get(position).get("content") + ""));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
