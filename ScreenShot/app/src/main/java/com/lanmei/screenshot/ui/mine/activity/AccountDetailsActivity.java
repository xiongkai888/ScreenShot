package com.lanmei.screenshot.ui.mine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.lanmei.screenshot.R;
import com.lanmei.screenshot.adapter.AccountDetailsAdapter;
import com.lanmei.screenshot.api.ScreenShotApi;
import com.lanmei.screenshot.bean.AccountDetailsListBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;


/**
 * 账户明细
 */
public class AccountDetailsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    AccountDetailsAdapter mAdapter;
    private SwipeRefreshController<AccountDetailsListBean> controller;


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
        actionbar.setTitle(R.string.account_details);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

//        EventBus.getDefault().register(this);

        smartSwipeRefreshLayout.initWithLinearLayout();
        ScreenShotApi api = new ScreenShotApi("app/capital");
        api.addParams("uid", api.getUserId(this));
        mAdapter = new AccountDetailsAdapter(this);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<AccountDetailsListBean>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }

    //申请提现成功时调用
//    @Subscribe
//    public void depositEvent(DepositEvent event){
//        if (controller != null){
//            controller.loadFirstPage();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
}
