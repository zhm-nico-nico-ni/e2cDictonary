package com.zhm.dictionary.friendly.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhm.dictionary.R;
import com.zhm.dictionary.friendly.Util.OsUtil;

public class AbsTopBar extends RelativeLayout implements View.OnClickListener {

    protected Context mContext;
    private LinearLayout mChildLayout;
    private RelativeLayout mLayoutTopBarParent;
    private Button mTopbarBackBtn;
    private LinearLayout mLeftLayout;
    private TextView mTitle;
    private RelativeLayout mRightLayout;

    public AbsTopBar(Context context) {
        this(context, null);
        mContext = context;
    }

    public AbsTopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public AbsTopBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

        initSubView();
    }

    protected void initSubView() {
        removeAllViews();
        LayoutInflater inflate = LayoutInflater.from(getContext());
        View view = inflate.inflate(R.layout.topbar_abs, this, true);
        if (view != null) {
            mChildLayout = (LinearLayout) view.findViewById(R.id.layout_child);
            mLayoutTopBarParent = (RelativeLayout) view.findViewById(R.id.topbar_abs_main_layout);
        }
        inflateChild();
    }

    public void inflateChild() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.topbar_right_default, null);
        mChildLayout.addView(view);
        mLeftLayout = (LinearLayout) findViewById(R.id.layout_left);
        mLeftLayout.setOnClickListener(this);
        mTopbarBackBtn = (Button) view.findViewById(R.id.left_btn);
        mTitle = (TextView) view.findViewById(R.id.center_txt);
        mRightLayout = (RelativeLayout) view.findViewById(R.id.layout_child_right);
    }

    public void setRightChild(View view) {
        RelativeLayout.LayoutParams params;

        params = new RelativeLayout.LayoutParams((int) OsUtil.dipToPixels(50), LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        view.setLayoutParams(params);
        mRightLayout.addView(view);
        mRightLayout.setVisibility(View.VISIBLE);
    }

    public void setLeftLayoutVisible(int visible) {
        mLeftLayout.setVisibility(visible);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_left:
                if (mContext instanceof Activity) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    View view = ((Activity) mContext).getCurrentFocus();
                    if (view != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    ((Activity) mContext).finish();
                }
                break;
            default:
                break;
        }
    }
}
