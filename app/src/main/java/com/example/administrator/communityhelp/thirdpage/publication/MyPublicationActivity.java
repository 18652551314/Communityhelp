package com.example.administrator.communityhelp.thirdpage.publication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.linliquan.carpooling.Carpooling_Main;
import com.example.administrator.communityhelp.firstpage.linliquan.forum.Forum_Main;
import com.example.administrator.communityhelp.firstpage.linliquan.housing_rental.Housing_Rental;
import com.example.administrator.communityhelp.firstpage.linliquan.look_for_sth.Look_For_Sth_Main;
import com.example.administrator.communityhelp.firstpage.linliquan.recruit.Recruit_Main;
import com.example.administrator.communityhelp.firstpage.linliquan.second_hand.Second_Hand_Main;
import com.example.administrator.communityhelp.firstpage.linliquan.tourism.Tourism_Main;
import com.example.administrator.communityhelp.thirdpage.publication.bbs.MyBbsActivity;

public class MyPublicationActivity extends AppCompatActivity {
    LinearLayout layoutBack;
    LinearLayout layoutBbs;
    LinearLayout layoutSecondhand;
    LinearLayout layoutTraveling;
    LinearLayout layoutCarShare;
    LinearLayout layoutJob;
    LinearLayout layoutLostGoods;
    LinearLayout layoutRoomBussiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publication);
        init();
        setClick();
    }

    private void setClick() {
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        layoutBbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPublicationActivity.this, MyForum_Main.class);
                startActivity(intent);
            }
        });
        layoutSecondhand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPublicationActivity.this, MySecond_Hand_Main.class);
                intent.putExtra("String", "Secondhand");
                startActivity(intent);
            }
        });
        layoutTraveling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPublicationActivity.this, MyTourism_Main.class);
                startActivity(intent);
            }
        });
        layoutCarShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPublicationActivity.this, MyCarpooling_Main.class);
                startActivity(intent);
            }

        });
        layoutJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPublicationActivity.this, MyRecruit_Main.class);
                startActivity(intent);
            }
        });
        layoutLostGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPublicationActivity.this, MyLook_For_Sth_Main.class);
                startActivity(intent);
            }
        });
        layoutRoomBussiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPublicationActivity.this, MyHousing_Rental.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        layoutBack = (LinearLayout) findViewById(R.id.back_layout_myPublication);
        layoutBbs = (LinearLayout) findViewById(R.id.linearLayout_myBbs);
        layoutSecondhand = (LinearLayout) findViewById(R.id.linearLayout_mySecondhand_goods);
        layoutTraveling = (LinearLayout) findViewById(R.id.linearLayout_myTraveling);
        layoutCarShare = (LinearLayout) findViewById(R.id.linearLayout_myCarShare);
        layoutJob = (LinearLayout) findViewById(R.id.linearLayout_jobResourse);
        layoutLostGoods = (LinearLayout) findViewById(R.id.linearLayout_lostGoods);
        layoutRoomBussiness = (LinearLayout) findViewById(R.id.linearLayout_roomBussiness);

    }
}
