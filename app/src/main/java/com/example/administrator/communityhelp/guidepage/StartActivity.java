package com.example.administrator.communityhelp.guidepage;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.mysql.SqlHelper;

public class StartActivity extends AppCompatActivity {
    private SQLiteDatabase dbWriter;
    private SQLiteDatabase dbReader;
    private SqlHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getHelper();
        createData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Intent intent = new Intent(StartActivity.this, GuideActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void createData() {
        try {
            String SqlSentence = "create table user(id INTEGER PRIMARY KEY autoincrement," +
                    "communityCode text,userId text,state boolean,userName text,isAdmin boolean," +
                    "communityName text,uuid text,msg text) ";
            String sql = "create table community(id INTEGER PRIMARY KEY autoincrement," +
                    "communityName text,communityCode text)";
            String sqlcommlist = "create table communityList(id INTEGER PRIMARY KEY autoincrement," +
                    "communityName text,communityCode text)";
            String imagePath = "create table imagePath(id INTEGER PRIMARY KEY autoincrement," +
                    "imagePath text)";
            String userMessage = "create table userMessage(id INTEGER PRIMARY KEY autoincrement," +
                    "headImage text,nickName text,account text)";
            dbWriter.execSQL(SqlSentence);
            dbWriter.execSQL(sql);
            dbWriter.execSQL(sqlcommlist);
            dbWriter.execSQL(imagePath);
            dbWriter.execSQL(userMessage);
        } catch (Exception e) {
        }

    }

    public void getHelper() {
        //获取帮助类
        helper = new SqlHelper(this);
        //通过帮助类获得读写的权限
        dbWriter = helper.getWritableDatabase();
        dbReader = helper.getReadableDatabase();
    }
}
