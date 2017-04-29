package com.koceeng.freedonation.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.BaseColumns;

import com.koceeng.freedonation.object.Content;
import com.koceeng.freedonation.util.DebugUtil;

public class SQLiteUtils {

    public static final String PARAM_CONTENT_UPDATE_CODE = "PARAM_CONTENT_UPDATE_CODE";

    private SQLiteInit sqLiteHelper = null;
    private SQLiteDatabase sqLiteDatabase = null;

    public SQLiteUtils(Context context) {
        if (sqLiteHelper == null)
            sqLiteHelper = new SQLiteInit(context);
    }

    public void openReadable() throws SQLiteException {
        if (sqLiteDatabase == null)
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
    }

    public void openWritable() throws SQLiteException {
        if ((sqLiteDatabase == null) || sqLiteDatabase.isReadOnly()) {
            openWritable(true);
        }
    }

    public void openWritable(boolean foreignKeys) throws SQLException {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        if (foreignKeys) {
            sqLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
        } else {
            sqLiteDatabase.execSQL("PRAGMA foreign_keys = OFF;");
        }
    }

    public void close(){
        if (sqLiteDatabase != null){
            sqLiteDatabase.close();
            sqLiteDatabase = null;
        }

        if (sqLiteHelper!= null){
            sqLiteHelper.close();
            sqLiteHelper = null;
        }
    }

    public void executeQuery(String query) {
        openWritable();

        sqLiteDatabase.execSQL(query);
    }

    public void putStringParam(String paramName, String value) {
        openWritable();
        if (paramName == null || paramName.isEmpty() || value == null || value.isEmpty())
            return;

        ContentValues cv = new ContentValues();
        cv.put("name", paramName);
        cv.put("value", value);

        sqLiteDatabase.insertWithOnConflict("params", "name", cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void removeStringParam(String paramName) {
        openWritable();
        sqLiteDatabase.delete("params", "name = @name", new String[]{paramName});
    }

    public String getStringParam(String paramName) {
        openReadable();

        String result = null;

        Cursor c = sqLiteDatabase.rawQuery("SELECT value FROM params WHERE name = @name;", new String[]{paramName});
        while (c.moveToNext()) {
            result = c.getString(c.getColumnIndex("value"));
        }
        c.close();

        return result;
    }

    public void putContent(Content content) {
        openWritable();
        if (content == null)
            return;

        ContentValues cv = new ContentValues();
        cv.put(BaseColumns._ID, 0);
        if ((content.getTimestamp() != null ? content.getTimestamp() : 0) != 0)
            cv.put("timestamp", content.getTimestamp());
        if (!(content.getTitle() != null ? content.getTitle() : "").equals(""))
            cv.put("title", content.getTitle());
        if (!(content.getSubtitle() != null ? content.getSubtitle() : "").equals(""))
            cv.put("subtitle", content.getSubtitle());
        if (!(content.getText() != null ? content.getText() : "").equals(""))
            cv.put("text", content.getText());
        if (!(content.getFooter() != null ? content.getFooter() : "").equals(""))
            cv.put("footer", content.getFooter());

        sqLiteDatabase.insertWithOnConflict("content_active", "_id", cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public Content getContent() {
        openReadable();

        Content result = null;

        Cursor c = sqLiteDatabase.rawQuery("SELECT timestamp, title, subtitle, text, footer FROM content_active WHERE _id = 0;", null);
        while (c.moveToNext()) {
            result = new Content();
            result.setTimestamp(c.getLong(c.getColumnIndex("timestamp")));
            result.setTitle(c.getString(c.getColumnIndex("title")));
            result.setSubtitle(c.getString(c.getColumnIndex("subtitle")));
            result.setText(c.getString(c.getColumnIndex("text")));
            result.setFooter(c.getString(c.getColumnIndex("footer")));
        }
        c.close();

        return result;
    }

    private static SQLiteUtils sqLiteDatabaseHelper = null;

    public static SQLiteUtils getInstance(Context context) {
        if (sqLiteDatabaseHelper == null) {
            if (context != null) {
                sqLiteDatabaseHelper = new SQLiteUtils(context);
            } else {
                DebugUtil.getInstance().e("SQLiteUtils", "SQLite creation cancelled, context is null");
            }
        }

        return sqLiteDatabaseHelper;
    }
}
