package com.example.administrator.communityhelp.my_other;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.communityhelp.R;
import com.yancy.imageselector.ImageLoader;

//优化处理并且能够调用摄像机拍照裁剪功能类
public class GlideLoader implements ImageLoader {
    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                //.placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                .placeholder(R.mipmap.loading)
                .centerCrop()
                .error(R.mipmap.no_pic)
                .into(imageView);
    }

}
