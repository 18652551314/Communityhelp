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
public class LookForSth_Two_ViewHolder extends RecyclerView.ViewHolder {
    public TextView userName, createTime, title, content, contact, contactPhone, replyNum;
    public LinearLayout linearLayout;
    public ImageView imageView, headimgurl, picPath;

    public LookForSth_Two_ViewHolder(View itemView) {
        super(itemView);
        userName = (android.widget.TextView) itemView.findViewById(R.id.look_for_sth_adp_2_userName);
        createTime = (TextView) itemView.findViewById(R.id.look_for_sth_adp_2_createTime);
        title = (TextView) itemView.findViewById(R.id.look_for_sth_adp_2_title);
        content = (TextView) itemView.findViewById(R.id.look_for_sth_adp_2_content);
        contact = (TextView) itemView.findViewById(R.id.look_for_sth_adp_2_contact);
        contactPhone = (TextView) itemView.findViewById(R.id.look_for_sth_adp_2_contactPhone);
        headimgurl = (ImageView) itemView.findViewById(R.id.look_for_sth_adp_2_headimgurl);
        replyNum = (TextView) itemView.findViewById(R.id.look_for_sth_adp_2_replyNum);

        picPath = (ImageView) itemView.findViewById(R.id.look_for_sth_adp_2_picPath);


        linearLayout = (LinearLayout) itemView.findViewById(R.id.lookfor_22);
        imageView = (ImageView) itemView.findViewById(R.id.qiuzhi_huifu_iv);
    }


}
