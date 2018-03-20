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
public class ErShou_One_ViewHolder extends RecyclerView.ViewHolder {
    public ImageView imaegeView_checkState;
    public TextView textView_delete;
    public LinearLayout linearLayout;
    public ImageView imageView;

    public TextView userName;
    public TextView createTime;
    public ImageView headimgurl;
    public TextView title;
    public TextView content;
    public TextView contactPhone;
    public ImageView picPath;
    public TextView replyNum;


    public ErShou_One_ViewHolder(View itemView) {
        super(itemView);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.ershou_ll);
        imageView = (ImageView) itemView.findViewById(R.id.ershou_huifu_iv);

        userName = (TextView) itemView.findViewById(R.id.sencse_apt_1_userName);
        createTime = (TextView) itemView.findViewById(R.id.sencse_apt_1_createTime);
        headimgurl = (ImageView) itemView.findViewById(R.id.pinche_iv1);
        title = (TextView) itemView.findViewById(R.id.sencse_apt_1_title);
        content = (TextView) itemView.findViewById(R.id.sencse_apt_1__content);
        contactPhone = (TextView) itemView.findViewById(R.id.sencse_apt_1_contactPhone);
        picPath = (ImageView) itemView.findViewById(R.id.sencse_apt_1_picPath);
        replyNum = (TextView) itemView.findViewById(R.id.sencse_apt_1_replyNum);

        imaegeView_checkState = (ImageView) itemView.findViewById(R.id.imageView_checkState_secondHand);
        textView_delete = (TextView) itemView.findViewById(R.id.textView_secondHandDelete);

    }
}
