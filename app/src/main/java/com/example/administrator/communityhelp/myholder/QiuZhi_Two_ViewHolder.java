package com.example.administrator.communityhelp.myholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;

/**
 * Created by Administrator on 2017/2/10.
 */
public class QiuZhi_Two_ViewHolder extends RecyclerView.ViewHolder{
    public TextView userName, createTime, title, content, contact, contactPhone, replyNum;
    public ImageView imageView, headimgurl;
    public LinearLayout linearLayout;

    public QiuZhi_Two_ViewHolder(View itemView) {
        super(itemView);
        userName = (TextView) itemView.findViewById(R.id.qiuzhi_main_adp_2_userName);
        createTime = (TextView) itemView.findViewById(R.id.qiuzhi_main_adp_2_createTime);
        title = (TextView) itemView.findViewById(R.id.qiuzhi_main_adp_2_title);
        content = (TextView) itemView.findViewById(R.id.qiuzhi_main_adp_2_content);
        contact = (TextView) itemView.findViewById(R.id.qiuzhi_main_adp_2_contact);
        contactPhone = (TextView) itemView.findViewById(R.id.qiuzhi_main_adp_2_contactPhone);
        headimgurl = (ImageView) itemView.findViewById(R.id.qiuzhi_main_adp_2_headigurl);
        replyNum = (TextView) itemView.findViewById(R.id.qiuzhi_main_adp_2_replyNum);

        linearLayout= (LinearLayout) itemView.findViewById(R.id.qiuzhi_22);
        imageView= (ImageView) itemView.findViewById(R.id.qiuzhi_huifu_iv);
    }
}
