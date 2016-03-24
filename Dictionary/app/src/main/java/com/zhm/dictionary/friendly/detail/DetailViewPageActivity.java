package com.zhm.dictionary.friendly.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.zhm.dictionary.R;
import com.zhm.dictionary.friendly.data.English2Chinese;
import com.zhm.dictionary.friendly.fragment.DetailFragment;

public class DetailViewPageActivity extends AppCompatActivity implements View.OnClickListener {

    private int mPos;
    private ArrayList<English2Chinese> mArrayEng2Chinese;
    private TextView mTvPrev, mTvNext;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_pager);

        mArrayEng2Chinese = getIntent().getParcelableArrayListExtra("English2Chinese");
        mPos = getIntent().getIntExtra("pos", 0);
        mTvPrev = (TextView) findViewById(R.id.tv_prev);
        mTvNext = (TextView) findViewById(R.id.tv_next);
        mTvPrev.setOnClickListener(this);
        mTvNext.setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpage);

        viewPager.setAdapter(new WordFragmentAdapter(getSupportFragmentManager(), mArrayEng2Chinese));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPos = position;
                int prev = position - 1;
                if (prev >= 0) {
                    mTvPrev.setText(mArrayEng2Chinese.get(prev).word);
                    startAnimation(mTvPrev);
                } else {
                    mTvPrev.clearAnimation();
                    mTvPrev.setVisibility(View.GONE);
                }

                int next = position + 1;
                if (next < mArrayEng2Chinese.size()) {
                    mTvNext.setText(mArrayEng2Chinese.get(next).word);
                    startAnimation(mTvNext);
                } else {
                    mTvNext.clearAnimation();
                    mTvNext.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setCurrentItem(mPos);
    }

    private void startAnimation(final View view) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.view_fade_in_and_out);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(anim);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_prev:
                viewPager.setCurrentItem(mPos - 1);
                break;
            case R.id.tv_next:
                viewPager.setCurrentItem(mPos + 1);
                break;
        }
    }

    private class WordFragmentAdapter extends FragmentStatePagerAdapter {
        List<Fragment> fragmentList;

        public WordFragmentAdapter(FragmentManager fm, List<English2Chinese> list) {
            super(fm);
            ArrayList<Fragment> res = new ArrayList<Fragment>();
            for (English2Chinese data : list) {
                res.add(DetailFragment.newInstance(data));
            }

            fragmentList = res;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

}
