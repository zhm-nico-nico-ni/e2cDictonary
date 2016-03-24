package com.zhm.dictionary.friendly.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTv extends TextView {
    public MyTv(Context context) {
        this(context, null);
    }

    public MyTv(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTv(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "font.ttf");
        setTypeface(typeFace);
    }
}
