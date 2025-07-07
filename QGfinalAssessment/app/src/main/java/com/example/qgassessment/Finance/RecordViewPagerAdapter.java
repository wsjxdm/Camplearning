package com.example.qgassessment.Finance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import java.util.List;

public class RecordViewPagerAdapter extends FragmentPagerAdapter {      //对应点击write按钮后跳转的添加记录的activity
    List<Fragment> fragmentList;
    String[] titles = {"支出", "收入"};
    public RecordViewPagerAdapter(@NonNull FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        return titles[position];
    }
}
