package com.zhm.dictionary.friendly.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;

import com.zhm.dictionary.R;
import com.zhm.dictionary.friendly.dbquery.HistoryQueryTableUtil;

public class HistoryInfoActivity extends ListInfoActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button rightView = (Button) View.inflate(this, R.layout.right_top_button, null);
        rightView.setText(R.string.clear);
        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryQueryTableUtil.clear();
                mAdapter.clear();
                Snackbar.make(mLvData, R.string.clear_success, Snackbar.LENGTH_SHORT)
                        .setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                finish();
                            }
                        }).show();
            }
        });
        topbar.setRightChild(rightView);
    }
}
