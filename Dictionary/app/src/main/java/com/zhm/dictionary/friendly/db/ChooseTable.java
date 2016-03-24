package com.zhm.dictionary.friendly.db;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class ChooseTable implements BaseColumns {

    public static final String TABLE_NAME = "ChooseTable";

    public static final String COLUMN_ID = _ID;

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "desc";
    public static final String COLUMN_TIME = "time";
//    public static final String COLUMN_ALIASES = "aliases";

    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT UNIQUE," +
            COLUMN_DESCRIPTION + " TEXT," + COLUMN_TIME +
//            " TEXT," + COLUMN_ALIASES +
            " TEXT)";

    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

}
