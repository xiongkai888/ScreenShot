package com.lanmei.screenshot.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.screenshot.R;
import com.lanmei.screenshot.bean.GiveUpReasonListBean;
import com.lanmei.screenshot.bean.OrderListBean;
import com.lanmei.screenshot.ui.order.activity.OrderDetailsSubActivity;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.FormatTime;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.FormatTextView;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 订单列表
 */
public class OrderListAdapter extends SwipeRefreshAdapter<OrderListBean.OrderBean> {

    FormatTime time;

    List<GiveUpReasonListBean.ReasonBean> reasonList;

    public OrderListAdapter(Context context) {
        super(context);
        time = new FormatTime();
    }

    public void setReasonList(List<GiveUpReasonListBean.ReasonBean> reasonList) {
        this.reasonList = reasonList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final OrderListBean.OrderBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", bean);
                bundle.putSerializable("list", (Serializable) reasonList);
                IntentUtil.startActivity(context, OrderDetailsSubActivity.class, bundle);
//                IntentUtil.startActivity(context, OrderDetailsActivity.class,bundle);
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

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
        @InjectView(R.id.order_give_up_tv)
        TextView orderGiveUpTv;
        @InjectView(R.id.order_delete_tv)
        TextView orderDeleteTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(final OrderListBean.OrderBean bean) {
            taskNumTv.setText(String.format(context.getString(R.string.task_num), bean.getOrder_num()));
            String state = bean.getStatus();
            if (!StringUtils.isEmpty(state)) {
                switch (state) {
                    case "1":
                        stateTv.setText(R.string.doing);
                        orderGiveUpTv.setVisibility(View.VISIBLE);
                        orderDeleteTv.setVisibility(View.GONE);
                        break;
                    case "2":
                        stateTv.setText(R.string.been_cancelled);
                        orderGiveUpTv.setVisibility(View.GONE);
                        orderDeleteTv.setVisibility(View.VISIBLE);
                        break;
                    case "3":
                        stateTv.setText(R.string.have_finished);
                        orderGiveUpTv.setVisibility(View.GONE);
                        orderDeleteTv.setVisibility(View.VISIBLE);
                        break;
                }
            }
            goodsTv.setText(bean.getBadyname());
            merchantTv.setText(bean.getShopname());
            commissionTv.setFormatText(bean.getPrice());
            ImageHelper.load(context, bean.getBadyimg(), picIv, null, true, R.drawable.default_pic, R.drawable.default_pic);
            orderGiveUpTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.giveUpOrder(bean.getId());
                    }
                }
            });
            orderDeleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.deleteOrder(bean.getOrder_num());
                    }
                }
            });
        }
    }

    OrderOperationListener listener;

    public interface OrderOperationListener {
        void giveUpOrder(String order_num);

        void deleteOrder(String order_num);
    }

    public void setOrderOperationListener(OrderOperationListener listener) {
        this.listener = listener;
    }


}
