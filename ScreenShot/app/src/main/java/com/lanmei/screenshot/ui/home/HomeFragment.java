package com.lanmei.screenshot.ui.home;

import android.os.Bundle;

import com.lanmei.screenshot.R;
import com.lanmei.screenshot.adapter.HomeAdapter;
import com.lanmei.screenshot.api.ScreenShotApi;
import com.lanmei.screenshot.bean.AdListBean;
import com.lanmei.screenshot.bean.HomeListBean;
import com.lanmei.screenshot.event.SetUserEvent;
import com.xson.common.app.BaseFragment;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;


/**
 * Created by xkai on 2018/7/13.
 * 首页
 */

public class HomeFragment extends BaseFragment {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    HomeAdapter mAdapter;
    private SwipeRefreshController<HomeListBean> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        toolbar.setTitle(getString(R.string.home));

        smartSwipeRefreshLayout.initWithLinearLayout();
        ScreenShotApi api = new ScreenShotApi("app/news_type");
        mAdapter = new HomeAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<HomeListBean>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();

        loadAd();//加载首页的轮播图

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void loadAd() {
        ScreenShotApi api = new ScreenShotApi("app/adpic");
        api.addParams("classid",1);
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<AdListBean<String>>() {
            @Override
            public void onResponse(AdListBean<String> response) {
                if (toolbar == null){
                    return;
                }
                mAdapter.setAdList(response.getData());
            }
        });
    }

    //获取用户信息时候调用
    @Subscribe
    public void setUserEvent(SetUserEvent event){
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
