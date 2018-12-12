package com.lanmei.screenshot.bean;

import com.alibaba.fastjson.JSON;
import com.xson.common.bean.DataBean;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

import java.util.List;

/**
 * @author xkai 首页轮播图
 */
public class AdListBean<T> extends DataBean<T> {

    public List<AdBean> getData() {
        List<AdBean>  dataList = null;
        try {
            L.d("BeanRequest", "列表解密后：" + Des.decode((String) data));
           dataList = JSON.parseArray(Des.decode((String) data), AdBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Created by xkai on 2017/7/7.
     * 首页轮播图
     */

    public static class AdBean {

        /**
         * id : 1
         * sort : 1
         * classid : 1
         * pic : http://zsqz2.hytqhy.com/Uploads/adpic/2018-06-12/1.jpg
         * smallpic : 
         * link : 
         * uptime : null
         * addtime : null
         * state : 1
         */

        private String id;
        private String sort;
        private String classid;
        private String pic;
        private String smallpic;
        private String link;
        private String uptime;
        private String addtime;
        private String state;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getClassid() {
            return classid;
        }

        public void setClassid(String classid) {
            this.classid = classid;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getSmallpic() {
            return smallpic;
        }

        public void setSmallpic(String smallpic) {
            this.smallpic = smallpic;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getUptime() {
            return uptime;
        }

        public void setUptime(String uptime) {
            this.uptime = uptime;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

}