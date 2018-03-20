package com.example.administrator.communityhelp.myholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;

/**
 * Created by Administrator on 2016/12/14.
 */
public class TelephoneActivityRecycleViewHolder extends RecyclerView.ViewHolder{
    public TextView textView_name,textView_phone;
    public TelephoneActivityRecycleViewHolder(View itemView) {
        super(itemView);
        textView_name= (TextView) itemView.findViewById(R.id.textView_name);
        textView_phone= (TextView) itemView.findViewById(R.id.textView_phone);
    }
}
