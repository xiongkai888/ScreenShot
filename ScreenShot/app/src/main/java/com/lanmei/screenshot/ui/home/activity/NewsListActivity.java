package com.lanmei.screenshot.ui.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.lanmei.screenshot.R;
import com.lanmei.screenshot.adapter.NewsListAdapter;
import com.lanmei.screenshot.api.ScreenShotApi;
import com.lanmei.screenshot.bean.NewsListBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;


/**
 * 新闻列表
 */
public class NewsListActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    NewsListAdapter mAdapter;
    private SwipeRefreshController<NewsListBean> controller;


    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        actionbar.setTitle(bundle.getString("title"));

        smartSwipeRefreshLayout.initWithLinearLayout();
        ScreenShotApi api = new ScreenShotApi("app/news_list");
        api.addParams("tid", bundle.getString("tid"));
        api.addParams("row", 10);
        mAdapter = new NewsListAdapter(this);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NewsListBean>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }
}
