package com.example.administrator.communityhelp.myholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;

/**
 * Created by Administrator on 2016/12/14.
 */
public class SheQuActivityRecycleViewHolder extends RecyclerView.ViewHolder{
    public ImageView imageView;
    public TextView textView_name,textView_referral,textView_date;
    public SheQuActivityRecycleViewHolder(View itemView) {
        super(itemView);
        imageView= (ImageView) itemView.findViewById(R.id.recycler_image);
        textView_name= (TextView) itemView.findViewById(R.id.textView_name);
        textView_referral= (TextView) itemView.findViewById(R.id.textView_referral);
        textView_date= (TextView) itemView.findViewById(R.id.textView_date);
    }
}
