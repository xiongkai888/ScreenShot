package com.lanmei.screenshot.ui.order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.screenshot.R;
import com.lanmei.screenshot.api.ScreenShotApi;
import com.lanmei.screenshot.bean.OrderDetailsBean;
import com.lanmei.screenshot.bean.OrderListBean;
import com.lanmei.screenshot.helper.BGASortableNinePhotoHelper;
import com.xson.common.app.BaseActivity;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.FormatTextView;

import java.util.ArrayList;

import butterknife.InjectView;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

/**
 * 订单详情
 */
public class OrderDetailsActivity extends BaseActivity {


    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.search_val_tv)
    TextView searchValTv;//搜索关键词
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
    @InjectView(R.id.claim_tv)
    TextView claimTv;//商家要求
    private String id;

    @InjectView(R.id.task_num_tv)
    TextView taskNumTv;
    @InjectView(R.id.state_tv)
    TextView stateTv;
    @InjectView(R.id.pic_iv)
    ImageView picIv;
    @InjectView(R.id.goods_tv)
    TextView goodsTv;
    @InjectView(R.id.merchant_tv)
    TextView merchantTv;
    @InjectView(R.id.commission_tv)
    FormatTextView commissionTv;
    @InjectView(R.id.upload_img_photoLayout)
    BGASortableNinePhotoLayout upload_img_photoLayout;//上传宝贝截图(多张用","隔开)
    @InjectView(R.id.pay_img_photoLayout)
    BGASortableNinePhotoLayout pay_img_photoLayout;//上传支付截图(多张用","隔开)

    BGASortableNinePhotoHelper mPayPhotoHelper;//支付截图
    BGASortableNinePhotoHelper mUploadPhotoHelper;//宝贝截图

    int photoType = 0;//0宝贝截图1支付截图

    OrderDetailsBean.DetailsBean bean;
    OrderListBean.OrderBean orderBean;


    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (!StringUtils.isEmpty(bundle)) {
            orderBean = (OrderListBean.OrderBean) bundle.getSerializable("bean");
            if (!StringUtils.isEmpty(orderBean)) {
                id = orderBean.getId();
            }
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_details;
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

    private void setOrderDetails() {
        if (StringUtils.isEmpty(bean)) {
            return;
        }
        taskNumTv.setText(String.format(getString(R.string.task_num), orderBean.getOrder_num()));
        String state = orderBean.getStatus();
        if (!StringUtils.isEmpty(state)) {
            switch (state) {
                case "1":
                    stateTv.setText(R.string.doing);
                    break;
                case "2":
                    stateTv.setText(R.string.been_cancelled);
                    break;
                case "3":
                    stateTv.setText(R.string.have_finished);
                    break;
            }
        }
        goodsTv.setText(orderBean.getBadyname());
        merchantTv.setText(orderBean.getShopname());
        commissionTv.setFormatText(orderBean.getPrice());
        ImageHelper.load(this, orderBean.getBadyimg(), picIv, null, true, R.drawable.default_pic, R.drawable.default_pic);

        searchValTv.setText(bean.getSearch_val());
        searchOrderTv.setText(bean.getSearch_order());
        areaTv.setText(bean.getArea());
        searchTermTv.setText(bean.getSearch_term());
        lowPriceTv.setTextValue(bean.getLow_price());
        highPriceTv.setTextValue(bean.getHigh_price());

        claimTv.setText(bean.getClaim());

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


}
