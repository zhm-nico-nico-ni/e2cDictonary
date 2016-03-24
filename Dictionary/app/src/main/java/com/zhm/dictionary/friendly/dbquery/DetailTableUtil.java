package com.zhm.dictionary.friendly.dbquery;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import com.zhm.dictionary.friendly.Util.OsUtil;
import com.zhm.dictionary.friendly.data.DetailTableInfo;
import com.zhm.dictionary.friendly.data.English2Chinese;
import com.zhm.dictionary.friendly.db.AppSelfDatabaseFactory;
import com.zhm.dictionary.friendly.db.DetailTable;

public class DetailTableUtil {

    public static void insertOrUpData(String word, String belong, String desc) {
        SQLiteDatabase db = AppSelfDatabaseFactory.getDatabase();
        String insert = String.format(
                "INSERT or REPLACE INTO " + DetailTable.TABLE_NAME + " ("
                        + DetailTable.COLUMN_WORD + ","
                        + DetailTable.COLUMN_BELONG + ","
                        + DetailTable.COLUMN_DESCRIPTION + ","
                        + DetailTable.COLUMN_COUNT + ","
                        + DetailTable.COLUMN_ADD_TIME
                        + ") VALUES (%1$s,%2$s,%3$s,"
                        + "(select 1+" + DetailTable.COLUMN_COUNT + " from "
                        + DetailTable.TABLE_NAME + " where " + DetailTable.COLUMN_WORD + "=%1$s and "
                        + DetailTable.COLUMN_BELONG + " = %2$s),"
                        + DatabaseUtils.sqlEscapeString(OsUtil.formatTime(System.currentTimeMillis())) + ")"
                , DatabaseUtils.sqlEscapeString(word)
                , DatabaseUtils.sqlEscapeString(belong)
                , DatabaseUtils.sqlEscapeString(desc));
        db.execSQL(insert);
    }

    public static void deleteWord(String word, String belong) {
        SQLiteDatabase db = AppSelfDatabaseFactory.getDatabase();
        String deleteSql = String.format(
                "delete from " + DetailTable.TABLE_NAME + " where " + DetailTable.COLUMN_WORD + " = '%s' and "
                        + DetailTable.COLUMN_BELONG + " = '%s' ;"
                , word, belong);
        db.execSQL(deleteSql);
    }

    public static ArrayList<DetailTableInfo> getByCustomTable(String belong, String groupby, String orderby) {
        SQLiteDatabase db = AppSelfDatabaseFactory.getDatabase();

        Cursor c = db.query(DetailTable.TABLE_NAME, new String[]{DetailTable.COLUMN_WORD, DetailTable.COLUMN_DESCRIPTION,
                DetailTable.COLUMN_ADD_TIME, DetailTable.COLUMN_COUNT}, DetailTable.COLUMN_BELONG + " = ?"
                , new String[]{belong}, groupby, null, orderby);
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
