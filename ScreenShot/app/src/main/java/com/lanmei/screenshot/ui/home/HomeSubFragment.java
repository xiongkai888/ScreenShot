package com.lanmei.screenshot.ui.home;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.lanmei.screenshot.R;
import com.lanmei.screenshot.adapter.HomeAdAdapter;
import com.lanmei.screenshot.adapter.HomeSubAdapter;
import com.lanmei.screenshot.api.ScreenShotApi;
import com.lanmei.screenshot.bean.AdListBean;
import com.lanmei.screenshot.bean.HomeSubListBean;
import com.lanmei.screenshot.bean.ReceivingOrderBean;
import com.lanmei.screenshot.event.OrderOperationEvent;
import com.lanmei.screenshot.event.SetUserEvent;
import com.lanmei.screenshot.utils.AKDialog;
import com.lanmei.screenshot.utils.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.FormatTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by xkai on 2018/7/13.
 * 首页
 */

public class HomeSubFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    HomeSubAdapter mAdapter;

    @InjectView(R.id.banner)
    ConvenientBanner banner;

    @InjectView(R.id.principal_tv)
    TextView principalTv;
    @InjectView(R.id.balance_tv)
    TextView balanceTv;
    @InjectView(R.id.integral_tv)
    TextView integralTv;
    @InjectView(R.id.need_num_tv)
    FormatTextView needNumTv;
    @InjectView(R.id.grade_tv)
    TextView gradeTv;//等级

    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;//下拉刷新

    @InjectView(R.id.state_tv)
    TextView stateTv;

    private String state;//(0|1=>我有时间|执行中)
    private String perfect;//(0|1|2|3=>未上传|已上传|审核不通过|审核通过)

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home_sub;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        toolbar.setTitle(R.string.home);
        setHomeHead();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.color2cc6fc, R.color.color33cea6, R.color.red, R.color.color9c9da0, R.color.colorF1C107, R.color.black);

        mAdapter = new HomeSubAdapter(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setNestedScrollingEnabled(false);
        //添加Android自带的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        loadAd();//加载首页的轮播图
        loadNewsType();//加载首页的新闻分类

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void loadNewsType() {
        ScreenShotApi api = new ScreenShotApi("app/news_type");
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<HomeSubListBean<String>>() {
            @Override
            public void onResponse(HomeSubListBean<String> response) {
                if (toolbar == null) {
                    return;
                }
                swipeRefreshLayout.setRefreshing(false);
                List<HomeSubListBean.HomeSubBean> list = response.getData();
                if (StringUtils.isEmpty(list)) {
                    return;
                }
                mAdapter.setData(list);
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (toolbar == null) {
                    return;
                }
                swipeRefreshLayout.setRefreshing(false);
                UIHelper.ToastMessage(getContext(), error.getMessage());
            }
        });
    }

    private void setHomeHead() {
        UserBean userBean = CommonUtils.getUserBean(context);
        if (userBean == null) {
            return;
        }
        principalTv.setText(userBean.getPrincipal());
        balanceTv.setText(userBean.getBalance());
        integralTv.setText(userBean.getIntegral());
        gradeTv.setText(String.format(context.getString(R.string.grade), userBean.getGrade()));
        needNumTv.setTextValue(userBean.getNeednum() + "");

        state = userBean.getState();
        perfect = userBean.getPerfect();
        if (StringUtils.isSame(CommonUtils.isThree, perfect)){
            stateTv.setVisibility(View.VISIBLE);
            if (StringUtils.isSame(CommonUtils.isZero, state)) {
                stateTv.setText(R.string.l_have_time);
            } else {
                stateTv.setText(R.string.doing);
            }
        }else {
            stateTv.setVisibility(View.INVISIBLE);
        }

    }

    private void loadAd() {
        ScreenShotApi api = new ScreenShotApi("app/adpic");
        api.addParams("classid", 1);
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<AdListBean<String>>() {
            @Override
            public void onResponse(AdListBean<String> response) {
                if (toolbar == null) {
                    return;
                }
                List<AdListBean.AdBean> list = response.getData();
                if (StringUtils.isEmpty(list)) {
                    return;
                }
                banner.setPages(new CBViewHolderCreator() {
                    @Override
                    public Object createHolder() {
                        return new HomeAdAdapter();
                    }
                }, list);
                banner.setPageIndicator(new int[]{R.drawable.shape_item_index_white, R.drawable.shape_item_index_red});
                banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
                banner.startTurning(3000);
            }
        });
    }

    //获取用户信息时候调用
    @Subscribe
    public void setUserEvent(SetUserEvent event) {
        setHomeHead();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        CommonUtils.loadUserInfo(context.getApplicationContext(), null);
        loadNewsType();//加载首页的新闻分类
    }


    @OnClick({R.id.state_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.state_tv://
                if (StringUtils.isEmpty(state)) {
                    return;
                }
                setState();
                break;
        }
    }

    private void setState() {
        if (!StringUtils.isSame(CommonUtils.isZero, state)) {
            return;
        }
        ScreenShotApi api = new ScreenShotApi("app/dispatch");//派单
        api.addParams("uid", api.getUserId(context));
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<ReceivingOrderBean<String>>() {
            @Override
            public void onResponse(ReceivingOrderBean<String> response) {
                if (toolbar == null) {
                    return;
                }
                ReceivingOrderBean.ReceivingBean bean = response.getData();
                if (bean == null) {
                    return;
                }
                AKDialog.getOrderDialog(context, bean.getPrice(), bean.getOrder_num(), new AKDialog.ReceiveOrderListener() {
                    @Override
                    public void yes(String order_num) {
                        loadRejectOrder(order_num, false);//接单
                    }

                    @Override
                    public void no(String order_num) {
                        loadRejectOrder(order_num, true);//拒单
                    }
                });
            }
        });
    }

    /**
     * @param order_num
     * @param isReject  是不是拒绝接单
     */
    private void loadRejectOrder(String order_num, final boolean isReject) {
        ScreenShotApi api = new ScreenShotApi(isReject ? "app/refusal" : "app/receipt");
        api.addParams("uid", api.getUserId(context));
        api.addParams("order_num", order_num);
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (toolbar == null) {
                    return;
                }
                UIHelper.ToastMessage(context, CommonUtils.getString(response));
                CommonUtils.loadUserInfo(context.getApplicationContext(), null);
                if (!isReject) {//接单时通知订单列表刷新
                    EventBus.getDefault().post(new OrderOperationEvent());
                }

            }
        });
    }
}
