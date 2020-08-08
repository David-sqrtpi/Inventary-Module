package com.sdandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLite extends SQLiteOpenHelper {

    public AdminSQLite(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int version){

        super(context, nombre, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE producto (code integer primary key, nombre text, precio integer);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS producto");
        db.execSQL("CREATE TABLE producto (code integer primary key, nombre text, precio integer);");

    }

}
