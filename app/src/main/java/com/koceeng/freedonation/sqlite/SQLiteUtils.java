package com.koceeng.freedonation.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.BaseColumns;

import com.koceeng.freedonation.alarm.AlarmObject;
import com.koceeng.freedonation.help.Faq;
import com.koceeng.freedonation.object.Content;
import com.koceeng.freedonation.util.DebugUtil;

import java.util.ArrayList;
import java.util.List;

public class SQLiteUtils {

    public static final String PARAM_LAST_FAQ_TIMESTAMP = "PARAM_LAST_FAQ_TIMESTAMP";
    private static SQLiteUtils sqLiteDatabaseHelper = null;
    private SQLiteInit sqLiteHelper = null;
    private SQLiteDatabase sqLiteDatabase = null;

    public SQLiteUtils(Context context) {
        if (sqLiteHelper == null)
            sqLiteHelper = new SQLiteInit(context);
    }

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
        if (!(content.getSource() != null ? content.getSource() : "").equals(""))
            cv.put("source", content.getSource());

        sqLiteDatabase.insertWithOnConflict("content_active", "_id", cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public Content getContent() {
        openReadable();

        Content result = null;

        Cursor c = sqLiteDatabase.rawQuery("SELECT timestamp, title, subtitle, text, footer, source FROM content_active WHERE _id = 0;", null);
        while (c.moveToNext()) {
            result = new Content();
            result.setTimestamp(c.getLong(c.getColumnIndex("timestamp")));
            result.setTitle(c.getString(c.getColumnIndex("title")));
            result.setSubtitle(c.getString(c.getColumnIndex("subtitle")));
            result.setText(c.getString(c.getColumnIndex("text")));
            result.setFooter(c.getString(c.getColumnIndex("footer")));
            result.setSource(c.getString(c.getColumnIndex("source")));
        }
        c.close();

        return result;
    }

    public AlarmObject putAlarm(AlarmObject alarmObject) {
        openWritable();
        if (alarmObject == null)
            return null;

        ContentValues cv = new ContentValues();
        cv.put("hour_of_day", alarmObject.getHourOfDay());
        cv.put("minute", alarmObject.getMinute());

        Long result = sqLiteDatabase.insert("alarm", null, cv);
        alarmObject.setPendingIntentRequestCode(result.intValue());

        return alarmObject;
    }

    public Integer getAlarmCount() {
        openReadable();

        Integer result = 0;

        Cursor c = sqLiteDatabase.rawQuery("SELECT COUNT(_id) AS count FROM alarm", null);
        while (c.moveToNext()) {
            result = c.getInt(0);
        }
        c.close();

        return result;
    }

    public AlarmObject getAlarmByHourAndMinute(int hourOfDay, int minute) {
        openReadable();

        AlarmObject result = null;

        Cursor c = sqLiteDatabase.rawQuery("SELECT _id, hour_of_day, minute FROM alarm WHERE hour_of_day = @hour AND minute = @minute LIMIT 1",
                new String[]{String.valueOf(hourOfDay), String.valueOf(minute)});
        while (c.moveToNext()) {
            result = new AlarmObject(c.getInt(c.getColumnIndex("hour_of_day")), c.getInt(c.getColumnIndex("minute")));
            result.setId(c.getInt(c.getColumnIndex("_id")));
            result.setPendingIntentRequestCode(c.getInt(c.getColumnIndex("_id")));
        }
        c.close();

        return result;
    }

    public List<AlarmObject> getAlarms() {
        openReadable();

        List<AlarmObject> result = new ArrayList<>();

        Cursor c = sqLiteDatabase.rawQuery("SELECT _id, hour_of_day, minute FROM alarm", null);
        while (c.moveToNext()) {
            AlarmObject alarmObject = new AlarmObject(c.getInt(c.getColumnIndex("hour_of_day")), c.getInt(c.getColumnIndex("minute")));
            alarmObject.setId(c.getInt(c.getColumnIndex("_id")));
            alarmObject.setPendingIntentRequestCode(c.getInt(c.getColumnIndex("_id")));
            result.add(alarmObject);
        }
        c.close();

        return result;
    }

    public void removeAlarm(Integer id) {
        openWritable();
        sqLiteDatabase.delete("alarm", "_id = @id", new String[]{id.toString()});
    }

    public void putFaqs(List<Faq> faqs) {
        openWritable();

        // clear data
        sqLiteDatabase.delete("faq", null, null);

        if (faqs == null || faqs.size() <= 0)
            return;

        ContentValues cv;
        for (Faq faq : faqs) {
            cv = new ContentValues();
            cv.put("question", faq.getQuestion());
            cv.put("answer", faq.getAnswer());

            sqLiteDatabase.insertWithOnConflict("faq", "question", cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public List<Faq> getFaqs() {
        openReadable();

        List<Faq> result = new ArrayList<>();

        Cursor c = sqLiteDatabase.rawQuery("SELECT question, answer FROM faq", null);
        while (c.moveToNext())
            result.add(new Faq(c.getString(c.getColumnIndex("question")), c.getString(c.getColumnIndex("answer"))));
        c.close();

        return result;
    }
}
