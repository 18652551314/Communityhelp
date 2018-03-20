package com.example.administrator.communityhelp.firstpage.bianmin.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.my_other.GlideLoader;

import java.io.IOException;
import java.io.InputStream;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017/1/4.
 */
public class Image_Big extends AppCompatActivity {
    private PhotoView photoView;
    private PhotoViewAttacher photoViewAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_big);
        String picPath=this.getIntent().getStringExtra("picPath");
        photoView= (PhotoView) findViewById(R.id.photoView);
        Glide.with(this)
                .load(picPath)
                .placeholder(R.mipmap.loading)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(photoView);
    }

}
