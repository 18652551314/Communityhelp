package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.linliquan.tourism.Tourism_Detaile_Information;
import com.example.administrator.communityhelp.firstpage.linliquan.tourism.Tourism_Main;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myholder.LunTan_ViewHolder;
import com.example.administrator.communityhelp.myholder.LvYou_ViewHolder;
import com.example.administrator.communityhelp.myholder.PinChe_One_ViewHolder;
import com.example.administrator.communityhelp.myinterface.MyCallBack;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/14.
 */
public class LvYou_One_ViewAdapter extends RecyclerView.Adapter {
    int images[]={R.drawable.shz,R.drawable.shwtg,R.drawable.shtg};
    private List<Map<String, Object>> list;
    private Context context;
    private int i = 1;
    MyCallBack myCallBack;

    public void setMyCallBack(MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
    }

    public void AddAll(List<Map<String, Object>> list) {
        this.list = list;
    }

    public LvYou_One_ViewAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LvYou_ViewHolder holder = new LvYou_ViewHolder(LayoutInflater.from(context).inflate(R.layout.lvyou_main_adp, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int state = 100;
        try {
            state = (int) list.get(position).get("approvedResult");
        } catch (Exception e) {
        }
        if(state==-1){
            ((LvYou_ViewHolder) holder).textView_delete.setVisibility(View.VISIBLE);
            ((LvYou_ViewHolder) holder).imageView_checkState.setVisibility(View.VISIBLE);
            ((LvYou_ViewHolder) holder).imageView_checkState.setImageResource(images[0]);
        }else if(state==0){
            ((LvYou_ViewHolder) holder).textView_delete.setVisibility(View.VISIBLE);
            ((LvYou_ViewHolder) holder).imageView_checkState.setVisibility(View.VISIBLE);
            ((LvYou_ViewHolder) holder).imageView_checkState.setImageResource(images[1]);
        }else if(state==1){
            ((LvYou_ViewHolder) holder).textView_delete.setVisibility(View.VISIBLE);
            ((LvYou_ViewHolder) holder).imageView_checkState.setVisibility(View.VISIBLE);
            ((LvYou_ViewHolder) holder).imageView_checkState.setImageResource(images[2]);
        }else {
            ((LvYou_ViewHolder) holder).textView_delete.setVisibility(View.GONE);
            ((LvYou_ViewHolder) holder).imageView_checkState.setVisibility(View.GONE);
        }
        ((LvYou_ViewHolder) holder).textView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCallBack.delete((String)list.get(position).get("id"));
            }
        });

        ((LvYou_ViewHolder) holder).title.setText(list.get(position).get("title") + "");
        ((LvYou_ViewHolder) holder).contact.setText(list.get(position).get("contact") + "");
        ((LvYou_ViewHolder) holder).contactPhone.setText(list.get(position).get("contactPhone") + "");
        new GlideLoader().displayImage(context, list.get(position).get("picPath") + "", ((LvYou_ViewHolder) holder).picPath);
        ((LvYou_ViewHolder) holder).linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(v.getContext(), Tourism_Detaile_Information.class);
                intent2.putExtra("id", list.get(position).get("id") + "");
                context.startActivity(intent2);
            }
        });
//        m/*yCallBack.getintent( ((LvYou_ViewHolder) holder).linearLayout, ((LvYou_ViewHolder) holder).imageView);
//        //callback.sendHolder(holder);*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
