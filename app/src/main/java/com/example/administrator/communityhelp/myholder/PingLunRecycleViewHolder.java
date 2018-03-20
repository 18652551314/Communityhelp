package com.example.administrator.communityhelp.myholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;

/**
 * Created by Administrator on 2016/12/14.
 */
public class PingLunRecycleViewHolder extends RecyclerView.ViewHolder {
    public TextView textView_userName, textView_date, textView_pinglun;

    public PingLunRecycleViewHolder(View itemView) {
        super(itemView);
        textView_userName = (TextView) itemView.findViewById(R.id.textView_userName);
        textView_date = (TextView) itemView.findViewById(R.id.textView_date);
        textView_pinglun = (TextView) itemView.findViewById(R.id.textView_pinglun);
    }
}
