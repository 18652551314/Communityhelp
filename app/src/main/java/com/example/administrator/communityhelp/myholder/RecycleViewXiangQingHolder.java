package com.example.administrator.communityhelp.myholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;

/**
 * Created by Administrator on 2016/12/14.
 */
public class RecycleViewXiangQingHolder extends RecyclerView.ViewHolder{
    public TextView textView_title,textView_context;
    public RecycleViewXiangQingHolder(View itemView) {
        super(itemView);
        textView_title= (TextView) itemView.findViewById(R.id.textView_title);
        textView_context= (TextView) itemView.findViewById(R.id.textView_context);
    }
}
