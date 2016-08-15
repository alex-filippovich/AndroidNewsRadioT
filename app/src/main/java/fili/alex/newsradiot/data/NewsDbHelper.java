package fili.alex.newsradiot.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fili.alex.newsradiot.model.NewsObject;


public class NewsDbHelper {
    private static final String TAG = "DbHelper";

    public static final String DATABASE_NAME = "news.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteDatabase db;
    public DbHelper dbHelper;

    public NewsDbHelper(Context context) {
        dbHelper = new DbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public NewsDbHelper open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public NewsDbHelper openForRead() throws SQLException {
        db = dbHelper.getReadableDatabase();
        return this;
    }

    public void addAll(List<NewsObject> newsObjects) {
        long startTime = System.currentTimeMillis();

        SQLiteStatement stmt = db.compileStatement(QUERY_INSERT_NEWS);

        try {
            db.beginTransaction();

            for (NewsObject newsObject : newsObjects) {
                stmt.bindString(1, nullToString(newsObject.id));
                stmt.bindString(2, nullToString(newsObject.title));
                stmt.bindString(3, nullToString(newsObject.content));
                stmt.bindString(4, nullToString(newsObject.snippet));
                stmt.bindString(5, nullToString(newsObject.pic));
                stmt.bindString(6, nullToString(newsObject.author));
                stmt.bindString(7, nullToString(newsObject.link));
                stmt.bindString(8, nullToString(newsObject.ts));
                stmt.bindString(9, nullToString(newsObject.ats));
                stmt.bindLong(10, boolToLong(newsObject.active));
                stmt.bindString(11, nullToString(newsObject.activets));
                stmt.bindLong(12, boolToLong(newsObject.geek));
                stmt.bindLong(13, newsObject.votes);
                stmt.bindLong(14, boolToLong(newsObject.del));
                stmt.bindString(15, nullToString(newsObject.slug));
                stmt.bindString(16, nullToString(newsObject.exttitle));
                stmt.bindLong(17, newsObject.comments);
                stmt.bindLong(18, newsObject.likes);
                stmt.bindString(19, nullToString(newsObject.origlink));
                stmt.bindString(20, nullToString(newsObject.domain));
                stmt.bindLong(21, newsObject.position);

                stmt.executeInsert();
                stmt.clearBindings();
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        long endTime = startTime - System.currentTimeMillis();
        Log.d(TAG,"addAll time" + endTime);
    }

    public List<NewsObject> getAll() {
        List<NewsObject> newsObjects = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s", NEWS_TABLE), null);

        if (cursor.getCount() > 0) {
            NewsObject newsObject;

            cursor.moveToFirst();
            do {
                newsObject = new NewsObject();

                newsObject._id = cursor.getLong(0);
                newsObject.id = cursor.getString(1);
                newsObject.title = cursor.getString(2);
                newsObject.content = cursor.getString(3);
                newsObject.snippet = cursor.getString(4);
                newsObject.pic = cursor.getString(5);
                newsObject.author = cursor.getString(6);
                newsObject.link = cursor.getString(7);
                newsObject.ts = cursor.getString(8);
                newsObject.ats = cursor.getString(9);
                newsObject.active = longToBool(cursor.getLong(10));
                newsObject.activets = cursor.getString(11);
                newsObject.geek = longToBool(cursor.getLong(12));
                newsObject.votes = (int) cursor.getLong(13);
                newsObject.del = longToBool(cursor.getLong(14));
                newsObject.slug = cursor.getString(15);
                newsObject.exttitle = cursor.getString(16);
                newsObject.comments = (int) cursor.getLong(17);
                newsObject.likes = (int) cursor.getLong(18);
                newsObject.origlink = cursor.getString(19);
                newsObject.domain = cursor.getString(20);
                newsObject.position = (int) cursor.getLong(21);

                newsObjects.add(newsObject);
            } while (cursor.moveToNext());

            cursor.close();
        }

        long endTime = startTime - System.currentTimeMillis();
        Log.d(TAG,"getAll time" + endTime);

        return newsObjects;
    }

    void update(NewsObject newsObject) {

    }

    public void close() {
        db.close();
    }

    private int boolToLong(boolean value) {
        return value ? 1 : 0;
    }

    private boolean longToBool(long value) {
        return value == 1;
    }

    private String nullToString(String value) {
        return value == null ? "" : value;
    }


    private static class DbHelper extends SQLiteOpenHelper {


        public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_NEWS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public static final String NEWS_TABLE = "news_list";

    private static final String CREATE_TABLE_NEWS =
            "CREATE TABLE " + NEWS_TABLE + "(" +
                    NewsObject.LOCAL_ID + " integer primary key autoincrement," +
                    NewsObject.ID + " text UNIQUE ON CONFLICT REPLACE," +
                    NewsObject.TITLE + " text," +
                    NewsObject.CONTENT + " text," +
                    NewsObject.SNIPPET + " text," +
                    NewsObject.PIC + " text," +
                    NewsObject.AUTHOR + " text," +
                    NewsObject.LINK + " text," +
                    NewsObject.TS + " text," +
                    NewsObject.ATS + " text," +
                    NewsObject.ACTIVE + " integer not null default 0," +
                    NewsObject.ACTIVETS + " TEXT," +
                    NewsObject.GEEK + " integer not null default 0," +
                    NewsObject.VOTES + " integer not null default 0," +
                    NewsObject.DEL + " integer not null default 0," +
                    NewsObject.SLUG + " text," +
                    NewsObject.EXTTITLE + " text," +
                    NewsObject.COMMENTS + " integer not null default 0," +
                    NewsObject.LIKES + " integer not null default 0," +
                    NewsObject.ORIGLINK + " text," +
                    NewsObject.DOMAIN + " text," +
                    NewsObject.POSITION + " integer not null default 0" +
                    ")";

    private static final String QUERY_INSERT_NEWS = "INSERT INTO " + NEWS_TABLE +
            " ("
            + "'" + NewsObject.ID + "',"
            + "'" + NewsObject.TITLE + "',"
            + "'" + NewsObject.CONTENT + "',"
            + "'" + NewsObject.SNIPPET + "',"
            + "'" + NewsObject.PIC + "',"
            + "'" + NewsObject.AUTHOR + "',"
            + "'" + NewsObject.LINK + "',"
            + "'" + NewsObject.TS + "',"
            + "'" + NewsObject.ATS + "',"
            + "'" + NewsObject.ACTIVE + "',"
            + "'" + NewsObject.ACTIVETS + "',"
            + "'" + NewsObject.GEEK + "',"
            + "'" + NewsObject.VOTES + "',"
            + "'" + NewsObject.DEL + "',"
            + "'" + NewsObject.SLUG + "',"
            + "'" + NewsObject.EXTTITLE + "',"
            + "'" + NewsObject.COMMENTS + "',"
            + "'" + NewsObject.LIKES + "',"
            + "'" + NewsObject.ORIGLINK + "',"
            + "'" + NewsObject.DOMAIN + "',"
            + "'" + NewsObject.POSITION + "'"

            + ") VALUES(?1, ?2, ?3, ?4, ?5,"
            + "?6, ?7, ?8, ?9, ?10, ?11, ?12,"
            + " ?13, ?14, ?15, ?16, ?17, ?18, ?19, ?20, ?21)";
}
