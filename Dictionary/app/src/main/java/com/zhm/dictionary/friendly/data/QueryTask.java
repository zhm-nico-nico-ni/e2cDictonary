package com.zhm.dictionary.friendly.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.zhm.dictionary.friendly.Util.OsUtil;
import com.zhm.dictionary.friendly.Util.Pinyin;
import com.zhm.dictionary.friendly.db.AppC2EDatabaseFactory;
import com.zhm.dictionary.friendly.db.AppDatabaseFactory;

public class QueryTask extends AsyncTask<String, Integer, List<English2Chinese>> {

    public String param;
    private Context mContext;
    private int limit = 20;

    public QueryTask(Context appContext, int limit) {
        mContext = appContext;
        this.limit = limit;
    }

    @Override
    protected List doInBackground(String... params) {

        try {
            param = params[0];
            char firstChar = param.charAt(0);
            if (OsUtil.isFirstLetterAlpha(String.valueOf(firstChar))) {
                SQLiteDatabase db = AppDatabaseFactory.getDatabase();

                Cursor c = db.query("dict_" + firstChar, new String[]{"word, description"},
                        "word like \'" + param + "%\' limit " + limit
                        , null, null, null, null);

                ArrayList<English2Chinese> result = new ArrayList<English2Chinese>();
                if (c != null) {
                    while (c.moveToNext()) {
                        English2Chinese data = new English2Chinese();
                        data.word = c.getString(0);
                        data.description = c.getString(1);
                        result.add(data);
                    }
                    c.close();
                }
                return result;
            } else {
                SQLiteDatabase db = AppC2EDatabaseFactory.getDatabase();
                String pinyin = Pinyin.getPinYin(mContext, param);
                char pinyinFirstChar = pinyin.charAt(0);

                Cursor c = db.query("cedict_" + pinyinFirstChar, new String[]{"simWord, description"},
                        "simWord like \'" + param + "%\' limit " + limit
                        , null, null, null, null);

                ArrayList<English2Chinese> result = new ArrayList<English2Chinese>();
                if (c != null) {
                    while (c.moveToNext()) {
                        English2Chinese data = new English2Chinese();
                        data.word = c.getString(0);
                        data.description = c.getString(1);
                        result.add(data);
                    }
                    c.close();
                }
                return result;
            }
        } catch (Exception ex){
            Log.e("QueryTask", " exception catch !", ex);
        }
        return null;
    }
}
