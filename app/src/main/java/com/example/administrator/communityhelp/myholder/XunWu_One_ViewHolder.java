package com.example.administrator.communityhelp.myholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;

/**
 * Created by Administrator on 2016/12/14.
 */
public class XunWu_One_ViewHolder extends RecyclerView.ViewHolder{
    public TextView userName,createTime,title,content,contact,contactPhone,replyNum;
    public ImageView headimgurl;


    public LinearLayout linearLayout;
    public ImageView imageView;
    public XunWu_One_ViewHolder(View itemView) {
        super(itemView);
        userName= (TextView) itemView.findViewById(R.id.xunwu_apt_1_userName);
        createTime= (TextView) itemView.findViewById(R.id.xunwu_apt_1_createTime);
        title= (TextView) itemView.findViewById(R.id.xunwu_apt_1_title);
        content= (TextView) itemView.findViewById(R.id.xunwu_apt_1_content);
        contact= (TextView) itemView.findViewById(R.id.xunwu_apt_1_contact);
        contactPhone= (TextView) itemView.findViewById(R.id.xunwu_apt_1_contactPhone);
        replyNum= (TextView) itemView.findViewById(R.id.xunwu_apt_1_replyNum);
        headimgurl= (ImageView) itemView.findViewById(R.id.xunwu_apt_1_headimgurl);


        linearLayout= (LinearLayout) itemView.findViewById(R.id.xunwu_ll);
        imageView= (ImageView) itemView.findViewById(R.id.xunwu_huifu_iv);
    }
}
