package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.bianmin.activity.Details;
import com.example.administrator.communityhelp.firstpage.linliquan.carpooling.Carpooling_Detaile_Information;
import com.example.administrator.communityhelp.firstpage.linliquan.tourism.Tourism_Detaile_Information;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myholder.LunTan_ViewHolder;
import com.example.administrator.communityhelp.myholder.PinChe_One_ViewHolder;
import com.example.administrator.communityhelp.myholder.SheQuActivityRecycleViewHolder;
import com.example.administrator.communityhelp.myinterface.MyCallBack;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/14.
 */
public class BianMin_One_ViewAdapter extends RecyclerView.Adapter {
    int images[] = {R.drawable.shz, R.drawable.shwtg, R.drawable.shtg};
    private List<Map<String, Object>> list;
    private Context context;
    MyCallBack myCallBack;
    MyCallBack myCallBack2;

    public void setMyCallBack(MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
    }

    public void setMyCallBack2(MyCallBack myCallBack) {
        this.myCallBack2 = myCallBack;
    }

    public void AddAll(List<Map<String, Object>> list) {
        this.list = list;
    }

    public BianMin_One_ViewAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PinChe_One_ViewHolder holder = new PinChe_One_ViewHolder(LayoutInflater.from(context).inflate(R.layout.carpooling_main_adp, parent, false));
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
            ((PinChe_One_ViewHolder) holder).imageView_checkstate.setVisibility(View.VISIBLE);
            ((PinChe_One_ViewHolder) holder).textView_delete.setVisibility(View.VISIBLE);
            ((PinChe_One_ViewHolder) holder).imageView_checkstate.setImageResource(images[0]);
        } else if (state == 0) {
            ((PinChe_One_ViewHolder) holder).imageView_checkstate.setVisibility(View.VISIBLE);
            ((PinChe_One_ViewHolder) holder).textView_delete.setVisibility(View.VISIBLE);
            ((PinChe_One_ViewHolder) holder).imageView_checkstate.setImageResource(images[1]);
        } else if (state == 1) {
            ((PinChe_One_ViewHolder) holder).imageView_checkstate.setVisibility(View.VISIBLE);
            ((PinChe_One_ViewHolder) holder).textView_delete.setVisibility(View.VISIBLE);
            ((PinChe_One_ViewHolder) holder).imageView_checkstate.setImageResource(images[2]);
        } else {
            ((PinChe_One_ViewHolder) holder).imageView_checkstate.setVisibility(View.GONE);
            ((PinChe_One_ViewHolder) holder).textView_delete.setVisibility(View.GONE);
        }
        ((PinChe_One_ViewHolder) holder).textView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCallBack.delete((String) list.get(position).get("id"));
                myCallBack2.delete((String) list.get(position).get("id"));
            }
        });
        ((PinChe_One_ViewHolder) holder).userName.setText(list.get(position).get("userName") + "");
        new GlideLoader().displayImage(context, list.get(position).get("headimgurl") + "", ((PinChe_One_ViewHolder) holder).headimgurl);
        ((PinChe_One_ViewHolder) holder).createTime.setText(list.get(position).get("creatTime") + "");
        ((PinChe_One_ViewHolder) holder).startdian.setText(list.get(position).get("start") + "");
        ((PinChe_One_ViewHolder) holder).enddian.setText(list.get(position).get("end") + "");
        ((PinChe_One_ViewHolder) holder).startTime.setText(list.get(position).get("startTime") + "");
        ((PinChe_One_ViewHolder) holder).contact.setText(list.get(position).get("contact") + "");
        ((PinChe_One_ViewHolder) holder).contactPhone.setText(list.get(position).get("contactPhone") + "");
        ((PinChe_One_ViewHolder) holder).carInfo.setText(list.get(position).get("carInfo") + "");
        ((PinChe_One_ViewHolder) holder).catContent.setText(list.get(position).get("content") + "");


        ((PinChe_One_ViewHolder) holder).linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Carpooling_Detaile_Information.class);
                intent.putExtra("id", list.get(position).get("id") + "");
                context.startActivity(intent);
            }
        });
        //myCallBack.getintent(((PinChe_One_ViewHolder) holder).linearLayout,null);


        //callback.sendHolder(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
