package com.lanmei.screenshot.bean;

import com.alibaba.fastjson.JSON;
import com.xson.common.bean.DataBean;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

import java.io.Serializable;
import java.util.List;

/**
 * @author xkai 放弃订单 原因列表
 */
public class GiveUpReasonListBean<T> extends DataBean<T> {

    public List<ReasonBean> getData() {
        List<ReasonBean>  dataList = null;
        try {
            L.d("BeanRequest", "列表解密后：" + Des.decode((String) data));
           dataList = JSON.parseArray(Des.decode((String) data), ReasonBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Created by xkai on 2017/7/7.
     * 首页轮播图
     */

    public static class ReasonBean implements Serializable{

        /**
         * id : 2
         * title : 好评找不到
         */

        private String id;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}