package com.zhm.dictionary.friendly;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zhm.dictionary.R;
import com.zhm.dictionary.friendly.db.AppC2EDatabaseFactory;
import com.zhm.dictionary.friendly.db.AppDatabaseFactory;
import com.zhm.dictionary.friendly.db.AppSelfDatabaseFactory;
import com.zhm.dictionary.friendly.fragment.CompatBaseFragment;
import com.zhm.dictionary.friendly.fragment.OtherFragment;
import com.zhm.dictionary.friendly.fragment.QueryFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String TAB_QUERY = "TAB_QUERY";
    public final static String TAB_OTHER = "TAB_OTHER";
    private static String mCurrentFragmentTag;
    protected Handler mUIHandler = new Handler(Looper.getMainLooper());
    boolean canKill = false;
    private ImageButton mBtnQuery;
    private ImageButton mBtnOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDatabaseFactory.Init(this);
        AppC2EDatabaseFactory.Init(this);
        AppSelfDatabaseFactory.Init(this);
        Log.d("Zhm", "MainActivity onCreate db init");
        setContentView(R.layout.activity_main);

        mBtnQuery = (ImageButton) findViewById(R.id.tab_query);
        mBtnOther = (ImageButton) findViewById(R.id.tab_other);

        mBtnQuery.setOnClickListener(this);
        mBtnOther.setOnClickListener(this);

        switchContent(TAB_QUERY);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_query:
                switchContent(TAB_QUERY);
                break;

            case R.id.tab_other:
                switchContent(TAB_OTHER);
                break;
        }
    }

    public void switchContent(String tag) {

        switchTabChoosed(tag);

        CompatBaseFragment toFragment = (CompatBaseFragment) getSupportFragmentManager()
                .findFragmentByTag(tag);


        if (toFragment == null) {
            toFragment = FragmentFactory.getFragmentByTag(tag);
            if (toFragment == null) {
                throw new NullPointerException(
                        "you should create a new Fragment by Tag" + tag);
            }
        }

        CompatBaseFragment hideFragment;
        if (tag.equals(TAB_OTHER)) {
            hideFragment = (CompatBaseFragment) getSupportFragmentManager()
                    .findFragmentByTag(TAB_QUERY);
        } else {
            hideFragment = (CompatBaseFragment) getSupportFragmentManager()
                    .findFragmentByTag(TAB_OTHER);
        }

        FragmentTransaction fmt = getSupportFragmentManager()
                .beginTransaction();
        if (!toFragment.isAdded()) {
            fmt.add(R.id.layout_content_container, toFragment, tag);
        }
        if (hideFragment != null) {
            fmt.hide(hideFragment);
        }
        fmt.show(toFragment);
        fmt.commit();
        mCurrentFragmentTag = tag;
    }

    public void switchTabChoosed(String tag) {
        if (tag.equals(TAB_QUERY)) {
            mBtnQuery.setSelected(true);
            mBtnOther.setSelected(false);
        } else {
            mBtnQuery.setSelected(false);
            mBtnOther.setSelected(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!canKill) {
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), " do again to finish", Toast.LENGTH_LONG).show();
                        canKill = true;
                        mUIHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                canKill = false;
                            }
                        }, 3500);
                    }
                });
            } else
                Process.killProcess(Process.myPid());
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    public static class FragmentFactory {
        public static CompatBaseFragment getFragmentByTag(String tag) {
            if (tag.equals(TAB_QUERY)) {
                return new QueryFragment();
            } else if (tag.equals(TAB_OTHER)) {
                return new OtherFragment();
            } else {
                return null;
            }
        }
    }
}


