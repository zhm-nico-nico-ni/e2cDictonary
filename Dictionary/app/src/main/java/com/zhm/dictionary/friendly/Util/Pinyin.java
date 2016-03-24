package com.zhm.dictionary.friendly.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zhm.dictionary.R;

public class Pinyin {

    public static final String SEARCH_PRE = "-";
    private static final String LOG_TAG = "Pinyin";
    private static final int CLEAR_DELAY_MILLIS = 15 * 1000;
    private static final int WORD_PINYIN_LENGTH = 5;
    private static final Pattern CHINESEPATTERN = Pattern.compile("[\\u4E00-\\u9FA5]");
    /**
     * Caching converted result
     */
    private static LruCache<String, String> mPinyinCache = new LruCache<String, String>(1000);
    private static LruCache<String, String[]> mPinyinsCache = new LruCache<String, String[]>(1000);
    private static LruCache<String, String> mPinyinWithNameCache = new LruCache<String, String>(1000);
    @SuppressLint("UseSparseArrays")
    private static HashMap<Integer, String> sSpecialMap = new HashMap<Integer, String>();
    //复姓
    private static HashMap<Long, String[]> sCompoundSpecialMap = new HashMap<Long, String[]>();
    private static Properties sPinYinTable;
    private static Runnable sClearTask = new Runnable() {
        @Override
        public void run() {
            unloadTable();
        }
    };

    static {
        sSpecialMap.put((int) '曾', "ZENG");
        sSpecialMap.put((int) '重', "CHONG");
        sSpecialMap.put((int) '区', "OU");
        sSpecialMap.put((int) '仇', "QIU");
        sSpecialMap.put((int) '秘', "BI");
        sSpecialMap.put((int) '冼', "XIAN");
        sSpecialMap.put((int) '解', "XIE");
        sSpecialMap.put((int) '折', "SHE");
        sSpecialMap.put((int) '单', "SHAN");
        sSpecialMap.put((int) '朴', "PIAO");
        sSpecialMap.put((int) '翟', "ZHAI");
        sSpecialMap.put((int) '查', "ZHA");
        sSpecialMap.put((int) '这', "ZHE");
        sSpecialMap.put((int) '覃', "QIN");

        sSpecialMap.put((int) '贲', "BEN");
        sSpecialMap.put((int) '参', "CAN");
        sSpecialMap.put((int) '种', "CHONG");
        sSpecialMap.put((int) '褚', "CHU");
        sSpecialMap.put((int) '弗', "FEI");
        sSpecialMap.put((int) '干', "GAN");
        sSpecialMap.put((int) '盖', "GE");
        sSpecialMap.put((int) '句', "GOU");
        sSpecialMap.put((int) '藉', "JI");
        sSpecialMap.put((int) '纪', "JI");
        sSpecialMap.put((int) '颉', "JIE");
        sSpecialMap.put((int) '璩', "JU");
        sSpecialMap.put((int) '阚', "KAN");
        sSpecialMap.put((int) '适', "KUO");
        sSpecialMap.put((int) '缪', "MIAO");
        sSpecialMap.put((int) '能', "NAI");
        sSpecialMap.put((int) '乜', "NIE");
        sSpecialMap.put((int) '蕃', "PI");
        sSpecialMap.put((int) '繁', "PO");
        sSpecialMap.put((int) '任', "REN");
        sSpecialMap.put((int) '芮', "RUI");
        sSpecialMap.put((int) '谌', "SHEN");
        sSpecialMap.put((int) '宿', "SU");
        sSpecialMap.put((int) '眭', "SUI");
        sSpecialMap.put((int) '佟', "TONG");
        sSpecialMap.put((int) '厘', "XI");
        sSpecialMap.put((int) '旋', "XUAN");
        sSpecialMap.put((int) '燕', "YAN");
        sSpecialMap.put((int) '应', "YING");
        sSpecialMap.put((int) '乐', "YUE");
        sSpecialMap.put((int) '员', "YUN");
        sSpecialMap.put((int) '祭', "ZHAI");
        sSpecialMap.put((int) '徵', "ZHENG");

        sCompoundSpecialMap.put(((long) '万' << 32) | (long) '俟', new String[]{"MO", "QI"});
        sCompoundSpecialMap.put(((long) '尉' << 32) | (long) '迟', new String[]{"YU", "CHI"});
    }

    private static InputStream getPinyinInputStream(Context ctx) {
        File filesDir = ctx.getFilesDir();
        if (filesDir == null) {
            return null;
        }

        InputStream is;

        try {
            is = new FileInputStream(new File(filesDir.getAbsolutePath() + File.separator + "pinyin.txt"));
        } catch (FileNotFoundException fnfe) {
            return null;
        }

        return is;
    }

