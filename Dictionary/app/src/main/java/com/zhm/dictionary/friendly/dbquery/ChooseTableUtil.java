package com.zhm.dictionary.friendly.dbquery;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.zhm.dictionary.R;
import com.zhm.dictionary.friendly.Util.OsUtil;
import com.zhm.dictionary.friendly.data.CustomTableInfo;
import com.zhm.dictionary.friendly.db.AppSelfDatabaseFactory;
import com.zhm.dictionary.friendly.db.ChooseTable;
import com.zhm.dictionary.friendly.db.DetailTable;

/**
 * Created by zhm on 2016/3/15.
 */
public class ChooseTableUtil {
    public final static String ChooseTableSharePref = "ChooseTableUtil";
    public final static String DEFAULT_NEW_WORD_TABLE = "DEFAULT_NEW_WORD_TABLE";
    public final static String DEFAULT_NEW_WORD = "Default new word";

    public static void addCustomTableIFNotExist(String tableName, String desc) {
        SQLiteDatabase db = AppSelfDatabaseFactory.getDatabase();
//        String insert = String.format(
//                "INSERT or IGNORE INTO ChooseTable (name,desc,time) VALUES (%s,%s,%s)"
//                , DatabaseUtils.sqlEscapeString(tableName)
//                , DatabaseUtils.sqlEscapeString(desc)
//                , DatabaseUtils.sqlEscapeString(OsUtil.formatTime(System.currentTimeMillis())));
//        Log.d("zhm", "addCustomTableIFNotExist-> " + insert);
//        db.execSQL(insert);

        ContentValues contentValues = new ContentValues();
        contentValues.put(ChooseTable.COLUMN_NAME, tableName);
        contentValues.put(ChooseTable.COLUMN_DESCRIPTION, desc);
        contentValues.put(ChooseTable.COLUMN_TIME, OsUtil.formatTime(System.currentTimeMillis()));
        db.insertWithOnConflict(ChooseTable.TABLE_NAME, null, contentValues, db.CONFLICT_IGNORE);
    }

    public static void insertOrUpData(String tableName, String desc) {
        SQLiteDatabase db = AppSelfDatabaseFactory.getDatabase();
//        String insert = String.format(
//                "INSERT or REPLACE INTO ChooseTable (name,desc,time) VALUES (%s,%s,%s)"
//                , DatabaseUtils.sqlEscapeString(tableName)
//                , DatabaseUtils.sqlEscapeString(desc)
//                , DatabaseUtils.sqlEscapeString(OsUtil.formatTime(System.currentTimeMillis())));
//        db.execSQL(insert);

        ContentValues contentValues = new ContentValues();
        contentValues.put(ChooseTable.COLUMN_NAME, tableName);
        contentValues.put(ChooseTable.COLUMN_DESCRIPTION, desc);
        contentValues.put(ChooseTable.COLUMN_TIME, OsUtil.formatTime(System.currentTimeMillis()));
        db.insertWithOnConflict(ChooseTable.TABLE_NAME, null, contentValues, db.CONFLICT_REPLACE);
    }

    public static void upData(String oldName, String tableName, String desc) {
        SQLiteDatabase db = AppSelfDatabaseFactory.getDatabase();
        db.beginTransaction();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ChooseTable.COLUMN_NAME, tableName);
        contentValues.put(ChooseTable.COLUMN_DESCRIPTION, desc);
        contentValues.put(ChooseTable.COLUMN_TIME, OsUtil.formatTime(System.currentTimeMillis()));
        db.update(ChooseTable.TABLE_NAME, contentValues, ChooseTable.COLUMN_NAME + "=?", new String[]{oldName});

        ContentValues contentValue2 = new ContentValues();
        contentValue2.put(DetailTable.COLUMN_BELONG, tableName);
        db.update(DetailTable.TABLE_NAME, contentValue2, DetailTable.COLUMN_BELONG + "=?", new String[]{oldName});

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void delete(Context context, String tableName) {
        SQLiteDatabase db = AppSelfDatabaseFactory.getDatabase();
        db.beginTransaction();
        tableName = DatabaseUtils.sqlEscapeString(tableName);
        String delete1 = String.format(
                "DELETE FROM ChooseTable WHERE name = %s"
                , tableName);
        db.execSQL(delete1);

        String delete2 = String.format(
                "DELETE FROM DetailTable WHERE belong = %s"
                , tableName);
        db.execSQL(delete2);
        db.setTransactionSuccessful();
        String defaultTable = getDefaultNewWordTableName(context);
        if (tableName.equals(defaultTable)) {
            setDefaultNewWordTableName(context, DEFAULT_NEW_WORD);
        }
        db.endTransaction();
    }

    public static CustomTableInfo queryTable(String tableName) {
        SQLiteDatabase db = AppSelfDatabaseFactory.getDatabase();
        CustomTableInfo info = null;
        List<CustomTableInfo> result = new ArrayList<>();
        Cursor c = db.query(ChooseTable.TABLE_NAME, new String[]{ChooseTable.COLUMN_NAME,
                        ChooseTable.COLUMN_DESCRIPTION, ChooseTable.COLUMN_TIME}, ChooseTable.COLUMN_NAME + " = " +
                        DatabaseUtils.sqlEscapeString(tableName)
                , null, null, null, null);
        if (c != null) {
            if (c.moveToNext()) {
                info = new CustomTableInfo();
                info.name = c.getString(0);
                info.description = c.getString(1);
                info.time = c.getString(2);
                result.add(info);
            }
            c.close();
        }

        return info;
    }

    public static List<CustomTableInfo> getAllTableInfo() {
        SQLiteDatabase db = AppSelfDatabaseFactory.getDatabase();

        List<CustomTableInfo> result = new ArrayList<>();
        Cursor c = db.query(ChooseTable.TABLE_NAME, new String[]{ChooseTable.COLUMN_NAME,
                ChooseTable.COLUMN_DESCRIPTION, ChooseTable.COLUMN_TIME}, "", null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                CustomTableInfo info = new CustomTableInfo();
                info.name = c.getString(0);
                info.description = c.getString(1);
                info.time = c.getString(2);
                result.add(info);
            }
            c.close();
        }

        return result;
    }

    public static void showAddTableDialog(final Activity activity, final IAddTableListener listener) {
        final EditText editTextName = new EditText(activity);
        new AlertDialog.Builder(activity).setTitle(R.string.create_new_table)

                .setView(
                editTextName).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextName.getText().toString();
                if (!TextUtils.isEmpty(name)) {
                    ChooseTableUtil.addCustomTableIFNotExist(name, "");
                    listener.onSuccess(name);
                } else {
                    Toast.makeText(activity, R.string.not_empty, Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton(R.string.cancel, null).show();
    }

    public static String getDefaultNewWordTableName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ChooseTableSharePref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(DEFAULT_NEW_WORD_TABLE, DEFAULT_NEW_WORD);
    }

    public static void setDefaultNewWordTableName(Context context, String name) {
        SharedPreferences.Editor edit = context.getSharedPreferences(ChooseTableSharePref, Context.MODE_PRIVATE).edit();
        edit.putString(DEFAULT_NEW_WORD_TABLE, name);
        edit.apply();
    }

    public interface IAddTableListener {
        void onSuccess(String name);
    }
}
