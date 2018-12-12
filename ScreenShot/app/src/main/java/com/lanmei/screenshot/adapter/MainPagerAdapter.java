package com.lanmei.screenshot.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.screenshot.ui.home.HomeFragment;
import com.lanmei.screenshot.ui.home.HomeSubFragment;
import com.lanmei.screenshot.ui.mine.mineFragment;
import com.lanmei.screenshot.ui.order.OrderFragment;


public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeSubFragment();//首页
            case 1:
                return new OrderFragment();//订单
            case 2:
                return new mineFragment();//我的
        }
        return new HomeFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }

}
