package com.zhaohe.zhundao.ui.home.action;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhaohe.zhundao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:活动管理碎片
 * @Author:邹苏隆
 * @Since:2016/11/29 10:18
 */
public class ActionFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ViewPager mPaper;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();

    private TextView tv_acton, tv_actoff;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_act, container, false);

        initView();

        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }
        };
        mPaper.setAdapter(mAdapter);
        mPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int currentIndex;

            @Override
            public void onPageSelected(int position) {
                resetColor();
                switch (position) {
                    case 0:
                        tv_acton.setTextColor(Color.rgb(87, 153, 8));
                        break;
                    case 1:
                        tv_actoff.setTextColor(Color.rgb(87, 153, 8));
                        break;

                    default:
                        tv_acton.setTextColor(Color.rgb(87, 153, 8));
                        break;
                }
                currentIndex = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        return view;
    }

    /**
     * 初始化控件
     */
    public void initView() {
        tv_acton = (TextView) view.findViewById(R.id.tv_acton);
        tv_actoff = (TextView) view.findViewById(R.id.tv_actoff);
        mPaper = (ViewPager) view.findViewById(R.id.view_pager);
        tv_acton.setOnClickListener(this);
        tv_actoff.setOnClickListener(this);

        ActionOnFragment f1 = new ActionOnFragment();
        ActionOffFrgment f2 = new ActionOffFrgment();


        mFragments.add(f1);
        mFragments.add(f2);

    }

    public void resetColor() {
        tv_acton.setTextColor(Color.rgb(56, 56, 56));
        tv_actoff.setTextColor(Color.rgb(56, 56, 56));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_acton:
                resetColor();
                tv_acton.setTextColor(Color.rgb(87, 153, 8));
                mPaper.setCurrentItem(0);
                break;
            case R.id.tv_actoff:
                resetColor();
                tv_actoff.setTextColor(Color.rgb(87, 153, 8));
                mPaper.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    /**
     * ViewPager适配器
     */
    public class MyPagerAdapter extends PagerAdapter {
        public List<Activity> mListViews;

        public MyPagerAdapter(List<Activity> mListViews) {
            this.mListViews = mListViews;
        }


        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }
}
