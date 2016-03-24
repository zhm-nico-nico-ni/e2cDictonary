package com.zhm.dictionary.friendly.Util;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OsUtil {

    static final SimpleDateFormat DATE_FORMAT_yyyyMMdd_HHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static float dipToPixels(float dipValue) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static boolean isFirstLetterAlpha(String pinyin) {
        if (pinyin != null && pinyin.length() > 0) {
            char ch = pinyin.charAt(0);
            return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
        }
        return false;
    }

    public static String formatTime(long time) {
        return DATE_FORMAT_yyyyMMdd_HHmmss.format(new Date(time));
    }

    public static void formatCount(int count, TextView tv) {
        if (count > 1) {
            tv.setVisibility(View.VISIBLE);
            if (count > 10) {
                tv.setTextColor(Color.RED);
            } else {
                tv.setTextColor(0xff333333);
            }
        } else {
            tv.setVisibility(View.GONE);
        }
    }
}
