package com.lanmei.screenshot.bean;

import com.alibaba.fastjson.JSON;
import com.xson.common.bean.AbsListBean;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

import java.io.Serializable;
import java.util.List;

/**
 * @author xkai 新闻列表
 */
public class NewsListBean extends AbsListBean {


    public String data;

    public List<NewsBean> dataList;

    @Override
    public List<NewsBean> getDataList() {
        try {
            L.d("BeanRequest", "列表解密后：" + Des.decode(data));
            dataList = JSON.parseArray(Des.decode(data), NewsBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Created by xkai on 2018/7/23.
     * 新闻列表
     */

    public static class NewsBean implements Serializable{


        /**
         * id : 51
         * tid : 1
         * title : 产品介绍
         * intro :
         * content : <p>
         产品介绍
         </p>
         * pic : http://stdrimages.img-cn-shenzhen.aliyuncs.com/170525/5926ea8bd2114.jpg
         * status : 1
         * addtime : 1495720468
         * stick : 1
         */

        private String id;
        private String tid;
        private String title;
        private String intro;
        private String content;
        private String pic;
        private String status;
        private String addtime;
        private String stick;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getStick() {
            return stick;
        }

        public void setStick(String stick) {
            this.stick = stick;
        }
    }

}