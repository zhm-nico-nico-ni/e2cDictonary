package com.zhm.dictionary.friendly.widget.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.zhm.dictionary.R;

public abstract class PopupDialogFragment extends DialogFragment {

    /**
     * No need to override, so set to final
     */
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.Dialog_Fullscreen);
        setupDialog(dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(params);
        window.setWindowAnimations(R.style.DialogAnimation);

        return dialog;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isAdded()) {
            super.show(manager, tag);
        }
    }

    /**
     * Call {@link Dialog#setContentView()} here
     */
    protected abstract void setupDialog(Dialog dialog);

}
