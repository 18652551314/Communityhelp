package com.example.administrator.communityhelp.myholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;

/**
 * Created by Administrator on 2016/12/14.
 */
public class GuideActivityRecycleViewHolder extends RecyclerView.ViewHolder{
    public TextView textView_name;
    public GuideActivityRecycleViewHolder(View itemView) {
        super(itemView);
        textView_name= (TextView) itemView.findViewById(R.id.textView_name);
    }
}
