package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.bianmin.activity.Details;
import com.example.administrator.communityhelp.firstpage.taoshihui.TaoShiHui_ShangPingXiangQingActivity;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myholder.RecycleViewHolder;
import com.example.administrator.communityhelp.myholder.SheQuActivityRecycleViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/14.
 */
public class SheQuActivityRecycleViewAdapter extends RecyclerView.Adapter{
    private List<Map<String,Object>> list;
    private Context context;

    public void AddAll(List<Map<String,Object>> list) {
        this.list = list;
    }

    public SheQuActivityRecycleViewAdapter(List<Map<String,Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SheQuActivityRecycleViewHolder holder = new SheQuActivityRecycleViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_shequ_recycleview_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        new GlideLoader().displayImage(context,list.get(position).get("picPath")+"",((SheQuActivityRecycleViewHolder) holder).imageView);
        ((SheQuActivityRecycleViewHolder) holder).textView_name.setText(list.get(position).get("title") + "");
        ((SheQuActivityRecycleViewHolder) holder).textView_referral.setText(list.get(position).get("content") + "");
        ((SheQuActivityRecycleViewHolder) holder).textView_date.setText(list.get(position).get("releaseDate") + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,Details.class);
                intent.putExtra("id", list.get(position).get("id").toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
