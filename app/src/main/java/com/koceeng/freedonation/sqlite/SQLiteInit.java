package com.koceeng.freedonation.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteInit extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "koceeng.free.donation.db";

    SQLiteInit(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        onUpgrade(db, 0, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion <= 0) {
            // create table params
            db.execSQL("CREATE TABLE IF NOT EXISTS params(" +
                    "_id INTEGER PRIMARY KEY, " +
                    "name TEXT NOT NULL UNIQUE, " +
                    "value TEXT NOT NULL " +
                    "); ");

            // create table content
            db.execSQL("CREATE TABLE IF NOT EXISTS contents(" +
                    "_id INTEGER PRIMARY KEY, " +
                    "key TEXT NOT NULL UNIQUE, " +
                    "title TEXT, " +
                    "subtitle TEXT, " +
                    "text TEXT, " +
                    "footer TEXT " +
                    "); ");
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
