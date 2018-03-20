package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.linliquan.carpooling.Carpooling_Detaile_Information;
import com.example.administrator.communityhelp.firstpage.linliquan.carpooling.Carpooling_Detaile_Information2;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myholder.PinChe_One_ViewHolder;
import com.example.administrator.communityhelp.myholder.PinChe_Twe_ViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/14.
 */
public class BianMin_Two_ViewAdapter extends RecyclerView.Adapter{
    private List<Map<String,Object>> list;
    private Context context;

    public void AddAll(List<Map<String,Object>> list) {
        this.list = list;
    }

    public BianMin_Two_ViewAdapter(List<Map<String,Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PinChe_Twe_ViewHolder holder = new PinChe_Twe_ViewHolder(LayoutInflater.from(context).inflate(R.layout.carpooling_main_adp_2, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((PinChe_Twe_ViewHolder) holder).userName.setText(list.get(position).get("userName") + "");
        ((PinChe_Twe_ViewHolder) holder).userName.setText(list.get(position).get("userName") + "");
        new GlideLoader().displayImage(context,list.get(position).get("headimgurl")+"",((PinChe_Twe_ViewHolder) holder).headimgurl);
        ((PinChe_Twe_ViewHolder) holder).createTime.setText(list.get(position).get("creatTime") + "");
        ((PinChe_Twe_ViewHolder) holder).startdian.setText(list.get(position).get("start") + "");
        ((PinChe_Twe_ViewHolder) holder).enddian.setText(list.get(position).get("end") + "");
        ((PinChe_Twe_ViewHolder) holder).startTime.setText(list.get(position).get("startTime") + "");
        ((PinChe_Twe_ViewHolder) holder).contact.setText(list.get(position).get("contact") + "");
        ((PinChe_Twe_ViewHolder) holder).contactPhone.setText(list.get(position).get("contactPhone") + "");
        ((PinChe_Twe_ViewHolder) holder).catContent.setText(list.get(position).get("content") + "");
        ((PinChe_Twe_ViewHolder) holder).linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),Carpooling_Detaile_Information2.class);
                intent.putExtra("id",list.get(position).get("id") + "");
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
