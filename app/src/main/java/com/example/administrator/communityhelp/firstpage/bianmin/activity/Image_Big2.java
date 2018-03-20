package com.example.administrator.communityhelp.firstpage.bianmin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.communityhelp.R;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017/1/4.
 */
public class Image_Big2 extends AppCompatActivity {
    private PhotoView photoView;
    private PhotoViewAttacher photoViewAttacher;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_big);
        intent=getIntent();
        String picPath=intent.getStringExtra("uri");
        photoView= (PhotoView) findViewById(R.id.photoView);
        Glide.with(this)
                .load(picPath)
                .placeholder(R.mipmap.loading)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(photoView);
    }

}
