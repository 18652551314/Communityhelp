package com.example.administrator.communityhelp.my_other;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.administrator.communityhelp.R;

public class ProgressDialogAon extends AlertDialog{
    //旋转动画
    private Animation animation;
    private ImageView imageView;

    public ProgressDialogAon(Context context) {
        super(context,R.style.MyDialogAon);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progressdialog_view_ano);
        imageView = (ImageView) findViewById(R.id.imageView);
        //加载动画资源
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.progress_rotate);
        //动画完成后，是否保留动画最后的状态，设为true
        animation.setFillAfter(true);
    }

    /**
     * 在AlertDialog的 onStart() 生命周期里面执行开始动画
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (animation != null) {
            imageView.startAnimation(animation);
        }
    }

    /**
     * 在AlertDialog的onStop()生命周期里面执行停止动画
     */
    @Override
    protected void onStop() {
        super.onStop();
        imageView.clearAnimation();
    }
}
