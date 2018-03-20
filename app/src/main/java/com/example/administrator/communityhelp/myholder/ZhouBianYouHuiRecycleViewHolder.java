package com.example.administrator.communityhelp.myholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;

/**
 * Created by Administrator on 2016/12/14.
 */
public class ZhouBianYouHuiRecycleViewHolder extends RecyclerView.ViewHolder{
    public ImageView imageView;
    public TextView textView_name,textView_referral,textView_price,textView_oldPrice,textView_shangdianName;
    public ZhouBianYouHuiRecycleViewHolder(View itemView) {
        super(itemView);
        imageView= (ImageView) itemView.findViewById(R.id.recycler_image);
        textView_name= (TextView) itemView.findViewById(R.id.textView_name);
        textView_referral= (TextView) itemView.findViewById(R.id.textView_referral);
        textView_price= (TextView) itemView.findViewById(R.id.textView_price);
        textView_oldPrice= (TextView) itemView.findViewById(R.id.textView_oldPrice);
        textView_shangdianName= (TextView) itemView.findViewById(R.id.textView_shangdianName);
    }
}
