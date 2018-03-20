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
public class LvYou_ViewHolder extends RecyclerView.ViewHolder{
    public TextView textView_delete;
    public TextView title,contact,contactPhone;
    public ImageView picPath,imageView_checkState;
    public LinearLayout linearLayout;
    public LvYou_ViewHolder(View itemView) {
        super(itemView);
        textView_delete= (TextView) itemView.findViewById(R.id.textView_myTraveldelete);
        imageView_checkState= (ImageView) itemView.findViewById(R.id.imageView_checkState_travel);
        title= (TextView) itemView.findViewById(R.id.lvyou_main_adp_title);
        contact= (TextView) itemView.findViewById(R.id.lvyou_main_adp_contact);
        contactPhone= (TextView) itemView.findViewById(R.id.lvyou_main_adp_contactPhone);
        picPath= (ImageView) itemView.findViewById(R.id.lvyou_main_adp_picPath);

        linearLayout= (LinearLayout) itemView.findViewById(R.id.lvyou_ll);
    }
}
