package com.zhm.dictionary.friendly.dbquery;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import com.zhm.dictionary.friendly.Util.OsUtil;
import com.zhm.dictionary.friendly.data.DetailTableInfo;
import com.zhm.dictionary.friendly.data.English2Chinese;
import com.zhm.dictionary.friendly.db.AppSelfDatabaseFactory;
import com.zhm.dictionary.friendly.db.HistoryQueryTable;

public class HistoryQueryTableUtil {

    public static void insertOrUpData(String word, String desc) {
        SQLiteDatabase db = AppSelfDatabaseFactory.getDatabase();
        String insert = String.format(
                "INSERT or REPLACE INTO " + HistoryQueryTable.TABLE_NAME + " ("
                        + HistoryQueryTable.COLUMN_WORD + ","
                        + HistoryQueryTable.COLUMN_DESCRIPTION + ","
                        + HistoryQueryTable.COLUMN_COUNT + ","
                        + HistoryQueryTable.COLUMN_ADD_TIME
                        + ") VALUES (%1$s,%2$s,"
                        + "(select 1+" + HistoryQueryTable.COLUMN_COUNT + " from " + HistoryQueryTable.TABLE_NAME + " where "
                        + HistoryQueryTable.COLUMN_WORD + "=%1$s),"
                        + DatabaseUtils.sqlEscapeString(OsUtil.formatTime(System.currentTimeMillis())) + ")"
                , DatabaseUtils.sqlEscapeString(word), DatabaseUtils.sqlEscapeString(desc));
        db.execSQL(insert);
    }

    public static void deleteWord(String word) {
        SQLiteDatabase db = AppSelfDatabaseFactory.getDatabase();
        String deleteSql =
                "delete from " + HistoryQueryTable.TABLE_NAME + " where " + HistoryQueryTable.COLUMN_WORD +
                        " = " + DatabaseUtils.sqlEscapeString(word);
        db.execSQL(deleteSql);
    }

    public static void clear() {
        SQLiteDatabase db = AppSelfDatabaseFactory.getDatabase();
        String deleteSql =
                "delete from " + HistoryQueryTable.TABLE_NAME;
        db.execSQL(deleteSql);
    }

    public static ArrayList<DetailTableInfo> getHistory(String groupby, String orderby) {
        SQLiteDatabase db = AppSelfDatabaseFactory.getDatabase();

        Cursor c = db.query(HistoryQueryTable.TABLE_NAME, new String[]{HistoryQueryTable.COLUMN_WORD, HistoryQueryTable.COLUMN_DESCRIPTION,
                HistoryQueryTable.COLUMN_ADD_TIME, HistoryQueryTable.COLUMN_COUNT}, null, null, groupby, null, orderby);
        ArrayList<DetailTableInfo> result = new ArrayList<>();
        if (c != null) {
            while (c.moveToNext()) {
                DetailTableInfo info = new DetailTableInfo();
                info.theWord = new English2Chinese();
                info.theWord.word = c.getString(0);
                info.theWord.description = c.getString(1);
                info.addTime = c.getString(2);
                info.count = c.getInt(3);
                result.add(info);
            }

            c.close();
        }

        return result;
    }
}
