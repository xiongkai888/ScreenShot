package com.lanmei.screenshot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.screenshot.R;
import com.lanmei.screenshot.bean.AccountDetailsListBean;
import com.lanmei.screenshot.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.FormatTime;
import com.xson.common.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 账户明细
 */
public class AccountDetailsAdapter extends SwipeRefreshAdapter<AccountDetailsListBean.RechargeResultBean> {

    FormatTime time;

    public AccountDetailsAdapter(Context context) {
        super(context);
        time = new FormatTime();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_account_details, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        AccountDetailsListBean.RechargeResultBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.consumptionNameTv.setText(bean.getTitle());
        if (StringUtils.isSame(bean.getType(), CommonUtils.isZero)){//进账
            viewHolder.consumptionSumTv.setText(String.format(context.getString(R.string.yuan_positive), bean.getMoney()));
            viewHolder.consumptionSumTv.setTextColor(context.getResources().getColor(R.color.red));
        }else {//出账
            viewHolder.consumptionSumTv.setText(String.format(context.getString(R.string.yuan_burden), bean.getMoney()));
            viewHolder.consumptionSumTv.setTextColor(context.getResources().getColor(R.color.color33cea6));
        }
        time.setTime(bean.getAddtime());
        viewHolder.timeTv.setText(time.formatterTime());
        viewHolder.balanceTv.setText(String.format(context.getString(R.string.balance), bean.getBalance()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.consumption_name_tv)
        TextView consumptionNameTv;
        @InjectView(R.id.consumption_sum_tv)
        TextView consumptionSumTv;
        @InjectView(R.id.balance_tv)
        TextView balanceTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

}
