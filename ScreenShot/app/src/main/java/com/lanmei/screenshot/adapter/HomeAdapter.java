package com.lanmei.screenshot.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.lanmei.screenshot.R;
import com.lanmei.screenshot.bean.AdListBean;
import com.lanmei.screenshot.bean.HomeListBean;
import com.lanmei.screenshot.ui.home.activity.NewsListActivity;
import com.lanmei.screenshot.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 首页
 */
public class HomeAdapter extends SwipeRefreshAdapter<HomeListBean.HomeBean> {

    public int TYPE_BANNER = 100;
    boolean isFirst = true;
    List<AdListBean.AdBean> list;//轮播图列表

    public HomeAdapter(Context context) {
        super(context);
    }

    public void setAdList(List<AdListBean.AdBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) { // banner
            return new BannerViewHolder(LayoutInflater.from(context).inflate(R.layout.head_home, parent, false));
        }
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_BANNER) {
            onBindBannerViewHolder(holder); // banner
            return;
        }
        final HomeListBean.HomeBean bean = getItem(position - 1);
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

        public void setParameter(HomeListBean.HomeBean bean) {
            titleTv.setText(bean.getTitle());
            titleSubTv.setText(bean.getSubtitle());
            ImageHelper.load(context, bean.getImg(), picIv, null, true, R.drawable.default_pic, R.drawable.default_pic);
        }

    }

    @Override
    public int getItemViewType2(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        }
        return super.getItemViewType2(position);
    }


    //头部
    public class BannerViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.banner)
        ConvenientBanner banner;

        @InjectView(R.id.principal_tv)
        TextView principalTv;
        @InjectView(R.id.balance_tv)
        TextView balanceTv;
        @InjectView(R.id.integral_tv)
        TextView integralTv;

        BannerViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter() {

            UserBean userBean = CommonUtils.getUserBean(context);
            if (userBean != null) {
                principalTv.setText(userBean.getPrincipal());
                balanceTv.setText(userBean.getBalance());
                integralTv.setText(userBean.getIntegral());
            }

            if (StringUtils.isEmpty(list) || !isFirst) {
                return;
            }
            banner.setPages(new CBViewHolderCreator() {
                @Override
                public Object createHolder() {
                    return new HomeAdAdapter();
                }
            }, list);
            banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
            banner.setPageIndicator(new int[]{R.drawable.shape_item_index_white, R.drawable.shape_item_index_red});
            banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
            banner.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                }
            });
            if (list.size() == 1) {
                return;
            }
            if (isFirst) {
                isFirst = !isFirst;
                banner.startTurning(3000);
            }
        }
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    public void onBindBannerViewHolder(RecyclerView.ViewHolder holder) {
        final BannerViewHolder viewHolder = (BannerViewHolder) holder;
        viewHolder.setParameter();
    }

}
