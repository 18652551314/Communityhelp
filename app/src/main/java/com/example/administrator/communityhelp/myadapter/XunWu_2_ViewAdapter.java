package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.linliquan.carpooling.Carpooling_Detaile_Information;
import com.example.administrator.communityhelp.firstpage.linliquan.housing_rental.Housing_Detaile_Information;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myholder.ErShou_One_ViewHolder;
import com.example.administrator.communityhelp.myholder.PinChe_One_ViewHolder;
import com.example.administrator.communityhelp.myholder.XunWu_Two_ViewHolder;
import com.example.administrator.communityhelp.myinterface.MyCallBack;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/14.
 */
public class XunWu_2_ViewAdapter extends RecyclerView.Adapter {
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

    public XunWu_2_ViewAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        XunWu_Two_ViewHolder holder = new XunWu_Two_ViewHolder(LayoutInflater.from(context).inflate(R.layout.xunwu_apt_2, parent, false));
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
            ((XunWu_Two_ViewHolder) holder).imageView_checkState.setVisibility(View.VISIBLE);
            ((XunWu_Two_ViewHolder) holder).textView_delete.setVisibility(View.VISIBLE);
            ((XunWu_Two_ViewHolder) holder).imageView_checkState.setImageResource(images[0]);
        } else if (state == 0) {
            ((XunWu_Two_ViewHolder) holder).imageView_checkState.setVisibility(View.VISIBLE);
            ((XunWu_Two_ViewHolder) holder).textView_delete.setVisibility(View.VISIBLE);
            ((XunWu_Two_ViewHolder) holder).imageView_checkState.setImageResource(images[1]);
        } else if (state == 1) {
            ((XunWu_Two_ViewHolder) holder).imageView_checkState.setVisibility(View.VISIBLE);
            ((XunWu_Two_ViewHolder) holder).textView_delete.setVisibility(View.VISIBLE);
            ((XunWu_Two_ViewHolder) holder).imageView_checkState.setImageResource(images[2]);
        }else {
            ((XunWu_Two_ViewHolder) holder).imageView_checkState.setVisibility(View.GONE);
            ((XunWu_Two_ViewHolder) holder).textView_delete.setVisibility(View.GONE);
        }
        ((XunWu_Two_ViewHolder) holder).textView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCallBack.delete((String) list.get(position).get("id"));
            }
        });
        ((XunWu_Two_ViewHolder) holder).userName.setText(list.get(position).get("userName") + "");
        ((XunWu_Two_ViewHolder) holder).createTime.setText(list.get(position).get("createTime") + "");
        ((XunWu_Two_ViewHolder) holder).title.setText(list.get(position).get("title") + "");
        ((XunWu_Two_ViewHolder) holder).content.setText(list.get(position).get("content") + "");
        ((XunWu_Two_ViewHolder) holder).contact.setText(list.get(position).get("contact") + "");
        ((XunWu_Two_ViewHolder) holder).contactPhone.setText(list.get(position).get("contactPhone") + "");
        new GlideLoader().displayImage(context, list.get(position).get("headimgurl") + "", ((XunWu_Two_ViewHolder) holder).headimgurl);

        if ((list.get(position).get("picPath") + "").equals("")) {
            ((XunWu_Two_ViewHolder) holder).picPath.setVisibility(View.GONE);

        }
        {
            new GlideLoader().displayImage(context, list.get(position).get("picPath") + "", ((XunWu_Two_ViewHolder) holder).picPath);
        }

        ((XunWu_Two_ViewHolder) holder).replyNum.setText(list.get(position).get("replyNum") + "");
        ((XunWu_Two_ViewHolder) holder).linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Housing_Detaile_Information.class);
                intent.putExtra("id", list.get(position).get("id") + "");
                context.startActivity(intent);
            }
        });
        /*myCallBack.getintent(((XunWu_Two_ViewHolder) holder).linearLayout,((XunWu_Two_ViewHolder) holder).imageView);*/
        //callback.sendHolder(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
