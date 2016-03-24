package com.zhm.dictionary.friendly.widget.dialog;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MyEditText extends EditText {
    private TextWatcher mTextWatcher;
    public MyEditText(Context context) {
        super(context);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "font.ttf");
        setTypeface(typeFace);
        super.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(mTextWatcher!=null){
                    mTextWatcher.beforeTextChanged(s, start, count, after);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String editable = getText().toString();
                String str = stringFilter(editable);
                if (!editable.equals(str)) {
                    setText(str);
                }
                if(mTextWatcher!=null){
                    mTextWatcher.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mTextWatcher!=null) {
                    mTextWatcher.afterTextChanged(s);
                }
            }
        });
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        mTextWatcher = watcher;
    }

    public static String stringFilter(String str)throws PatternSyntaxException {

        String regEx = "[[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\\d\"\n]";

        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);

        return m.replaceAll("");

    }
}
