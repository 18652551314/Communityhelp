package com.example.administrator.communityhelp.myholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;

/**
 * Created by Administrator on 2016/12/14.
 */
public class PinChe_One_ViewHolder extends RecyclerView.ViewHolder {
    public TextView userName, textView_delete;
    public ImageView headimgurl,imageView_checkstate;
    public TextView createTime;
    public TextView startdian;
    public TextView enddian;
    public TextView startTime;
    public TextView contact;
    public TextView contactPhone;
    public TextView carInfo;
    public TextView catContent;


    public CardView cardView;
    public LinearLayout linearLayout;

    public PinChe_One_ViewHolder(View itemView) {
        super(itemView);
        textView_delete= (TextView) itemView.findViewById(R.id.textView_carpoolDelete);
        imageView_checkstate= (ImageView) itemView.findViewById(R.id.imageView_checkState_carpool);
        userName = (TextView) itemView.findViewById(R.id.pinche_tv_id);
        headimgurl = (ImageView) itemView.findViewById(R.id.pinche_iv1);
        createTime= (TextView) itemView.findViewById(R.id.carpooling_main_zck_time);
        startdian= (TextView) itemView.findViewById(R.id.carpooling_main_zck_startdian);
        enddian= (TextView) itemView.findViewById(R.id.carpooling_main_zck_enddian);
        startTime= (TextView) itemView.findViewById(R.id.carpooling_main_zck_startTime);
        contact= (TextView) itemView.findViewById(R.id.carpooling_main_zck_contact);
        contactPhone= (TextView) itemView.findViewById(R.id.carpooling_main_zck_contactPhone);
        carInfo= (TextView) itemView.findViewById(R.id.carpooling_main_zck_carInfo);
        catContent= (TextView) itemView.findViewById(R.id.carpooling_main_zck_content);


        cardView = (CardView) itemView.findViewById(R.id.pinche_card);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.pinche_linear);
    }
}
