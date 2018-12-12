package com.lanmei.screenshot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.screenshot.R;
import com.lanmei.screenshot.bean.GiveUpReasonListBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 放弃订单 原因
 */
public class GiveUpReasonAdapter extends SwipeRefreshAdapter<GiveUpReasonListBean.ReasonBean> {


    public GiveUpReasonAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_give_up_reason, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, final int position) {
        final GiveUpReasonListBean.ReasonBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.giveUpReason(bean);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.content_tv)
        TextView contentTv;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);

        }

        public void setParameter(GiveUpReasonListBean.ReasonBean bean) {
            contentTv.setText(bean.getTitle());
        }

    }

    GiveUpReasonListener listener;

    public interface GiveUpReasonListener{
        void giveUpReason(GiveUpReasonListBean.ReasonBean bean);
    }

    public void setGiveUpReasonListener(GiveUpReasonListener listener){
        this.listener = listener;
    }


}
