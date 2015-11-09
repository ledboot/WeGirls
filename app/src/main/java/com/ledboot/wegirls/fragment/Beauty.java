package com.ledboot.wegirls.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ledboot.wegirls.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/3 0003.
 */
public class Beauty extends BaseFragment {


    ViewPager viewPager;
    TabLayout tabLayout;

    String[] tabTitle;

    FragmentManager fm;
    GirlsFragmentPagerAdapter girlsFragmentPagerAdapter;

    public Beauty(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.beauty,container,false);
        initView(view);
        initData();
        setListener();
        return view;
    }

    private void initView(View view){
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
    }

    private void initData(){
        tabTitle = getResources().getStringArray(R.array.tab_title);
        fm = getActivity().getSupportFragmentManager();
        List<Fragment> list = new ArrayList<>();
        NormalGirlsPageFragment normalGirlsPageFragment = NormalGirlsPageFragment.newInstance();
        TaoModelPageFragment taoModelPageFragment = TaoModelPageFragment.newInstance();
        list.add(normalGirlsPageFragment);
        list.add(taoModelPageFragment);
        girlsFragmentPagerAdapter = new GirlsFragmentPagerAdapter(fm,list);
        viewPager.setAdapter(girlsFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setListener(){
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    class GirlsFragmentPagerAdapter extends FragmentPagerAdapter{
        private List<Fragment> list;

        public GirlsFragmentPagerAdapter(FragmentManager fm,List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return tabTitle.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle[position];
        }
    }
}
