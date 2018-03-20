package com.example.administrator.communityhelp.thirdpage.publication.bbs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.communityhelp.R;


public class MyBbsActivity extends AppCompatActivity {
    ImageView imageView_edit;
   LinearLayout layout_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bbs);
        init();
        setClick();
    }

    private void setClick() {
        imageView_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MyBbsActivity.this,CommunityActivity.class);
                startActivity(intent);
            }
        });
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        imageView_edit= (ImageView) findViewById(R.id.imageView_editBbs);
        layout_back= (LinearLayout) findViewById(R.id.back_layout_myPublication);
    }
}
