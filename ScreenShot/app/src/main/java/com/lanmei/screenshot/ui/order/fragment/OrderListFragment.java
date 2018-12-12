package com.lanmei.screenshot.ui.order.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import com.lanmei.screenshot.R;
import com.lanmei.screenshot.adapter.GiveUpReasonAdapter;
import com.lanmei.screenshot.adapter.OrderListAdapter;
import com.lanmei.screenshot.api.ScreenShotApi;
import com.lanmei.screenshot.bean.GiveUpReasonListBean;
import com.lanmei.screenshot.bean.OrderListBean;
import com.lanmei.screenshot.event.GiveUpReasonEvent;
import com.lanmei.screenshot.event.OrderOperationEvent;
import com.lanmei.screenshot.utils.AKDialog;
import com.lanmei.screenshot.utils.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.InjectView;


/**
 * Created by Administrator on 2017/4/27.
 * 订单列表
 */

public class OrderListFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    OrderListAdapter mAdapter;
    private SwipeRefreshController<OrderListBean> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initSwipeRefreshLayout() {
        smartSwipeRefreshLayout.initWithLinearLayout();
        ScreenShotApi api = new ScreenShotApi("app/myorder");
        api.addParams("uid", api.getUserId(context));
        api.addParams("row", 20);
        api.addParams("status", getArguments().getString("status"));
        mAdapter = new OrderListAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<OrderListBean>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        mAdapter.setOrderOperationListener(new OrderListAdapter.OrderOperationListener() {
            @Override
            public void giveUpOrder(String oid) {
                loadGiveUpReason(oid);
            }

            @Override
            public void deleteOrder(String order_num) {
                deleteOrderNum(order_num);
            }
        });
        controller.loadFirstPage();
    }

    private void deleteOrderNum(final String order_num) {
        AKDialog.getAlertDialog(context, "确认删除该订单？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadDeleteOrder(order_num);
            }
        });
    }

    //删除订单
    private void loadDeleteOrder(String order_num) {
        ScreenShotApi api = new ScreenShotApi("app/delete_order");
        api.addParams("uid",api.getUserId(context));
        api.addParams("order_num",order_num);
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<GiveUpReasonListBean<String>>() {
            @Override
            public void onResponse(GiveUpReasonListBean<String> response) {
                if (mAdapter == null){
                    return;
                }
                UIHelper.ToastMessage(context,CommonUtils.getString(response));
                EventBus.getDefault().post(new OrderOperationEvent());
                CommonUtils.loadUserInfo(context.getApplicationContext(),null);//
            }
        });
    }

    List<GiveUpReasonListBean.ReasonBean> reasonList;

    //放弃订单原因弹框
    public void loadGiveUpReason(final String oid) {
        if (!StringUtils.isEmpty(reasonList)) {
            getGiveUpListDialog(oid);
            return;
        }
        ScreenShotApi api = new ScreenShotApi("app/reason");
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<GiveUpReasonListBean<String>>() {
            @Override
            public void onResponse(GiveUpReasonListBean<String> response) {
                if (mAdapter == null){
                    return;
                }
                getGiveUpListDialog(oid);
                EventBus.getDefault().post(new GiveUpReasonEvent(response.getData()));
            }
        });
    }

    private void getGiveUpListDialog(final String oid) {
        AKDialog.getGiveUpListDialog(getContext(), reasonList, new GiveUpReasonAdapter.GiveUpReasonListener() {
            @Override
            public void giveUpReason(GiveUpReasonListBean.ReasonBean bean) {
                        loadGiveUpOrder(oid,bean.getTitle());
//                UIHelper.ToastMessage(getContext(), R.string.developing);
            }
        });
    }

    //放弃订单
    private void loadGiveUpOrder(String oid, String title) {
        ScreenShotApi api = new ScreenShotApi("app/forgo_order");
        api.addParams("uid",api.getUserId(context));
        api.addParams("oid",oid);
        api.addParams("reason",title);
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (mAdapter == null){
                    return;
                }
                UIHelper.ToastMessage(context,CommonUtils.getString(response));
                EventBus.getDefault().post(new OrderOperationEvent());
                CommonUtils.loadUserInfo(context.getApplicationContext(),null);//
            }
        });
    }

    //获取放弃订单原因时调用
    @Subscribe
    public void giveUpReasonEvent(GiveUpReasonEvent event) {
        reasonList = event.getReasonList();
        mAdapter.setReasonList(reasonList);
    }
    //操作订单状态的时候调用
    @Subscribe
    public void orderOperationEvent(OrderOperationEvent event) {
        if (controller != null) {
            controller.loadFirstPage();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
