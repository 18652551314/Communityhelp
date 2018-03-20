package com.example.administrator.communityhelp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by Administrator on 2016/12/29.
 */
public class ImageScale {
    private Context context;
    private CardView cardView;
    private Intent intent;

    public ImageScale(Context context, CardView cardView, Intent intent) {
        this.context = context;
        this.cardView = cardView;
        this.intent = intent;
    }

    public void animate(){
        cardView.setClickable(true);
        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Animation scaleshrink= AnimationUtils.loadAnimation(context, R.anim.scaleshrink);
                        scaleshrink.setFillAfter(true);
                        v.startAnimation(scaleshrink);
                        v.setPressed(true);
//                        v.animate().scaleX(0.8f).scaleY(0.8f);
                        return true;
                    case MotionEvent.ACTION_UP:
                        float eventX=event.getX();
                        float eventY=event.getY();
                        Animation scaleenlarge= AnimationUtils.loadAnimation(context, R.anim.scaleenlarge);
                        scaleenlarge.setFillAfter(true);
                        v.startAnimation(scaleenlarge);
                        v.setPressed(false);
                        if(eventX>0&&eventX<v.getWidth()&&eventY>0&&eventY<v.getHeight()){
                            context.startActivity(intent);
                        }
                        return true;
                }
                return true;
            }
        });
    }
}
