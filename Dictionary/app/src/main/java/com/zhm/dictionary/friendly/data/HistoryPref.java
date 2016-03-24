package com.zhm.dictionary.friendly.data;

public class HistoryPref {

    private static final String PREF = "HistoryPref";

    /*public static ArrayList<HistoryData> getHistory(Context ctx) {

        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        Map<String, ?> map = sp.getAll();
        ArrayList<HistoryData> historyDatas = new ArrayList<>();

        for(Map.Entry<String, ?> entry: map.entrySet()){
            historyDatas.add(new HistoryData((String) entry.getValue()));
        }

        return historyDatas;
    }*/

    /*public static void addHistory(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        HistoryData old = new HistoryData( sp.getString(key,"{}"));

        old.word = key;
        old.count++;
        old.time = System.currentTimeMillis();

        SharedPreferences.Editor edit = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit();
        edit.putString(key, old.toString());
        edit.apply();
    }*/
}
