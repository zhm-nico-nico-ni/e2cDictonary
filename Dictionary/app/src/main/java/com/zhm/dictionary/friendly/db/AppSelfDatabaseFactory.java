package com.zhm.dictionary.friendly.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class AppSelfDatabaseFactory {
    private static AppSelfDataBaseOpenHelper sqliteHelper = null;

    /**
     * Private constructor to prevent from instantiation.
     */
    private AppSelfDatabaseFactory() {
    }

    /**
     * Initialize this factory with the specified <code>Context</code>.
     *
     * @param context the specified <code>Context</code> to get database
     */
    public synchronized static void Init(Context context) {
        if (sqliteHelper == null) {
            //避免多线程时可能创建多个instance
            synchronized (AppSelfDatabaseFactory.class) {
                if (sqliteHelper == null) {
                    sqliteHelper = new AppSelfDataBaseOpenHelper(context);
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
        return sqliteHelper.getWritableDatabase();
    }
}
