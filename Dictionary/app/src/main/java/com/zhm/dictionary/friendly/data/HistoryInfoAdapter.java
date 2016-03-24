package com.zhm.dictionary.friendly.data;

import android.content.Context;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.zhm.dictionary.R;
import com.zhm.dictionary.friendly.Util.OsUtil;

public class HistoryInfoAdapter extends BaseAdapter {
    private final Context mContext;
    private ArrayList<DetailTableInfo> mData = new ArrayList<>();

    public HistoryInfoAdapter(Context ctx) {
        mContext = ctx;
    }

    public void addFirst(DetailTableInfo data) {
        if (data != null) {
            ArrayList<DetailTableInfo> newList = new ArrayList<DetailTableInfo>();
            newList.add(data);
            newList.addAll(mData);
            mData = newList;
            notifyDataSetChanged();
        }
    }

    public void delete(int pos) {
        mData.remove(pos);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void setData(List<DetailTableInfo> data) {
        mData.clear();
        if (data != null)
            mData.addAll(data);
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
        ViewHolder itemView = null;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.word_list_item, null);
            itemView = new ViewHolder(convertView);
            convertView.setTag(itemView);
        } else {
            itemView = (ViewHolder) convertView.getTag();
        }

        DetailTableInfo dinfo = (DetailTableInfo) getItem(position);
        SpannableString spannableString = new SpannableString(dinfo.theWord.word);
        itemView.tvWord.setText(spannableString);
        OsUtil.formatCount(dinfo.count, itemView.tvCount);

        itemView.tvCount.setText(String.valueOf(dinfo.count));
        itemView.tvAddTime.setText(dinfo.addTime);

        return convertView;
    }

    public ArrayList<English2Chinese> getmData() {
        ArrayList<English2Chinese> res = new ArrayList<>();
        for (DetailTableInfo data : mData) {
            res.add(data.theWord);
        }
        return res;
    }

    private class ViewHolder {
        TextView tvWord, tvCount, tvAddTime;

        public ViewHolder(View root) {
            tvWord = (TextView) root.findViewById(R.id.tv_word);
            tvCount = (TextView) root.findViewById(R.id.tv_count);
            tvAddTime = (TextView) root.findViewById(R.id.tv_addtime);
        }
    }
}
