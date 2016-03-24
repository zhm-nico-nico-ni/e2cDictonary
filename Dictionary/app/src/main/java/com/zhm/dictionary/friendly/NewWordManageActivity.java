package com.zhm.dictionary.friendly;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.zhm.dictionary.R;
import com.zhm.dictionary.friendly.data.CustomTableInfo;
import com.zhm.dictionary.friendly.data.DetailTableInfo;
import com.zhm.dictionary.friendly.db.DetailTable;
import com.zhm.dictionary.friendly.dbquery.ChooseTableUtil;
import com.zhm.dictionary.friendly.dbquery.DetailTableUtil;
import com.zhm.dictionary.friendly.detail.ListInfoActivity;
import com.zhm.dictionary.friendly.widget.AbsTopBar;
import com.zhm.dictionary.friendly.widget.dialog.CommonMenuDialog;

public class NewWordManageActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView mLvData;
    private NewWordTableAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_info);
        AbsTopBar topbar = (AbsTopBar) findViewById(R.id.topbar);
        topbar.setTitle(getString(R.string.new_word));
        ImageButton rightView = (ImageButton) View.inflate(this, R.layout.button_add_item, null);
        topbar.setRightChild(rightView);
        rightView.setImageResource(R.drawable.btn_add);
        rightView.setOnClickListener(this);

        mLvData = (ListView) findViewById(R.id.lv_datalist);
        mAdapter = new NewWordTableAdapter(this);
        mLvData.setAdapter(mAdapter);

        mLvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final CustomTableInfo info = (CustomTableInfo) mAdapter.getItem(position);
                ArrayList<DetailTableInfo> list = DetailTableUtil.getByCustomTable(info.name, null, DetailTable.COLUMN_ADD_TIME+ " desc");
                Intent it = new Intent(getBaseContext(), ListInfoActivity.class);
                it.putExtra("title", info.name);
                it.putExtra("canDelete", true);
                it.putParcelableArrayListExtra("English2Chinese", list);
                startActivity(it);
            }
        });
        mLvData.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                showItemDialog(position);
                return true;
            }
        });

        queryData();
    }

    private void queryData() {
        List<CustomTableInfo> list = ChooseTableUtil.getAllTableInfo();
        mAdapter.setDates(list);
    }

    @Override
    public void onClick(View v) {
        ChooseTableUtil.showAddTableDialog(this, new ChooseTableUtil.IAddTableListener() {
            @Override
            public void onSuccess(final String name) {
                queryData();
                AlertDialog.Builder builder = new AlertDialog.Builder(NewWordManageActivity.this);
                builder.setMessage(getString(R.string.set_default_new_word_table_tips));
                builder.setTitle(R.string.tips);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChooseTableUtil.setDefaultNewWordTableName(getBaseContext(), name);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.create().show();
            }
        });
    }

    private void showItemDialog(final int position) {
        CommonMenuDialog dialog = new CommonMenuDialog(this);
        dialog.addItem(R.string.set_default_new_word_table);
        dialog.addItem(R.string.rename);
        dialog.addItem(R.string.set_description);
        dialog.addItem(R.string.delete);
        dialog.setMenuDialogListener(new CommonMenuDialog.IMenuDialogListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onItemClicked(int index) {
                final CustomTableInfo info = (CustomTableInfo) mAdapter.getItem(position);
                switch (index) {
                    case 0:
                        ChooseTableUtil.setDefaultNewWordTableName(getBaseContext(), info.name);
                        break;
                    case 1: {
                        final EditText editTextName = new EditText(NewWordManageActivity.this);
                        editTextName.setText(info.name);
                        new AlertDialog.Builder(NewWordManageActivity.this)
                                .setTitle(R.string.rename)
//                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setView(editTextName)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String name = editTextName.getText().toString();
                                        if (!TextUtils.isEmpty(name) && !name.equals(info.name)) {
                                            ChooseTableUtil.upData(info.name, name, info.description);
                                            queryData();
                                        }
                                    }
                                }).setNegativeButton(R.string.cancel, null).show();
                    }
                    break;
                    case 2: {
                        final EditText editTextName = new EditText(NewWordManageActivity.this);
                        editTextName.setText(info.description);
                        new AlertDialog.Builder(NewWordManageActivity.this)
                                .setTitle(R.string.set_description)
//                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setView(editTextName)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String desc = editTextName.getText().toString();
                                        if (!TextUtils.isEmpty(desc)) {
                                            ChooseTableUtil.insertOrUpData(info.name, desc);
                                            queryData();
                                        }
                                    }
                                }).setNegativeButton(R.string.cancel, null).show();

                    }
                    break;
                    case 3: {
                        final CustomTableInfo customTableInfo = (CustomTableInfo) mAdapter.getItem(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewWordManageActivity.this);
                        builder.setMessage(getString(R.string.delete_table_tips, customTableInfo.name));
                        builder.setTitle(R.string.tips);
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ChooseTableUtil.delete(getBaseContext(), customTableInfo.name);
                                dialog.dismiss();
                                mAdapter.delete(position);
                            }
                        });

                        builder.setNegativeButton(R.string.cancel, null);
                        builder.create().show();
                    }
                    break;
                }
            }
        });
        dialog.show();
    }

    private class NewWordTableAdapter extends BaseAdapter {
        List<CustomTableInfo> list = new ArrayList<>();
        private Context context;

        public NewWordTableAdapter(Context ctx) {
            context = ctx;
        }

        public void setDates(List<CustomTableInfo> l) {
            list.clear();
            list.addAll(l);
            notifyDataSetChanged();
        }

        public void delete(int pos) {
            list.remove(pos);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.word_list_item2, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            CustomTableInfo info = (CustomTableInfo) getItem(position);
            holder.tvTableName.setText(info.name);
            holder.tvDescription.setText(info.description);
            holder.tvAddTime.setText(info.time);

            return convertView;
        }
    }

    private class ViewHolder {

        TextView tvTableName, tvAddTime, tvDescription;

        public ViewHolder(View root) {
            tvTableName = (TextView) root.findViewById(R.id.tv_table_name);
            tvAddTime = (TextView) root.findViewById(R.id.tv_addtime);
            tvDescription = (TextView) root.findViewById(R.id.tv_description);
        }
    }
}
