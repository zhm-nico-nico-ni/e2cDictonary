package com.zhm.dictionary.friendly.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.zhm.dictionary.R;
import com.zhm.dictionary.friendly.Util.OsUtil;
import com.zhm.dictionary.friendly.data.English2Chinese;
import com.zhm.dictionary.friendly.data.QueryTask;
import com.zhm.dictionary.friendly.dbquery.HistoryQueryTableUtil;
import com.zhm.dictionary.friendly.detail.DetailViewPageActivity;
import com.zhm.dictionary.friendly.widget.AbsTopBar;
import com.zhm.dictionary.friendly.widget.MyTv;
import com.zhm.dictionary.friendly.widget.dialog.MyEditText;

public class QueryFragment extends CompatBaseFragment implements View.OnClickListener {
    public static final int CLEAR_AFTER_RETURN = 1;

    private MyEditText mAutoCompleteTextView;
    private ImageButton mIbCancel;
    private QueryTask mQueryTask;
    private ListView mLvData;

    private ViewAdapter mAdapter, mHistoryAdapter;
    private ListView mlvHistory;
    Runnable mQueryRunner = new Runnable() {
        @Override
        public void run() {
            if (!TextUtils.isEmpty(mAutoCompleteTextView.getText().toString())) {
                if (mQueryTask != null && !mQueryTask.isCancelled()) {
                    mQueryTask.cancel(true);
                }
                mQueryTask = new QueryTask(getActivity().getApplicationContext(), 20) {
                    @Override
                    protected void onPostExecute(List<English2Chinese> list) {
                        mAdapter.setData(list, this.param.length(), OsUtil.isFirstLetterAlpha(mAutoCompleteTextView.getText().toString()));
                    }
                };
                mQueryTask.execute(mAutoCompleteTextView.getText().toString());
                mLvData.setVisibility(View.VISIBLE);
                mlvHistory.setVisibility(View.GONE);
            } else {
                mAdapter.setData(null, 0, true);
                mLvData.setVisibility(View.GONE);
                mlvHistory.setVisibility(View.VISIBLE);
                return;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.layout_query, null);
        AbsTopBar topBar = (AbsTopBar) view.findViewById(R.id.topbar);
        topBar.setLeftLayoutVisible(View.GONE);
        topBar.setTitle(getString(R.string.tabname_dictionary));
        mAutoCompleteTextView = (MyEditText) view.findViewById(R.id.et_searchtext);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        mIbCancel = (ImageButton) view.findViewById(R.id.ibtn_deltext);
        mIbCancel.setFocusable(true);
        mIbCancel.setFocusableInTouchMode(true);
        mLvData = (ListView) view.findViewById(R.id.lv_datalist);
        mAdapter = new ViewAdapter(getActivity());
        mLvData.setAdapter(mAdapter);
        mLvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetailViewPageActivity.class);
                intent.putParcelableArrayListExtra("English2Chinese", mAdapter.getmData());
                intent.putExtra("pos", position);
                startActivityForResult(intent, CLEAR_AFTER_RETURN);
                English2Chinese data = (English2Chinese) mAdapter.getItem(position);
                mHistoryAdapter.addFirst(data);
                HistoryQueryTableUtil.insertOrUpData(data.word, data.description);
                mIbCancel.requestFocus();
            }
        });

        mlvHistory = (ListView) view.findViewById(R.id.lv_historylist);
        mHistoryAdapter = new ViewAdapter(getActivity());
        mlvHistory.setAdapter(mHistoryAdapter);
        mlvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetailViewPageActivity.class);
                intent.putParcelableArrayListExtra("English2Chinese", mHistoryAdapter.getmData());
                intent.putExtra("pos", position);
                startActivity(intent);
                mIbCancel.requestFocus();
            }
        });

        mIbCancel.setOnClickListener(this);

        mAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUIHandler.removeCallbacks(mQueryRunner);
                mUIHandler.postDelayed(mQueryRunner, 300);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showInput();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isVisible())
            showInput();
    }

    private void showInput() {
        mUIHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAutoCompleteTextView.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mAutoCompleteTextView, 0);
            }
        }, 500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
            case R.id.ibtn_deltext:
                mAutoCompleteTextView.setText("");
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CLEAR_AFTER_RETURN) {
            mAutoCompleteTextView.setText("");
            showInput();
        }
    }

    private class ViewAdapter extends BaseAdapter {
        private final Context mContext;
        private ArrayList<English2Chinese> mData = new ArrayList<English2Chinese>();
        private int mHighLight;
        private boolean mFirstLetterAlpha = true;

        public ViewAdapter(Context ctx) {
            mContext = ctx;
        }

        public void addFirst(English2Chinese data) {
            if (data != null) {
                ArrayList<English2Chinese> newList = new ArrayList<English2Chinese>();
                newList.add(data);
                newList.addAll(mData);
                mData = newList;
                notifyDataSetChanged();
            }
        }

        public void setData(List<English2Chinese> data, int highLight, boolean firstLetterAlpha) {
            mData.clear();
            if (data != null)
                mData.addAll(data);
            mHighLight = highLight;
            mFirstLetterAlpha = firstLetterAlpha;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder itemView ;

            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.word_list_item1, null);
                itemView = new ViewHolder();
                itemView.tvTitle = (MyTv) convertView.findViewById(R.id.itemView);
                itemView.tvDescription = (MyTv) convertView.findViewById(R.id.tv_description);
                convertView.setTag(itemView);
            } else {
                itemView = (ViewHolder) convertView.getTag();
            }
            English2Chinese item = ((English2Chinese) getItem(position));
            SpannableString spannableString = new SpannableString(item.word);
            if (mHighLight > 0)
                spannableString.setSpan(new ForegroundColorSpan(0xFFff2525), 0, mHighLight, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            itemView.tvTitle.setText(spannableString);
            if(!mFirstLetterAlpha){
                itemView.tvDescription.setText(item.description);
                itemView.tvDescription.setVisibility(View.VISIBLE);
            } else {
                itemView.tvDescription.setVisibility(View.GONE);
            }

            return convertView;
        }

        public ArrayList<English2Chinese> getmData() {
            return mData;
        }
    }

    private class ViewHolder{
        public MyTv tvTitle;
        public MyTv tvDescription;
    }
}
