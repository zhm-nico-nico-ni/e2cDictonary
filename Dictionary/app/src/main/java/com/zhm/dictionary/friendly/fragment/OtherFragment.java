package com.zhm.dictionary.friendly.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.zhm.dictionary.friendly.NewWordManageActivity;
import com.zhm.dictionary.R;
import com.zhm.dictionary.friendly.data.CustomTableInfo;
import com.zhm.dictionary.friendly.data.DetailTableInfo;
import com.zhm.dictionary.friendly.db.DetailTable;
import com.zhm.dictionary.friendly.db.HistoryQueryTable;
import com.zhm.dictionary.friendly.dbquery.ChooseTableUtil;
import com.zhm.dictionary.friendly.dbquery.DetailTableUtil;
import com.zhm.dictionary.friendly.dbquery.HistoryQueryTableUtil;
import com.zhm.dictionary.friendly.detail.HistoryInfoActivity;
import com.zhm.dictionary.friendly.detail.ListInfoActivity;
import com.zhm.dictionary.friendly.widget.AbsTopBar;

public class OtherFragment extends CompatBaseFragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.layout_other, null);
        AbsTopBar topBar = (AbsTopBar) view.findViewById(R.id.topbar);
        topBar.setLeftLayoutVisible(View.GONE);
        topBar.setTitle(getString(R.string.record));

        view.findViewById(R.id.tv_history).setOnClickListener(this);
        view.findViewById(R.id.tv_new_word).setOnClickListener(this);
        view.findViewById(R.id.tv_new_word_default).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_history: {
                ArrayList<DetailTableInfo> list = HistoryQueryTableUtil.getHistory(null, HistoryQueryTable.COLUMN_ADD_TIME +" desc");
                Intent it = new Intent(getContext(), HistoryInfoActivity.class);
                it.putExtra("title", getString(R.string.history));
                it.putParcelableArrayListExtra("English2Chinese", list);
                startActivity(it);
            }
            break;
            case R.id.tv_new_word: {
                Intent it2 = new Intent(getContext(), NewWordManageActivity.class);
                startActivity(it2);
            }
            break;
            case R.id.tv_new_word_default: {
                String defaultNewWordTable = ChooseTableUtil.getDefaultNewWordTableName(getContext());
                if (!TextUtils.isEmpty(defaultNewWordTable)) {
                    CustomTableInfo info = ChooseTableUtil.queryTable(defaultNewWordTable);
                    if (info == null) {
                        ChooseTableUtil.addCustomTableIFNotExist(defaultNewWordTable, "");
                    }

                    Intent it = new Intent(getContext(), ListInfoActivity.class);
                    it.putExtra("title", defaultNewWordTable);
                    it.putExtra("canDelete", true);
                    ArrayList<DetailTableInfo> list = DetailTableUtil.getByCustomTable(defaultNewWordTable
                            , null, DetailTable.COLUMN_ADD_TIME+ " desc");
                    it.putParcelableArrayListExtra("English2Chinese", list);
                    startActivity(it);
                }
            }
            break;
        }
    }
}
