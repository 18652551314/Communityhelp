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
public class ErShou_Twe_ViewHolder extends RecyclerView.ViewHolder{
    public LinearLayout linearLayout;
    public ImageView imageView;
    public ImageView imaegeView_checkState;
    public TextView textView_delete;

    //网络change
    public TextView userName;
    public TextView creatTime;
    public ImageView headimgurl;
    public TextView title;
    public TextView content;
    public TextView contactPhone;
    public TextView replyNum;
    public ErShou_Twe_ViewHolder(View itemView) {
        super(itemView);
        imaegeView_checkState= (ImageView) itemView.findViewById(R.id.imageView_checkState_secondHand2);
        textView_delete= (TextView) itemView.findViewById(R.id.textView_secondHandDelete2);
        linearLayout= (LinearLayout) itemView.findViewById(R.id.ershou_ll_2);
        imageView= (ImageView) itemView.findViewById(R.id.ershou_huifu_iv_2);

        userName= (TextView) itemView.findViewById(R.id.sencse_apt_2_userName);
        creatTime= (TextView) itemView.findViewById(R.id.sencse_apt_2_creatTime);
        headimgurl= (ImageView) itemView.findViewById(R.id.sencse_apt_2_headimgurl);
        title= (TextView) itemView.findViewById(R.id.sencse_apt_2_title);
        content= (TextView) itemView.findViewById(R.id.sencse_apt_2_content);
        contactPhone= (TextView) itemView.findViewById(R.id.sencse_apt_2_contactPhone);
        replyNum= (TextView) itemView.findViewById(R.id.sencse_apt_2_replyNum);

    }
}
