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
public class QiuZhi_One_ViewHolder extends RecyclerView.ViewHolder {
    public TextView userName, createTime, title, content, contact, contactPhone, replyNum,textView_jobDelete;
    public LinearLayout linearLayout;
    public ImageView imageView, headimgurl,imageView_checkState;

    public QiuZhi_One_ViewHolder(View itemView) {
        super(itemView);
        textView_jobDelete= (TextView) itemView.findViewById(R.id.textView_jobDelete);
        imageView_checkState= (ImageView) itemView.findViewById(R.id.imageView_checkState_job);
        userName = (TextView) itemView.findViewById(R.id.qiuzhi_main_adp_1_userName);
        createTime = (TextView) itemView.findViewById(R.id.qiuzhi_main_adp_1_createTime);
        title = (TextView) itemView.findViewById(R.id.qiuzhi_main_adp_1_title);
        content = (TextView) itemView.findViewById(R.id.qiuzhi_main_adp_1_content);
        contact = (TextView) itemView.findViewById(R.id.qiuzhi_main_adp_1_contact);
        contactPhone = (TextView) itemView.findViewById(R.id.qiuzhi_main_adp_1_contactPhone);
        headimgurl = (ImageView) itemView.findViewById(R.id.qiuzhi_main_adp_1_headimgurl);
        replyNum = (TextView) itemView.findViewById(R.id.qiuzhi_main_adp_1_replyNum);

        linearLayout = (LinearLayout) itemView.findViewById(R.id.qiuzhi_ll);
        imageView = (ImageView) itemView.findViewById(R.id.qiuzhi_huifu_iv);
    }
}
