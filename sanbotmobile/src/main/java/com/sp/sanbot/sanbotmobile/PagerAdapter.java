package com.sp.sanbot.sanbotmobile;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vootl on 20/12/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment>mFragmentList = new ArrayList<>();
    List<String>mFragmentTitleList = new ArrayList<>();

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title){
        mFragmentTitleList.add(title);
        mFragmentList.add(fragment);
    }

    public CharSequence getPageTitle(int position){
        return mFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return  mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
