package com.example.administrator.communityhelp.thirdpage.community;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.Community_Choice;
import com.example.administrator.communityhelp.R;

public class MyCommunityActivity extends BaseActivity {
    LinearLayout linearLayout_back;
    ImageView imageView;
    TextView textView_SelectedCommunity;
    TextView textView_ChangeCommunity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_community);
        init();
        linearLayout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //预留方法获取社当前区信息
        String name=getCommunity();
        textView_SelectedCommunity.setText(name);
        textView_ChangeCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MyCommunityActivity.this, Community_Choice.class);
                intent.putExtra("aa","aa");
                startActivity(intent);
            }
        });
    }

    public void init() {
        linearLayout_back= (LinearLayout) findViewById(R.id.back_layout_myCommunity);
        imageView= (ImageView) findViewById(R.id.imagView_selected_community);
        textView_SelectedCommunity= (TextView) findViewById(R.id.textView_selected_community);
        textView_ChangeCommunity= (TextView) findViewById(R.id.textView_changeCommunity);
    }

    public String getCommunity() {
        String Communityname=getUserData().getCommunityName();
        return Communityname;
    }

    @Override
    protected void onStart() {
        super.onStart();
        String name=getCommunity();
        textView_SelectedCommunity.setText(name);
    }
}
