package com.zhm.dictionary.friendly.db;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DetailTable implements BaseColumns {

    public static final String TABLE_NAME = "DetailTable";

    public static final String COLUMN_ID = _ID;

    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_COUNT = "word_count";
    public static final String COLUMN_BELONG = "belong";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ADD_TIME = "add_time";

    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_WORD + " TEXT," +
            COLUMN_BELONG + " TEXT," + COLUMN_COUNT + " INTEGER default 1 not null," +
            COLUMN_DESCRIPTION + " TEXT, " + COLUMN_ADD_TIME + " TEXT)";

    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
        String createUniqueIndex = "CREATE UNIQUE INDEX IF NOT EXISTS idx_belong_word ON %s(%s,%s)";
        sqLiteDatabase.execSQL(String.format(createUniqueIndex, TABLE_NAME, COLUMN_BELONG, COLUMN_WORD));
    }
}
