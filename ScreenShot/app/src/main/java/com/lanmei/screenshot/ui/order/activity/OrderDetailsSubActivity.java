package com.lanmei.screenshot.ui.order.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lanmei.screenshot.R;
import com.lanmei.screenshot.adapter.GiveUpReasonAdapter;
import com.lanmei.screenshot.api.ScreenShotApi;
import com.lanmei.screenshot.bean.GiveUpReasonListBean;
import com.lanmei.screenshot.bean.OrderDetailsBean;
import com.lanmei.screenshot.bean.OrderListBean;
import com.lanmei.screenshot.event.GiveUpReasonEvent;
import com.lanmei.screenshot.event.OrderOperationEvent;
import com.lanmei.screenshot.helper.BGASortableNinePhotoHelper;
import com.lanmei.screenshot.utils.AKDialog;
import com.lanmei.screenshot.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.FormatTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

/**
 * 订单详情(本月第一单)
 */
public class OrderDetailsSubActivity extends BaseActivity {


    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.search_val_tv)
    TextView searchValTv;//关键词
    @InjectView(R.id.search_order_tv)
    TextView searchOrderTv;//排序方式
    @InjectView(R.id.low_price_tv)
    FormatTextView lowPriceTv;//最低价格
    @InjectView(R.id.high_price_tv)
    FormatTextView highPriceTv;//最高价格
    @InjectView(R.id.area_tv)
    TextView areaTv;//商家地址
    @InjectView(R.id.search_term_tv)
    TextView searchTermTv;//折扣服务
    @InjectView(R.id.upload_img_photoLayout)
    BGASortableNinePhotoLayout upload_img_photoLayout;//上传宝贝截图(多张用","隔开)
    @InjectView(R.id.pay_img_photoLayout)
    BGASortableNinePhotoLayout pay_img_photoLayout;//上传支付截图(多张用","隔开)

    BGASortableNinePhotoHelper mPayPhotoHelper;//支付截图
    BGASortableNinePhotoHelper mUploadPhotoHelper;//宝贝截图
    @InjectView(R.id.goods_command1_et)
    EditText goodsCommand1Et;
    @InjectView(R.id.goods_command2_et)
    EditText goodsCommand2Et;
    @InjectView(R.id.goods_command3_et)
    EditText goodsCommand3Et;
    @InjectView(R.id.check_command_et)
    EditText checkCommandEt;
    @InjectView(R.id.store_name_et)
    EditText storeNameEt;
    @InjectView(R.id.zhao_cha1_et)
    EditText zhaoCha1Et;
    @InjectView(R.id.zhao_cha2_et)
    EditText zhaoCha2Et;
    @InjectView(R.id.zhao_cha3_et)
    EditText zhaoCha3Et;
    @InjectView(R.id.same_store_command_et)
    EditText sameStoreCommandEt;
    private String id;

    OrderDetailsBean.DetailsBean bean;
    OrderListBean.OrderBean orderBean;

    private int photoType = 0;//0宝贝截图1支付截图

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (!StringUtils.isEmpty(bundle)) {
            orderBean = (OrderListBean.OrderBean) bundle.getSerializable("bean");
            if (!StringUtils.isEmpty(orderBean)) {
                id = orderBean.getId();
            }
            reasonList = (List<GiveUpReasonListBean.ReasonBean>) bundle.getSerializable("list");
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_details_sub;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.order_details);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        if (StringUtils.isEmpty(id)) {
            return;
        }
        initPhotoHelper();
        ScreenShotApi api = new ScreenShotApi("app/order_data");
        api.addParams("uid", api.getUserId(this));
        api.addParams("oid", id);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<OrderDetailsBean<String>>() {
            @Override
            public void onResponse(OrderDetailsBean<String> response) {
                if (isFinishing()) {
                    return;
                }
                bean = response.getData();
                setOrderDetails();
            }
        });

    }

    private void setOrderDetails() {
        if (StringUtils.isEmpty(bean)) {
            return;
        }

        searchValTv.setText(bean.getSearch_val());
        searchOrderTv.setText(bean.getSearch_order());
        areaTv.setText(bean.getArea());
        searchTermTv.setText("");
        searchTermTv.setText(bean.getSearch_term());
        lowPriceTv.setTextValue(bean.getLow_price());
        highPriceTv.setTextValue(bean.getHigh_price());

//        claimTv.setText(bean.getClaim());
        mUploadPhotoHelper.setData((ArrayList<String>) bean.getBady().getBadyimg());
    }

    private void initPhotoHelper() {
        mUploadPhotoHelper = new BGASortableNinePhotoHelper(this, upload_img_photoLayout);//0宝贝截图
        mUploadPhotoHelper.setDelegate(new BGASortableNinePhotoLayout.Delegate() {
            @Override
            public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
                photoType = 0;
                if (mUploadPhotoHelper != null) {
                    mUploadPhotoHelper.onClickAddNinePhotoItem(sortableNinePhotoLayout, view, position, models);
                }
            }

            @Override
            public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
                if (mUploadPhotoHelper != null) {
                    mUploadPhotoHelper.onClickDeleteNinePhotoItem(sortableNinePhotoLayout, view, position, model, models);
                }
            }

            @Override
            public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
                if (mUploadPhotoHelper != null) {
                    mUploadPhotoHelper.onClickNinePhotoItem(sortableNinePhotoLayout, view, position, model, models);
                }
            }
        });

        mPayPhotoHelper = new BGASortableNinePhotoHelper(this, pay_img_photoLayout);//1支付截图
        mPayPhotoHelper.setDelegate(new BGASortableNinePhotoLayout.Delegate() {
            @Override
            public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
                photoType = 1;
                if (mPayPhotoHelper != null) {
                    mPayPhotoHelper.onClickAddNinePhotoItem(sortableNinePhotoLayout, view, position, models);
                }
            }

            @Override
            public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
                if (mPayPhotoHelper != null) {
                    mPayPhotoHelper.onClickDeleteNinePhotoItem(sortableNinePhotoLayout, view, position, model, models);
                }
            }

            @Override
            public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
                if (mPayPhotoHelper != null) {
                    mPayPhotoHelper.onClickNinePhotoItem(sortableNinePhotoLayout, view, position, model, models);
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (photoType == 0) {
            if (mUploadPhotoHelper != null) {
                mUploadPhotoHelper.onActivityResult(requestCode, resultCode, data);
            }
        } else if (photoType == 1) {
            if (mPayPhotoHelper != null) {
                mPayPhotoHelper.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    @OnClick({R.id.copy_tv, R.id.get_answer1_tv, R.id.get_answer2_tv, R.id.get_answer3_tv, R.id.give_up_order_tv, R.id.next_step_tv})
    public void onViewClicked(View view) {
        if (bean == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.copy_tv://复制关键字
                if (StringUtils.isEmpty(bean.getSearch_val())) {
                    UIHelper.ToastMessage(this, R.string.not_copy_empty);
                    return;
                }
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", bean.getSearch_val());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                UIHelper.ToastMessage(this, R.string.copy_succeed);
                break;
            case R.id.get_answer1_tv://找茬答案1
                zhaoCha1Et.setText("1");
                break;
            case R.id.get_answer2_tv://找茬答案2
                zhaoCha2Et.setText("2");
                break;
            case R.id.get_answer3_tv://找茬答案3
                zhaoCha3Et.setText("3");
                break;
            case R.id.give_up_order_tv://放弃订单
                loadGiveUpReason();
                break;
            case R.id.next_step_tv://下一步
                UIHelper.ToastMessage(this, R.string.developing);
                break;
        }
    }


    List<GiveUpReasonListBean.ReasonBean> reasonList;

    //放弃订单原因弹框
    public void loadGiveUpReason() {
        if (!StringUtils.isEmpty(reasonList)) {
            getGiveUpListDialog();
            return;
        }
        ScreenShotApi api = new ScreenShotApi("app/reason");
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<GiveUpReasonListBean<String>>() {
            @Override
            public void onResponse(GiveUpReasonListBean<String> response) {
                if (isFinishing()) {
                    return;
                }
                reasonList = response.getData();
                EventBus.getDefault().post(new GiveUpReasonEvent(reasonList));
                getGiveUpListDialog();
            }
        });
    }

    private void getGiveUpListDialog() {
        AKDialog.getGiveUpListDialog(getContext(), reasonList, new GiveUpReasonAdapter.GiveUpReasonListener() {
            @Override
            public void giveUpReason(GiveUpReasonListBean.ReasonBean bean) {
//                        loadGiveUpOrder(bean.getTitle());
                UIHelper.ToastMessage(getContext(), R.string.developing);
            }
        });
    }

    //放弃订单
    private void loadGiveUpOrder(String title) {
        ScreenShotApi api = new ScreenShotApi("app/forgo_order");
        api.addParams("uid", api.getUserId(this));
        api.addParams("oid", id);
        api.addParams("reason", title);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(), CommonUtils.getString(response));
                EventBus.getDefault().post(new OrderOperationEvent());
                CommonUtils.loadUserInfo(getApplicationContext(), null);//
                finish();
            }
        });
    }

}
