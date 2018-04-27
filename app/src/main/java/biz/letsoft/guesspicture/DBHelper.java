package biz.letsoft.guesspicture;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "my_tag";

    public static final String TABLE_NAME = "urers";

    public static final String KEY_ID = "_id";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASS = "password";

    private static final String DATABASE_NAME = "usersDB";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" ("
                + KEY_ID + " integer primary key autoincrement,"
                + KEY_LOGIN + " text,"
                + KEY_PASS + " text" + ");");

        ContentValues cv = new ContentValues();

        cv.put(KEY_LOGIN, "Igor");
        cv.put(KEY_PASS, "email1@email.com");
        db.insert(TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }
}
