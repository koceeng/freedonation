package com.koceeng.freedonation.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteInit extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
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

            // create table content_active
            db.execSQL("CREATE TABLE IF NOT EXISTS content_active(" +
                    "_id INTEGER PRIMARY KEY, " +
                    "timestamp INTEGER, " +
                    "title TEXT, " +
                    "subtitle TEXT, " +
                    "text TEXT, " +
                    "footer TEXT, " +
                    "source TEXT " +
                    "); ");

            // create table alarm
            db.execSQL("CREATE TABLE IF NOT EXISTS alarm(" +
                    "_id INTEGER PRIMARY KEY, " +
                    "hour_of_day INTEGER NOT NULL, " +
                    "minute INTEGER NOT NULL " +
                    "); ");

            // create table faq
            db.execSQL("CREATE TABLE IF NOT EXISTS faq(" +
                    "_id INTEGER PRIMARY KEY, " +
                    "question TEXT NOT NULL UNIQUE, " +
                    "answer TEXT NOT NULL" +
                    "); ");
        }

        if (oldVersion <= 1) {
            // create table changelist
            db.execSQL("CREATE TABLE IF NOT EXISTS changelog(" +
                    "_id INTEGER PRIMARY KEY, " +
                    "version_code TEXT NOT NULL, " +
                    "version_name TEXT NOT NULL, " +
                    "version_critical BOOLEAN, " +
                    "kind TEXT NOT NULL, " +
                    "type TEXT NOT NULL, " +
                    "note TEXT NOT NULL " +
                    "); ");
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
