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
public class PinChe_Twe_ViewHolder extends RecyclerView.ViewHolder{
    public TextView userName;
    public ImageView headimgurl;
    public TextView createTime;
    public TextView startdian;
    public TextView enddian;
    public TextView startTime;
    public TextView contact;
    public TextView contactPhone;
    public TextView catContent;
    public LinearLayout linearLayout;

    public PinChe_Twe_ViewHolder(View itemView) {
        super(itemView);
        userName = (TextView) itemView.findViewById(R.id.carpooling_main_zcz_userName);
        headimgurl = (ImageView) itemView.findViewById(R.id.pinche_iv1);
        createTime= (TextView) itemView.findViewById(R.id.carpooling_main_zcz_createTime);
        startdian= (TextView) itemView.findViewById(R.id.carpooling_main_zcz_start);
        enddian= (TextView) itemView.findViewById(R.id.carpooling_main_zcz_end);
        startTime= (TextView) itemView.findViewById(R.id.carpooling_main_zcz_startTime);
        contact= (TextView) itemView.findViewById(R.id.carpooling_main_zcz_contact);
        contactPhone= (TextView) itemView.findViewById(R.id.carpooling_main_zcz_contactPhone);
        catContent= (TextView) itemView.findViewById(R.id.carpooling_main_zcz_content);
        linearLayout= (LinearLayout) itemView.findViewById(R.id.car_ll_fuzhu_2);

    }
}