    private static synchronized boolean ensureTableLoaded(Context ctx) {
        if (sPinYinTable == null) {
            Log.i(LOG_TAG, "Load pinyin table");
            InputStream is;
            if (ctx == null) {
                return false;
            }
            Resources resources = ctx.getResources();
            if (resources != null) {
                try {
                    is = resources.openRawResource(R.raw.unicode_to_hanyu_pinyin);
                    if (is == null) {
                        is = getPinyinInputStream(ctx);
                        if (is == null) {
                            return false;
                        }
                    }
                } catch (Resources.NotFoundException nfe) {
                    is = getPinyinInputStream(ctx);
                    if (is == null) {
                        return false;
                    }
                }
            } else {
                is = getPinyinInputStream(ctx);
                if (is == null) {
                    return false;
                }
            }
            sPinYinTable = new Properties();
            try {
                sPinYinTable.load(is);
                is.close();
            } catch (IOException ioe) {
                sPinYinTable = null;
                Log.e(LOG_TAG, ioe.getMessage());
                return false;
            }

            Log.i(LOG_TAG, "Load piniyin table done");
        }
        return true;
    }

    private static synchronized void unloadTable() {
        if (sPinYinTable != null) {
            Log.i(LOG_TAG, "Unload piniyin table");
            sPinYinTable.clear();
        }
        sPinYinTable = null;
    }

    @SuppressLint("DefaultLocale")
    private static String getSinglePinYin(Context ctx, char ch) {
        String chKey = String.valueOf(ch);
        String foundRecord = mPinyinCache.get(chKey);
        if (foundRecord != null) {
            return foundRecord;
        }

        if (!ensureTableLoaded(ctx) || sPinYinTable == null) {
            return "";
        }

        int codePointOfChar = ch;
        String codepointHexStr = Integer.toHexString(codePointOfChar).toUpperCase();

        foundRecord = sPinYinTable.getProperty(codepointHexStr);
        if (foundRecord != null) { // 处理多音字
            int index = foundRecord.indexOf(',');
            if (index != -1) {
                foundRecord = foundRecord.substring(0, index);
            }
            if (chKey != null && foundRecord != null) {
                mPinyinCache.put(chKey, foundRecord);
            }
        }

        return foundRecord;
    }

