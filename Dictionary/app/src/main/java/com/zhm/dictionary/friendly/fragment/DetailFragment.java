package com.zhm.dictionary.friendly.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

import com.zhm.dictionary.R;
import com.zhm.dictionary.friendly.data.English2Chinese;
import com.zhm.dictionary.friendly.data.QueryTask;
import com.zhm.dictionary.friendly.dbquery.ChooseTableUtil;
import com.zhm.dictionary.friendly.dbquery.DetailTableUtil;
import com.zhm.dictionary.friendly.widget.AbsTopBar;
import com.zhm.dictionary.friendly.widget.WebContainerDic;

public class DetailFragment extends CompatBaseFragment implements View.OnClickListener {

    WebContainerDic mWebContainerDic;
    private English2Chinese mData;

    public static DetailFragment newInstance(English2Chinese data) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mData = bundle.getParcelable("data");
        Log.d("haha", "description:" + mData.description);

        View root = View.inflate(getActivity(), R.layout.layout_detatil, null);
        AbsTopBar topBar = (AbsTopBar) root.findViewById(R.id.topbar);
        topBar.setTitle(mData.word);
        ImageButton rightView = (ImageButton) View.inflate(getActivity(), R.layout.button_add_item, null);
        topBar.setRightChild(rightView);
        rightView.setImageResource(R.drawable.ic_starred);
        rightView.setOnClickListener(this);


        mWebContainerDic = (WebContainerDic) root.findViewById(R.id.wv_detail);

        if (!TextUtils.isEmpty(mData.description)) {
            setWeb(mData.description);
        } else {
            QueryTask task = new QueryTask(getActivity().getApplicationContext(), 1) {
                @Override
                protected void onPostExecute(List<English2Chinese> list) {
                    if (list != null && list.size() > 0) {
                        mData = list.get(0);
                        setWeb(mData.description);
                    }
                }
            };
            task.execute(mData.word);
        }

        return root;
    }

    private void setWeb(String description) {
        int index = description.indexOf("<div class=\"break\"></div>");
        if(index>0)
            description = description.substring(0,index);

        StringBuilder html = new StringBuilder();

        html.append("<html>");
        html.append("<head>");

        html.append(getCSSForTable());
        html.append("</head>");
        html.append("<body>");
        html.append(description);
        html.append("</body></html>");

        mWebContainerDic.loadData(html.toString(), "text/html; charset=UTF-8", null);
    }

    private String getCSSForTable() {

        return "<style>" + "* {font-family: \"Lucida Grande\", Lucida, Verdana, sans-serif;" +
                "}" +
                "" +
                "body {" +
                "padding: 5px;" +
                "}" +
                "" +
                "h5 {" +
                "font-size: 14px;" +
                "margin: 2px;" +
                "}" +
                "" +
                "p {" +
                "font-size: 16px;" +
                "color: #353535;" +
                "margin: 0px;" +
                "}" +
                "" +
                "ol {" +
                "color: #002000;" +
                "margin-left: 3px;" +
                "font-size: 18px;" +
                "}" +
                "" +
                "h2 {" +
                "font-size: 28px;" +
                "color: #006000;     " +
                "font-weight:700;" +
                "    word-wrap:break-word;" +
                "    width:70%;" +
                "    display:block;" +
                "}" +
                "" +
                "h3 {" +
                "color: #888888;" +
                "margin-left: 3px;" +
                "font-size: 14px;" +
                "}" +
                "" +
                "b {" +
                "font-style: italic;" +
                "color: #008000;" +
                "}" +
                "" +
                ".break {" +
                "padding: 0px;" +
                "}" +
                "" +
                ".speaker_img {" +
                "margin-bottom: -5px;" +
                "margin-left: 5px;" +
                "}" +
                "</style>";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_right:
                String defTableName = ChooseTableUtil.getDefaultNewWordTableName(getContext());
                if (TextUtils.isEmpty(defTableName)) {
                    ChooseTableUtil.showAddTableDialog(getActivity(), new ChooseTableUtil.IAddTableListener() {
                        @Override
                        public void onSuccess(String name) {
                            ChooseTableUtil.setDefaultNewWordTableName(getActivity(), name);
                            DetailTableUtil.insertOrUpData(mData.word, name, mData.description);
                            Snackbar.make(mWebContainerDic, R.string.word_added, Snackbar.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    DetailTableUtil.insertOrUpData(mData.word, defTableName, mData.description);
                    Snackbar.make(mWebContainerDic, R.string.word_added, Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
