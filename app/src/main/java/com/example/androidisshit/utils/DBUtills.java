package com.example.androidisshit.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

public class DBUtills {
    private SQLiteDatabase getDB(Context context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        return  db;
    }

    public void setCumulativePlayRatio(Context context,float ratio, String uri) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("cumulativePlayRatio", ratio);
        getDB(context).update("data", null, "uri = ?", new String[] {uri});
    }

    public Map<Integer, Float> getAllCumulativePlayRatio(Context context) {
        Cursor cursor = getDB(context).query("data", null, null, null, null, null, null);
        Map<Integer, Float> ratioMap = new HashMap<Integer, Float>();
        if(cursor.moveToFirst()) {
            for(int i=0;i<cursor.getCount();i++) {
                cursor.move(i);
                Integer id = cursor.getInt(0);
                Integer uri = cursor.getInt(1);
                Float ratio = cursor.getFloat(2);
                ratioMap.put(uri,ratio);
            }
        }
        return ratioMap;
    }


}
