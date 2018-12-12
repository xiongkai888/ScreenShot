package com.lanmei.screenshot;

import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.lanmei.screenshot.adapter.MainPagerAdapter;
import com.lanmei.screenshot.event.LogoutEvent;
import com.lanmei.screenshot.helper.TabHelper;
import com.lanmei.screenshot.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.widget.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;

public class MainActivity extends BaseActivity{

    @InjectView(R.id.viewPager)
    NoScrollViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        if (!CommonUtils.isLogin(this)){
            finish();
            return;
        }

        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setNoScroll(true);

        new TabHelper(this, mTabLayout);

        CommonUtils.loadUserInfo(getApplication(),null);
        EventBus.getDefault().register(this);

    }

    //退出登录时调用
    @Subscribe
    public void logoutEvent(LogoutEvent event){
        if (!CommonUtils.isLogin(this)){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
