package com.lanmei.screenshot.bean;

import com.xson.common.bean.DataBean;
import com.xson.common.utils.JsonUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

/**
 * Created by xkai on 2018/7/24.
 * 点击
 */

public class ReceivingOrderBean<T> extends DataBean<T> {

    public ReceivingBean getData() {
        try {
            ReceivingBean bean = JsonUtil.jsonToBean(Des.decode((String) data), ReceivingBean.class);
            L.d("BeanRequest", "（我有时间）订单信息：" + Des.decode((String) data));
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class ReceivingBean{

        /**
         * order_num : t100201807241128066471
         * uid : 44
         * status : 1
         * addtime : 1532402886
         * tid : 62
         * num : 2
         * price : 5.00
         */

        private String order_num;
        private String uid;
        private int status;
        private int addtime;
        private String tid;
        private int num;
        private String price;

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getAddtime() {
            return addtime;
        }

        public void setAddtime(int addtime) {
            this.addtime = addtime;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

}
