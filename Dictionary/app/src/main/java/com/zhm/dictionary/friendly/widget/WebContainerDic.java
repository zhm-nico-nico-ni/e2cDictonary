package com.zhm.dictionary.friendly.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class WebContainerDic extends WebView {
    public WebContainerDic(Context context) {
        super(context);
    }


    public WebContainerDic(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructs a new WebView with layout parameters and a default style.
     *
     * @param context      a Context object used to access application assets
     * @param attrs        an AttributeSet passed to our parent
     * @param defStyleAttr an attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     */
    public WebContainerDic(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
