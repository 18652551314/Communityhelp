package com.example.administrator.communityhelp.mysql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2017/1/12.
 */
public class Mysqlhelp {
    SQLiteDatabase dbWriter;
    SQLiteDatabase dbReader;
    SqlHelper helper;

    public Mysqlhelp(Context context) {
        this.dbWriter=dbWriter;
        this.dbReader = dbReader;
        this.helper = helper;
    }
}
