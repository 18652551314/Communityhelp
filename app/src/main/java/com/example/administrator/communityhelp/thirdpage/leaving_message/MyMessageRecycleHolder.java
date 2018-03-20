package com.example.administrator.communityhelp.thirdpage.leaving_message;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.my_other.CircularImage;

/**
 * Created by Administrator on 2017/1/6.
 */
public class MyMessageRecycleHolder extends RecyclerView.ViewHolder {
    ImageView imageView_headPic;
    TextView textView_name;
    TextView textView_time;
    TextView textView_message;
    ImageView imageView_sound;
    ImageView imageView_mes01;
    ImageView imageView_mes02;
    ImageView imageView_mes03;
    ImageView imageView_mes04;
    ImageView imageView_mes05;
    TextView textView_sound;
    public MyMessageRecycleHolder(View itemView) {
        super(itemView);
        imageView_headPic= (CircularImage) itemView.findViewById(R.id.imageView_myMessage_headPic);
        imageView_mes01= (ImageView) itemView.findViewById(R.id.imageView_myMessage_Pic1);
        imageView_mes02= (ImageView) itemView.findViewById(R.id.imageView_myMessage_Pic2);
        imageView_mes03= (ImageView) itemView.findViewById(R.id.imageView_myMessage_Pic3);
        imageView_mes04= (ImageView) itemView.findViewById(R.id.imageView_myMessage_Pic4);
        imageView_mes05= (ImageView) itemView.findViewById(R.id.imageView_myMessage_Pic5);
        textView_name= (TextView) itemView.findViewById(R.id.textView_myMessage_name);
        textView_time= (TextView) itemView.findViewById(R.id.textView_myMessage_time);
        textView_message= (TextView) itemView.findViewById(R.id.textView_myMessage_text);
        imageView_sound= (ImageView) itemView.findViewById(R.id.imageView_myMessage_playSound);
        textView_sound= (TextView) itemView.findViewById(R.id.text_recordertime);
    }
}
