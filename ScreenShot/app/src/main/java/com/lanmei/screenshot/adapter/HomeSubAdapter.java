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
import com.lanmei.screenshot.bean.HomeSubListBean;
import com.lanmei.screenshot.ui.home.activity.NewsListActivity;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 首页
 */
public class HomeSubAdapter extends SwipeRefreshAdapter<HomeSubListBean.HomeSubBean> {


    public HomeSubAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, final int position) {
        final HomeSubListBean.HomeSubBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("tid",bean.getId());
                bundle.putString("title",bean.getTitle());
                IntentUtil.startActivity(context, NewsListActivity.class,bundle);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.pic_iv)
        ImageView picIv;
        @InjectView(R.id.title_tv)
        TextView titleTv;
        @InjectView(R.id.title_sub_tv)
        TextView titleSubTv;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);

        }

        public void setParameter(HomeSubListBean.HomeSubBean bean) {
            titleTv.setText(bean.getTitle());
            titleSubTv.setText(bean.getSubtitle());
            ImageHelper.load(context, bean.getImg(), picIv, null, true, R.drawable.default_pic, R.drawable.default_pic);
        }

    }


}
