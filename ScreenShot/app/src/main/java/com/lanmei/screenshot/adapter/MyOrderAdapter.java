package com.lanmei.screenshot.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.screenshot.ui.order.fragment.OrderListFragment;


/**
 * 我的订单
 */
public class MyOrderAdapter extends FragmentPagerAdapter {


    public MyOrderAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle bundle = new Bundle();
        switch (position) {//0待支付|1待接单|2待处理|3出发中|4已到达|5服务中|6服务完成|7确认完成|8待评价|9 全部|10取消
            case 0:
                bundle.putString("status","");//全部
                break;
            case 1:
                bundle.putString("status","1");//执行中
                break;
            case 2:
                bundle.putString("status","2");//取消
                break;
            case 3:
                bundle.putString("status","3");//已完成
                break;

        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "全部";
            case 1:
                return "执行中";
            case 2:
                return "已取消";
            case 3:
                return "已完成";
        }
        return "";
    }


}
