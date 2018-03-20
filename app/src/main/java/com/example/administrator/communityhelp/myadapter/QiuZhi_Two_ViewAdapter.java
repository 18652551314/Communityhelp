package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.linliquan.recruit.Recruit_Detaile_Information;
import com.example.administrator.communityhelp.firstpage.linliquan.recruit.Recruit_Detaile_Information2;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myholder.QiuZhi_One_ViewHolder;
import com.example.administrator.communityhelp.myholder.QiuZhi_Two_ViewHolder;
import com.example.administrator.communityhelp.myinterface.MyCallBack;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/9.
 */
public class QiuZhi_Two_ViewAdapter extends RecyclerView.Adapter {
    private List<Map<String,Object>> list;
    private Context context;
    private MyCallBack myCallBack;

    public void setMyCallBack(MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
    }

    public void AddAll(List<Map<String,Object>> list) {
        this.list = list;
    }

    public QiuZhi_Two_ViewAdapter(List<Map<String,Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        QiuZhi_Two_ViewHolder holder = new QiuZhi_Two_ViewHolder(LayoutInflater.from(context).inflate(R.layout.qiuzhi_main_adp_2, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((QiuZhi_Two_ViewHolder) holder).userName.setText(list.get(position).get("userName") + "");
        ((QiuZhi_Two_ViewHolder) holder).createTime.setText(list.get(position).get("createTime") + "");
        ((QiuZhi_Two_ViewHolder) holder).title.setText(list.get(position).get("title") + "");
        ((QiuZhi_Two_ViewHolder) holder).content.setText(Html.fromHtml(list.get(position).get("content")+""));
        ((QiuZhi_Two_ViewHolder) holder).contact.setText(list.get(position).get("contact") + "");
        ((QiuZhi_Two_ViewHolder) holder).contactPhone.setText(list.get(position).get("contactPhone") + "");
        ((QiuZhi_Two_ViewHolder) holder).replyNum.setText(list.get(position).get("replyNum") + "");
        new GlideLoader().displayImage(context,list.get(position).get("headimgurl")+"",((QiuZhi_Two_ViewHolder) holder).headimgurl);
        ((QiuZhi_Two_ViewHolder) holder).linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),Recruit_Detaile_Information2.class);
                intent.putExtra("id",list.get(position).get("id") + "");
                context.startActivity(intent);
            }
        });
    /*    myCallBack.getintent(((QiuZhi_One_ViewHolder) holder).linearLayout,((QiuZhi_One_ViewHolder) holder).imageView);*/

        //callback.sendHolder(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
