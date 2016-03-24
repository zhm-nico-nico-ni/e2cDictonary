package com.zhm.dictionary.friendly.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhm.dictionary.R;

public class CommonMenuDialog extends Dialog implements View.OnClickListener {

    private int mItemIndex = 0;
    private IMenuDialogListener mListener;

    private ViewGroup mContentView;
    private TextView mTitleTv;
    private TextView mSubTitleTv;
    private TextView mCancelTv;


    public CommonMenuDialog(Context context) {
        super(context, R.style.AlertDialog);

        View contentView = View.inflate(getContext(), R.layout.layout_common_menu_dialog, null);
        mContentView = (ViewGroup) contentView.findViewById(R.id.dialog_container);
//        mTitleTv = (TextView) contentView.findViewById(R.id.tv_title_alert);
//        mSubTitleTv = (TextView) contentView.findViewById(R.id.tv_sub_title_alert);
        mCancelTv = (TextView) contentView.findViewById(R.id.tv_cancel);
        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener != null) {
                    mListener.onCancel();
                }
            }
        });

        setContentView(contentView);

//		Window window = getWindow();
//		WindowManager.LayoutParams params = window.getAttributes();
//		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//		params.width = OsUtil.dpToPx( 343 );
//		params.gravity = Gravity.RIGHT;
//		params.width = 400;
//		window.setAttributes(params);
    }

    public void setMenuDialogListener(IMenuDialogListener l) {
        mListener = l;
    }


    public CommonMenuDialog setTitle(String text) {
//		if(mCancelTv.getVisibility() == View.VISIBLE || mContentView.getChildCount() > 1){
//			((View)mContentView.findViewById(R.id.v_divider)).setVisibility(View.VISIBLE);
//		}
        mTitleTv.setVisibility(View.VISIBLE);
        mTitleTv.setText(text);
        return this;
    }


    public CommonMenuDialog setSubTitle(String text) {
        mSubTitleTv.setVisibility(View.VISIBLE);
        mSubTitleTv.setText(text);
        return this;
    }

    public CommonMenuDialog addItem(String text) {
        LinearLayout item = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_common_menu_item, mContentView, false);
        TextView tv = (TextView) item.findViewById(R.id.menu_content);
        tv.setTag(mItemIndex++);
        tv.setOnClickListener(this);
        tv.setText(text);
        mContentView.addView(item, mContentView.getChildCount() - 1);
        return this;
    }

    public CommonMenuDialog addItem(String text, int textColor) {
        LinearLayout item = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_common_menu_item, mContentView, false);
        TextView tv = (TextView) item.findViewById(R.id.menu_content);
        tv.setTag(mItemIndex++);
        tv.setOnClickListener(this);
        tv.setText(text);
        tv.setTextColor(textColor);
        mContentView.addView(item, mContentView.getChildCount() - 1);
        return this;
    }

    /*public CommonMenuDialog addItemWithDrable(String text) {
        LinearLayout item = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_common_menu_item_with_icon, mContentView, false);
        TextView tv = (TextView)item.findViewById(R.id.menu_content);
        tv.setTag(mItemIndex);
        tv.setOnClickListener(this);
        Drawable ic = getContext().getResources().getDrawable(R.drawable.phone_icon);
        ic.setBounds(0, 0, ic.getMinimumWidth(), ic.getMinimumHeight());
        tv.setCompoundDrawables(ic, null, null, null);
        tv.setText(text);
        item.setTag(mItemIndex);
        item.setOnClickListener(this);
        mItemIndex++;
        mContentView.addView(item, mContentView.getChildCount() - 1);
        return this;
    }*/

    public void setItemVisibility(int index, int visibility) {
        // 前面有2个默认的子view
        mContentView.getChildAt(index + 2).setVisibility(visibility);
    }

    public CommonMenuDialog addItem(int resId) {
        return addItem(getContext().getString(resId));
    }

    public CommonMenuDialog addItem(int resId, int textColor) {
        return addItem(getContext().getString(resId), textColor);
    }

    /*public CommonMenuDialog addItemWithDrable(int resId){
        return addItemWithDrable(getContext().getString(resId));
    }*/

    public void addCancelItem(String text) {
        if (mTitleTv.getVisibility() == View.VISIBLE) {
            mContentView.findViewById(R.id.v_divider).setVisibility(View.VISIBLE);
        }
        mCancelTv.setVisibility(View.VISIBLE);
        mCancelTv.setText(text);
    }

    public void addCancelItem(int resId) {
        mContentView.findViewById(R.id.v_divider).setVisibility(View.VISIBLE);
        mCancelTv.setVisibility(View.VISIBLE);
        mCancelTv.setText(resId);
    }


    @Override
    public void onClick(View v) {
        dismiss();
        Integer index = (Integer) v.getTag();
        if (index != null && mListener != null) {
            mListener.onItemClicked(index.intValue());
        }
    }

    @Override
    public void show() {
        //NOTE(lijianfeng) 当activity已经结束，Dialog.show()会抛出WindowManager.BadTokenException异常
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static interface IMenuDialogListener {
        void onItemClicked(int index); //index begin with 0

        void onCancel();
    }
}
