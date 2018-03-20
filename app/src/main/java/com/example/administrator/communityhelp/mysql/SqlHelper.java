package com.example.administrator.communityhelp.mysql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/12/6.
 */
public class SqlHelper extends SQLiteOpenHelper {
    public SqlHelper(Context context) {
        super(context, "db",null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tablename="create table text(_id integer primary key autoincrement,title text,masn text,userid text,uuid text)";
        db.execSQL(tablename);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
