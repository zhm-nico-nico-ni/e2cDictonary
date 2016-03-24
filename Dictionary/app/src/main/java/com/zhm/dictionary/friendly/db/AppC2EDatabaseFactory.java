package com.zhm.dictionary.friendly.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

public class AppC2EDatabaseFactory {

    /**
     * The singleton helper instance to get the sqlite database connection.
     */
    private static AppC2ESQLiteOpenHelper sqliteHelper = null;

    /**
     * Private constructor to prevent from instantiation.
     */
    private AppC2EDatabaseFactory() {
    }

    /**
     * Initialize this factory with the specified <code>Context</code>.
     *
     * @param context the specified <code>Context</code> to get database
     */
    public synchronized static void Init(Context context) {
        if (sqliteHelper == null) {
            //避免多线程时可能创建多个instance
            synchronized (AppC2EDatabaseFactory.class) {
                if (sqliteHelper == null) {
                    sqliteHelper = new AppC2ESQLiteOpenHelper(context);
                    try {
                        sqliteHelper.createDataBase();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * @return
     */
    public synchronized static SQLiteDatabase getDatabase() {
        if (sqliteHelper == null) {
            throw new IllegalStateException("YYCallDatabaseFactory is not inited.");
        }

        return sqliteHelper.openDataBase();
    }

    public synchronized static SQLiteDatabase getC2EDatabase() {
        if (sqliteHelper == null) {
            throw new IllegalStateException("YYCallDatabaseFactory is not inited.");
        }

        return sqliteHelper.openDataBase();
    }
}