    @SuppressLint("DefaultLocale")
    public static String getPinYin(Context ctx, String src) {
        if ((src == null) || (src.length() <= 0)) {
            return null;
        }
        Daemon.handler().removeCallbacks(sClearTask);

        char[] t1 = src.toCharArray();
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < t1.length; i++) {
            // 判断是否为汉字字符
            if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                if (i == 0) {
                    if (t1.length > 1 && Character.toString(t1[i + 1]).matches("[\\u4E00-\\u9FA5]+")) {
                        long key = ((long) t1[i] << 32) | (long) t1[i + 1];
                        String[] compSpecial = sCompoundSpecialMap.get(key);
                        if (compSpecial != null && compSpecial.length >= 2) {
                            ret.append(compSpecial[0]);
                            ret.append(compSpecial[1]);
                            i++;
                            continue;
                        }
                    }
                    String special = sSpecialMap.get((int) t1[i]);
                    if (special != null) {
                        ret.append(special);
                        continue;
                    }
                }
                String t2 = getSinglePinYin(ctx, t1[i]);
                if (t2 != null) {
                    ret.append(t2);
                } else {
                    ret.append(t1[i]);
                }
            } else {
                ret.append(t1[i]);
            }
        }

        Daemon.handler().postDelayed(sClearTask, CLEAR_DELAY_MILLIS);
        return ret.toString().toUpperCase();
    }

    @SuppressLint("DefaultLocale")
    public static String[] getPinYinArray(Context ctx, String src) {
        if ((src == null) || (src.length() <= 0)) {
            return null;
        }
        if (mPinyinsCache.get(src) != null) {
            return mPinyinsCache.get(src);
        }
        Daemon.handler().removeCallbacks(sClearTask);

        char[] chars = src.toCharArray();
        String[] pinyins = new String[src.length()];
        for (int i = 0; i < chars.length; i++) {
            if (Character.toString(chars[i]).matches("[\\u4E00-\\u9FA5]+")) {
                if (i == 0) {
                    if (chars.length > 1 && Character.toString(chars[i + 1]).matches("[\\u4E00-\\u9FA5]+")) {
                        long key = ((long) chars[i] << 32) | (long) chars[i + 1];
                        String[] compSpecial = sCompoundSpecialMap.get(key);
                        if (compSpecial != null && compSpecial.length >= 2) {
                            pinyins[i] = compSpecial[0];
                            pinyins[i + 1] = compSpecial[1];
                            i++;
                            continue;
                        }
                    }
                    String special = sSpecialMap.get((int) chars[i]);
                    if (special != null) {
                        pinyins[i] = special;
                        continue;
                    }
                }

                pinyins[i] = getSinglePinYin(ctx, chars[i]);
                if (pinyins[i] != null) {
                    pinyins[i] = pinyins[i].toUpperCase();
                } else {
                    pinyins[i] = Character.toString(chars[i]).toUpperCase();
                }
            } else {
                pinyins[i] = Character.toString(chars[i]).toUpperCase();
            }
        }

        Daemon.handler().postDelayed(sClearTask, CLEAR_DELAY_MILLIS);
        mPinyinsCache.put(src, pinyins);
        return pinyins;
    }

    @SuppressLint("DefaultLocale")
    public static String joinPinyinCaseSensitive(String[] pinyins) {
        if (pinyins != null) {
            StringBuilder sb = new StringBuilder();
            for (String py : pinyins) {
                if (TextUtils.isEmpty(py)) continue;
                sb.append(py.charAt(0));
                sb.append(py.substring(1).toLowerCase());
            }
            return sb.toString();
        }
        return null;
    }

    public static String joinPinyin(String[] pinyins) {
        if (pinyins != null) {
            StringBuilder sb = new StringBuilder();
            for (String py : pinyins) {
                sb.append(py);
            }
            return sb.toString();
        }
        return null;
    }

    public static String joinPinyinHeader(String[] pinyins) {
        if (pinyins != null) {
            StringBuilder sb = new StringBuilder();
            for (String py : pinyins) {
                if (TextUtils.isEmpty(py)) continue;
                sb.append(py.charAt(0));
            }
            return sb.toString();
        }
        return null;
    }

    public static String joinPinyinWithSpace(String[] pinyins) {
        if (pinyins != null) {
            StringBuilder sb = new StringBuilder();
            for (String py : pinyins) {
                sb.append(py).append(' ');
            }
            if (pinyins.length > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();
        }
        return null;
    }

    public static String joinPinyinWithName(String[] pinyins, String name) {
        if (pinyins != null && name != null) {
            if (mPinyinWithNameCache.get(name) != null) {
                return mPinyinWithNameCache.get(name);
            }
            if (pinyins.length != name.length()) {
                Log.w(LOG_TAG, "the length of pinyins is no the same with the name");
                return null;
            } else {
                StringBuilder sb = new StringBuilder();
                int i = 0;
                for (String py : pinyins) {
                    int need = WORD_PINYIN_LENGTH - py.length();
                    sb.append(py);
                    for (int j = 0; j < need; j++) {
                        sb.append('0');
                    }
                    sb.append(name.charAt(i));
                    i++;
                }
                mPinyinWithNameCache.put(name, sb.toString());
                return sb.toString();
            }
        }
        return null;
    }

    //fix BUG #2723 ［灰度崩溃］java.lang.OutOfMemoryError
    public static String getSearchPinyin(Context context, String name) {
        if (name == null || name.trim().length() <= 0) return "";
        String pinyins[] = Pinyin.getPinYinArray(context, name);
        try {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < pinyins.length && builder.length() < 500; i++) {
                builder.append(SEARCH_PRE);
                builder.append(firstCharToUpperCase(pinyins[i]));
                for (int j = i + 1; j < pinyins.length; j++) {
                    builder.append(firstCharToUpperCase(pinyins[j]));
                }
            }
            return builder.toString();
        } catch (OutOfMemoryError error) {
            return joinPinyinCaseSensitive(pinyins);
        }
    }

    public static String firstCharToUpperCase(String pinyin) {
        if (pinyin == null || pinyin.length() <= 0) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(pinyin.substring(0, 1).toUpperCase());
        sb.append(pinyin.substring(1).toLowerCase());
        return sb.toString();
    }

    public static boolean containChinese(String s) {
        if (s == null || s.length() <= 0) return false;
        Matcher matcher = CHINESEPATTERN.matcher(s);
        return matcher.find();
    }

    public static int[] matchPinyinRange(Context context, String name, String filterStr) {
        filterStr = filterStr.toUpperCase();
        int[] matchRange = new int[2];
        boolean hasMatchFirst = false;
        String[] pinyins = getPinYinArray(context, name);
        for (int i = 0; pinyins != null && i < pinyins.length; i++) {
            String pinyin = pinyins[i];
            if (filterStr.length() < pinyin.length()) {
                if (pinyin.startsWith(filterStr)) {
                    if (!hasMatchFirst) {
                        matchRange[0] = i;
                        matchRange[1] = i + 1;
                        break;
                    } else {
                        matchRange[1] = i + 1;
                        break;
                    }
                } else {
                    continue;
                }
            } else if (filterStr.length() == pinyin.length()) {
                if (pinyin.equals(filterStr)) {
                    if (!hasMatchFirst) {
                        matchRange[0] = i;
                        matchRange[1] = i + 1;
                        break;
                    } else {
                        matchRange[1] = i + 1;
                        break;
                    }
                } else {
                    continue;
                }
            } else {
                if (filterStr.startsWith(pinyin)) {
                    if (!hasMatchFirst) {
                        matchRange[0] = i;
                        hasMatchFirst = true;
                        filterStr = filterStr.substring(pinyin.length());
                    } else {
                        filterStr = filterStr.substring(pinyin.length());
                    }
                } else {
                    continue;
                }
            }
        }

        return matchRange;
    }
}
