package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.linliquan.second_hand.Second_Hand_Detaile_Information;
import com.example.administrator.communityhelp.firstpage.linliquan.second_hand.Second_Hand_Detaile_Information2;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myholder.ErShou_One_ViewHolder;
import com.example.administrator.communityhelp.myholder.ErShou_Twe_ViewHolder;
import com.example.administrator.communityhelp.myinterface.MyCallBack;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/14.
 */
public class ErShou_Twe_ViewAdapter extends RecyclerView.Adapter{
    int images[] = {R.drawable.shz, R.drawable.shwtg, R.drawable.shtg};
    private List<Map<String,Object>> list;
    private Context context;
    public MyCallBack myCallBack;
    public MyCallBack myCallBack2;
    public void setMyCallBack(MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
    }
    public void setMyCallBack2(MyCallBack myCallBack) {
        this.myCallBack2 = myCallBack;
    }

    public void AddAll(List<Map<String,Object>> list) {
        this.list = list;
    }

    public ErShou_Twe_ViewAdapter(List<Map<String,Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ErShou_Twe_ViewHolder holder = new ErShou_Twe_ViewHolder(LayoutInflater.from(context).inflate(R.layout.sencse_apt_2, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int state = 100;
        try {
            state = (int) list.get(position).get("approvedResult");
        } catch (Exception e) {
        }
        if (state == -1) {
            ((ErShou_Twe_ViewHolder) holder).imaegeView_checkState.setVisibility(View.VISIBLE);
            ((ErShou_Twe_ViewHolder) holder).textView_delete.setVisibility(View.VISIBLE);
            ((ErShou_Twe_ViewHolder) holder).imaegeView_checkState.setImageResource(images[0]);
        } else if (state == 0) {
            ((ErShou_Twe_ViewHolder) holder).imaegeView_checkState.setVisibility(View.VISIBLE);
            ((ErShou_Twe_ViewHolder) holder).textView_delete.setVisibility(View.VISIBLE);
            ((ErShou_Twe_ViewHolder) holder).imaegeView_checkState.setImageResource(images[1]);
        } else if (state == 1) {
            ((ErShou_Twe_ViewHolder) holder).imaegeView_checkState.setVisibility(View.VISIBLE);
            ((ErShou_Twe_ViewHolder) holder).textView_delete.setVisibility(View.VISIBLE);
            ((ErShou_Twe_ViewHolder) holder).imaegeView_checkState.setImageResource(images[2]);
        } else {
            ((ErShou_Twe_ViewHolder) holder).imaegeView_checkState.setVisibility(View.GONE);
            ((ErShou_Twe_ViewHolder) holder).textView_delete.setVisibility(View.GONE);
        }
        ((ErShou_Twe_ViewHolder) holder).textView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCallBack.delete(list.get(position).get("id").toString());
//                myCallBack2.delete(list.get(position).get("id").toString());
            }
        });


        ((ErShou_Twe_ViewHolder) holder).userName.setText(list.get(position).get("userName") + "");
        ((ErShou_Twe_ViewHolder) holder).creatTime.setText(list.get(position).get("createTime") + "");
        ((ErShou_Twe_ViewHolder) holder).title.setText(list.get(position).get("title") + "");
        ((ErShou_Twe_ViewHolder) holder).content.setText(list.get(position).get("content") + "");
        ((ErShou_Twe_ViewHolder) holder).contactPhone.setText(list.get(position).get("contactPhone") + "");
        ((ErShou_Twe_ViewHolder) holder).replyNum.setText(list.get(position).get("replyNum") + "");
        new GlideLoader().displayImage(context,list.get(position).get("headimgurl")+"",((ErShou_Twe_ViewHolder) holder).headimgurl);
        ((ErShou_Twe_ViewHolder) holder).linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),Second_Hand_Detaile_Information2.class);
                intent.putExtra("id",list.get(position).get("id") + "");
                context.startActivity(intent);
            }
        });

/*
        myCallBack.getintent(((ErShou_Twe_ViewHolder) holder).linearLayout,((ErShou_Twe_ViewHolder) holder).imageView);
*/

        //callback.sendHolder(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
