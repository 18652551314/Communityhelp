package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.bianmin.activity.Details;
import com.example.administrator.communityhelp.myholder.GuideActivityRecycleViewHolder;
import com.example.administrator.communityhelp.myholder.TelephoneActivityRecycleViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/14.
 */
public class TelephoneActivityRecycleViewAdapter extends RecyclerView.Adapter{
    private List<Map<String,Object>> list;
    private Context context;

    public void AddAll(List<Map<String,Object>> list) {
        this.list = list;
    }

    public TelephoneActivityRecycleViewAdapter(List<Map<String,Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TelephoneActivityRecycleViewHolder holder = new TelephoneActivityRecycleViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_telephone_recycleview_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((TelephoneActivityRecycleViewHolder) holder).textView_name.setText(list.get(position).get("title") + "");
        ((TelephoneActivityRecycleViewHolder) holder).textView_phone.setText(list.get(position).get("contactPhone") + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转系统拨号界面
                String telephone=((TelephoneActivityRecycleViewHolder) holder).textView_phone.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+telephone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
