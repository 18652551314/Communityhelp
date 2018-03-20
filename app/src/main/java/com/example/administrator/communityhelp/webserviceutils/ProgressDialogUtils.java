package com.example.administrator.communityhelp.webserviceutils;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.administrator.communityhelp.R;

public class ProgressDialogUtils extends AlertDialog{
    private static ProgressDialog mProgressDialog;

    //旋转动画
    private AnimationDrawable animation;
    private ImageView imageView;
    private View view;

    public ProgressDialogUtils(Context context) {
        super(context,R.style.MyDialog);
    }
    public  static ProgressDialogUtils getDialog(Context context){
        return new ProgressDialogUtils(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progressdialog_view);
        imageView = (ImageView) findViewById(R.id.imageView);
//        //加载动画资源
//        animation = AnimationUtils.loadAnimation(getContext(), R.anim.progress_rotate);
//        //动画完成后，是否保留动画最后的状态，设为true
//        animation.setFillAfter(true);
        //加载动画资源
        animation = (AnimationDrawable) imageView.getDrawable();
        view = getLayoutInflater().inflate(R.layout.progressdialog_view, null);
        view.getBackground().setAlpha(150);
    }

    /**
     * 显示ProgressDialog
     *
     * @param context
     * @param message
     */
    public static void showProgressDialog(Context context, CharSequence message) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(context, "", message);
            mProgressDialog.setCancelable(true);//设置进度条是否可以按退回键取消
            mProgressDialog.setCanceledOnTouchOutside(true);//设置点击进度对话框外的区域对话框消失
        } else {
            mProgressDialog.show();
        }
    }

    /**
     * 关闭ProgressDialog
     */
    public static void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * 在AlertDialog的 onStart() 生命周期里面执行开始动画
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (animation != null) {
//            imageView.startAnimation(animation);
            animation.start();
        }
    }

    /**
     * 在AlertDialog的onStop()生命周期里面执行停止动画
     */
    @Override
    protected void onStop() {
        super.onStop();
//        imageView.clearAnimation();
        animation.stop();
    }
}
