package com.lanmei.screenshot.event;

import com.lanmei.screenshot.bean.GiveUpReasonListBean;

import java.util.List;

/**
 * Created by xkai on 2018/8/10.
 * 放弃订单原因列表事件
 */

public class GiveUpReasonEvent {

    private List<GiveUpReasonListBean.ReasonBean> reasonList;

    public List<GiveUpReasonListBean.ReasonBean> getReasonList() {
        return reasonList;
    }

    public GiveUpReasonEvent(List<GiveUpReasonListBean.ReasonBean> reasonList){
        this.reasonList = reasonList;
    }

}
