package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.linliquan.housing_rental.Housing_Detaile_Information;
import com.example.administrator.communityhelp.firstpage.linliquan.housing_rental.Housing_Detaile_Information2;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myholder.QiuZhi_One_ViewHolder;
import com.example.administrator.communityhelp.myholder.XunWu_One_ViewHolder;
import com.example.administrator.communityhelp.myholder.XunWu_Two_ViewHolder;
import com.example.administrator.communityhelp.myinterface.MyCallBack;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/14.
 */
public class XunWu_One_ViewAdapter extends RecyclerView.Adapter{
    private List<Map<String,Object>> list;
    private Context context;
    MyCallBack myCallBack;

    public void setMyCallBack(MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
    }

    public void AddAll(List<Map<String,Object>> list) {
        this.list = list;
    }

    public XunWu_One_ViewAdapter(List<Map<String,Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        XunWu_One_ViewHolder holder = new XunWu_One_ViewHolder(LayoutInflater.from(context).inflate(R.layout.xunwu_1_main_adp, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((XunWu_One_ViewHolder) holder).userName.setText(list.get(position).get("userName") + "");
        ((XunWu_One_ViewHolder) holder).createTime.setText(list.get(position).get("createTime") + "");
        ((XunWu_One_ViewHolder) holder).title.setText(list.get(position).get("title") + "");
        ((XunWu_One_ViewHolder) holder).content.setText(list.get(position).get("content") + "");
        ((XunWu_One_ViewHolder) holder).contact.setText(list.get(position).get("contact") + "");
        ((XunWu_One_ViewHolder) holder).contactPhone.setText(list.get(position).get("contactPhone") + "");
        ((XunWu_One_ViewHolder) holder).replyNum.setText(list.get(position).get("replyNum") + "");
        new GlideLoader().displayImage(context,list.get(position).get("headimgurl")+"",((XunWu_One_ViewHolder) holder).headimgurl);
        ((XunWu_One_ViewHolder) holder).linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),Housing_Detaile_Information2.class);
                intent.putExtra("id",list.get(position).get("id") + "");
                context.startActivity(intent);
            }
        });
/*
        myCallBack.getintent(((XunWu_One_ViewHolder) holder).linearLayout,((XunWu_One_ViewHolder) holder).imageView);
*/

        //callback.sendHolder(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
