package com.zhm.dictionary.friendly.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

public class BottomPopupDialog extends PopupDialogFragment {
    public static final String TAG = "BottomPopupDialog";

    private View.OnClickListener onClickListener;

    private Context context;
    private String confirmText;

    public void setConfirmText(String confirmText) {
        this.confirmText = confirmText;
    }

    @Override
    protected void setupDialog(Dialog dialog) {
//        context = getActivity();
//        dialog.setContentView(R.layout.layout_bottom_popup_dialog);
//
//        if(!TextUtils.isEmpty(confirmText)) {
//            ((Button)dialog.findViewById(R.id.btn_clear_confirm)).setText(confirmText);
//        }
//
//        dialog.findViewById(R.id.btn_clear_confirm).setOnClickListener(onClickListener);
//        dialog.findViewById(R.id.btn_clear_cancel).setOnClickListener(onClickListener);
    }

    public void setClickListener(View.OnClickListener l) {
        onClickListener = l;
    }
}