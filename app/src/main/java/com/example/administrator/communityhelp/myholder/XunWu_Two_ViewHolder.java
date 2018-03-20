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
public class XunWu_Two_ViewHolder extends RecyclerView.ViewHolder{

    public LinearLayout linearLayout;
    public ImageView imageView,picPath,headimgurl,imageView_checkState;

    public TextView userName,createTime,title,content,contact,contactPhone,replyNum,textView_delete;

    public XunWu_Two_ViewHolder(View itemView) {
        super(itemView);
        textView_delete= (TextView) itemView.findViewById(R.id.textView_houseDelete);
        imageView_checkState= (ImageView) itemView.findViewById(R.id.imageView_checkState_house);
        linearLayout= (LinearLayout) itemView.findViewById(R.id.fangwu_ll);
        imageView= (ImageView) itemView.findViewById(R.id.fangwu_huifu_iv);
        headimgurl= (ImageView) itemView.findViewById(R.id.xunwu_apt_2_headimgurl);
        userName= (TextView) itemView.findViewById(R.id.xunwu_apt_2_userName);
        title= (TextView) itemView.findViewById(R.id.xunwu_apt_2_title);
        createTime= (TextView) itemView.findViewById(R.id.xunwu_apt_2_createTime);
        content= (TextView) itemView.findViewById(R.id.xunwu_apt_2_content);
        contact= (TextView) itemView.findViewById(R.id.xunwu_apt_2_contact);
        contactPhone= (TextView) itemView.findViewById(R.id.xunwu_apt_2_contactPhone);
        replyNum= (TextView) itemView.findViewById(R.id.xunwu_apt_2_replyNum);
        picPath= (ImageView) itemView.findViewById(R.id.xunwu_apt_2_picPath);
    }
}
