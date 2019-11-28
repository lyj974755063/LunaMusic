package com.example.lunamusic.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String sql = "create table data (id INTEGER primary key autoincrement, uri INTEGER(50), cumulativePlayRatio REAL(5))";

    public SQLiteHelper(Context context) {
        super(context, "songData", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
