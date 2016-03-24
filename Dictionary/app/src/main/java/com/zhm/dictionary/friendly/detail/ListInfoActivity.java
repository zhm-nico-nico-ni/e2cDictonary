package com.zhm.dictionary.friendly.detail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import com.zhm.dictionary.R;
import com.zhm.dictionary.friendly.data.DetailTableInfo;
import com.zhm.dictionary.friendly.data.HistoryInfoAdapter;
import com.zhm.dictionary.friendly.dbquery.DetailTableUtil;
import com.zhm.dictionary.friendly.widget.AbsTopBar;

public class ListInfoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    protected ListView mLvData;
    protected HistoryInfoAdapter mAdapter;
    protected AbsTopBar topbar;
    private boolean canDelete = false;
    private String mTableName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_info);
        topbar = (AbsTopBar) findViewById(R.id.topbar);

        mLvData = (ListView) findViewById(R.id.lv_datalist);
        mAdapter = new HistoryInfoAdapter(getApplicationContext());
        mLvData.setAdapter(mAdapter);
        procData();

        mLvData.setOnItemClickListener(this);
        mLvData.setOnItemLongClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        procData();
    }

    private void procData() {
        ArrayList<DetailTableInfo> mArrayEng2Chinese = getIntent().getParcelableArrayListExtra("English2Chinese");
        mAdapter.setData(mArrayEng2Chinese);
        mTableName = getIntent().getStringExtra("title");
        canDelete = getIntent().getBooleanExtra("canDelete", false);
        topbar.setTitle(mTableName);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DetailViewPageActivity.class);
        intent.putParcelableArrayListExtra("English2Chinese", mAdapter.getmData());
        intent.putExtra("pos", position);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (canDelete) {
            final DetailTableInfo wordInfo = (DetailTableInfo) mAdapter.getItem(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.remove_starred));
            builder.setTitle(R.string.tips);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DetailTableUtil.deleteWord(wordInfo.theWord.word, mTableName);
                    dialog.dismiss();
                    mAdapter.delete(position);
                }
            });

            builder.setNegativeButton(R.string.cancel, null);
            builder.create().show();
        }
        return canDelete;
    }
}
