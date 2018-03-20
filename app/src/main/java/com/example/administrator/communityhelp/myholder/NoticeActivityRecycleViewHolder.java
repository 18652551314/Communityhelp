package com.example.administrator.communityhelp.myholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;

/**
 * Created by Administrator on 2016/12/14.
 */
public class NoticeActivityRecycleViewHolder extends RecyclerView.ViewHolder{
    public TextView textView_name,textView_date,textView_content;
    public NoticeActivityRecycleViewHolder(View itemView) {
        super(itemView);
        textView_name= (TextView) itemView.findViewById(R.id.notices_title);
        textView_date= (TextView) itemView.findViewById(R.id.notices_date);
        textView_content= (TextView) itemView.findViewById(R.id.text_content);
    }
}
