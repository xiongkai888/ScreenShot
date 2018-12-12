package com.lanmei.screenshot.bean;

import com.alibaba.fastjson.JSON;
import com.xson.common.bean.AbsListBean;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

import java.io.Serializable;
import java.util.List;

/**
 * @author xkai 订单列表
 */
public class OrderListBean extends AbsListBean {


    public String data;


    public List<OrderBean> dataList;

    public List<OrderBean> getDataList() {
        try {
            L.d("BeanRequest", "列表解密后：" + Des.decode(data));
            dataList = JSON.parseArray(Des.decode(data), OrderBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }


    /**
     * Created by xkai on 2018/4/25.
     * 订单
     */

    public static class OrderBean implements Serializable{


        private String order_num;//任务编号
        private String badyname;//宝贝名字
        private String shopname;//商家名字
        private String status;//状态(1|2|3=>执行中|已取消|已完成)
        private String price;//佣金
        private String receipt;//是否接单(0|1=>未接单|已接单)
        /**
         * id : 39
         * addtime : 1532402886
         * uid : 44
         * tid : 62
         * num : 2
         * isdel : 0
         */

        private String id;
        private String addtime;
        private String uid;
        private String tid;
        private String num;
        private String isdel;
        private String badyimg;

        public void setBadyimg(String badyimg) {
            this.badyimg = badyimg;
        }

        public String getBadyimg() {
            return badyimg;
        }

        public String getOrder_num() {
            return order_num;
        }

        public String getBadyname() {
            return badyname;
        }

        public String getShopname() {
            return shopname;
        }

        public String getStatus() {
            return status;
        }

        public String getPrice() {
            return price;
        }

        public String getReceipt() {
            return receipt;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public void setBadyname(String badyname) {
            this.badyname = badyname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setReceipt(String receipt) {
            this.receipt = receipt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getIsdel() {
            return isdel;
        }

        public void setIsdel(String isdel) {
            this.isdel = isdel;
        }
    }
}