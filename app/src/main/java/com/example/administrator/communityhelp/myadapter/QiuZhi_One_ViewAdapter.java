package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.linliquan.housing_rental.Housing_Detaile_Information;
import com.example.administrator.communityhelp.firstpage.linliquan.recruit.Recruit_Detaile_Information;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myholder.PinChe_One_ViewHolder;
import com.example.administrator.communityhelp.myholder.QiuZhi_One_ViewHolder;
import com.example.administrator.communityhelp.myholder.XunWu_Two_ViewHolder;
import com.example.administrator.communityhelp.myinterface.MyCallBack;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/14.
 */
public class QiuZhi_One_ViewAdapter extends RecyclerView.Adapter {
    int images[] = {R.drawable.shz, R.drawable.shwtg, R.drawable.shtg};
    private List<Map<String, Object>> list;
    private Context context;
    private MyCallBack myCallBack;

    public void setMyCallBack(MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
    }

    public void AddAll(List<Map<String, Object>> list) {
        this.list = list;
    }

    public QiuZhi_One_ViewAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        QiuZhi_One_ViewHolder holder = new QiuZhi_One_ViewHolder(LayoutInflater.from(context).inflate(R.layout.qiuzhi_main_adp, parent, false));
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
            ((QiuZhi_One_ViewHolder) holder).imageView_checkState.setVisibility(View.VISIBLE);
            ((QiuZhi_One_ViewHolder) holder).imageView_checkState.setImageResource(images[0]);
            ((QiuZhi_One_ViewHolder) holder).textView_jobDelete.setVisibility(View.VISIBLE);
        } else if (state == 0) {
            ((QiuZhi_One_ViewHolder) holder).imageView_checkState.setVisibility(View.VISIBLE);
            ((QiuZhi_One_ViewHolder) holder).imageView_checkState.setImageResource(images[1]);
            ((QiuZhi_One_ViewHolder) holder).textView_jobDelete.setVisibility(View.VISIBLE);
        }else if(state==1){
            ((QiuZhi_One_ViewHolder) holder).imageView_checkState.setVisibility(View.VISIBLE);
            ((QiuZhi_One_ViewHolder) holder).imageView_checkState.setImageResource(images[2]);
            ((QiuZhi_One_ViewHolder) holder).textView_jobDelete.setVisibility(View.VISIBLE);
        }else {
            ((QiuZhi_One_ViewHolder) holder).imageView_checkState.setVisibility(View.GONE);
            ((QiuZhi_One_ViewHolder) holder).textView_jobDelete.setVisibility(View.GONE);
        }
        ((QiuZhi_One_ViewHolder) holder).textView_jobDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCallBack.delete((String) list.get(position).get("id"));
            }
        });

        ((QiuZhi_One_ViewHolder) holder).userName.setText(list.get(position).get("userName") + "");
        ((QiuZhi_One_ViewHolder) holder).createTime.setText(list.get(position).get("createTime") + "");
        ((QiuZhi_One_ViewHolder) holder).title.setText(list.get(position).get("title") + "");
        ((QiuZhi_One_ViewHolder) holder).content.setText(Html.fromHtml(list.get(position).get("content") + ""));
        ((QiuZhi_One_ViewHolder) holder).contact.setText(list.get(position).get("contact") + "");
        ((QiuZhi_One_ViewHolder) holder).contactPhone.setText(list.get(position).get("contactPhone") + "");
        ((QiuZhi_One_ViewHolder) holder).replyNum.setText(list.get(position).get("replyNum") + "");
        new GlideLoader().displayImage(context, list.get(position).get("headimgurl") + "", ((QiuZhi_One_ViewHolder) holder).headimgurl);
        ((QiuZhi_One_ViewHolder) holder).linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Recruit_Detaile_Information.class);
                intent.putExtra("id", list.get(position).get("id") + "");
                context.startActivity(intent);
            }
        });


/*
        myCallBack.getintent(((QiuZhi_One_ViewHolder) holder).linearLayout,((QiuZhi_One_ViewHolder) holder).imageView);
*/

        //callback.sendHolder(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
