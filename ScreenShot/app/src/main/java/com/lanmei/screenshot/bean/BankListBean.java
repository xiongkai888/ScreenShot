package com.lanmei.screenshot.bean;

import com.alibaba.fastjson.JSON;
import com.xson.common.bean.DataBean;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

import java.util.List;

/**
 * @author xkai 银行卡列表和职业列表共用
 */
public class BankListBean<T> extends DataBean<T> {

    public List<BankBean> getData() {
        List<BankBean>  dataList = null;
        try {
            L.d("BeanRequest", "列表解密后：" + Des.decode((String) data));
           dataList = JSON.parseArray(Des.decode((String) data), BankBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Created by xkai on 2017/7/7.
     * 银行卡
     */

    public static class BankBean {

        /**
         * id : 1
         * name : 招商银行
         */

        private String id;
        private String name;
        /**
         * status : 1    （职业多了这个参数）
         */

        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}