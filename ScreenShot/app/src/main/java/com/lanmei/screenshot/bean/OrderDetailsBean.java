package com.lanmei.screenshot.bean;

import com.xson.common.bean.DataBean;
import com.xson.common.utils.JsonUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

import java.util.List;

/**
 * Created by xkai on 2018/7/26.
 * 订单详情
 */

public class OrderDetailsBean<T> extends DataBean<T> {

    public DetailsBean getData() {
        try {
            DetailsBean bean = JsonUtil.jsonToBean(Des.decode((String) data), DetailsBean.class);
            L.d("BeanRequest", "订单详情：" + Des.decode((String) data));
            return bean;
        } catch (Exception e) {
            L.d("BeanRequest", "无法解析数据");
            e.printStackTrace();
        }
        return null;
    }

    public static class DetailsBean{

        /**
         * id : 55
         * status : 1
         * addtime : 1534323090
         * uid : 44
         * price : 5.00
         * tid : 69
         * num : 1
         * receipt : 0
         * order_num : t100201808151651305471
         * badyname : 天短袖t恤男学生半袖上衣服潮流翻领polo衫2018新款男装体恤衫
         * shopname : 淘宝店铺
         * isdel : 0
         * badyimg : http://zsqz2.hytqhy.com/Uploads/order/2018-06-25/5b5183ddb57cd.jpg
         * upload_img : null
         * pay_img : null
         * taobao_num : null
         * pay_price : null
         * type_tkl : null
         * tkl : null
         * shop_tkl : null
         * zhaocha : [["找茬1","11","1"],["找","茬2","222"],["找","茬2","222"]]
         * area : 广东省广州市天河区
         * search_id : 1
         * search_val : 搜索
         * search_order : 综合排序
         * low_price : 80
         * high_price : 120
         * search_term : 包邮,天猫,消费者保障,淘金币抵钱,7+天内退货
         * claim : 下单备注
         * bady : {"badyimg":["http://zsqz2.hytqhy.com/Uploads/order/2018-06-25/5b5183ddb57cd.jpg"],"badyname":"天短袖t恤男学生半袖上衣服潮流翻领polo衫2018新款男装体恤衫","badyprice":"100","badynum":"1","badyurl":"https://detail.m.tmall.com/item.htm?spm=a1z10.1-b.w4004-17895562035.21.4bc81a3d7v6tqn&abtest=_AB-LR73-PR73&pvid=5b2d0248-b960-40ca-a40a-758a1571f2f2&pos=11&abbucket=_AB-M73_B11&acm=03068.1003.1.702815&id=567903220152&scm=1007.12941.28043.100200300000000&sm=true&smToken=99a63a78aadf4e0d9dc3bdb28e9372","badyspec":"颜色","badyspecvalue":"红色","spec":[{"spec":"颜色","spacevalue":"红色"}]}
         * bady1 : {"badyimg":["http://zsqz2.hytqhy.com/Uploads/order/2018-06-25/5b5183ddb57cd.jpg"],"badyname":"天短袖t恤男学生半袖上衣服潮流翻领polo衫2018新款男装体恤衫","badyprice":"100","badynum":"1","badyurl":"https://detail.m.tmall.com/item.htm?spm=a1z10.1-b.w4004-17895562035.21.4bc81a3d7v6tqn&abtest=_AB-LR73-PR73&pvid=5b2d0248-b960-40ca-a40a-758a1571f2f2&pos=11&abbucket=_AB-M73_B11&acm=03068.1003.1.702815&id=567903220152&scm=1007.12941.28043.100200300000000&sm=true&smToken=99a63a78aadf4e0d9dc3bdb28e9372","spec":[]}
         * praise : {"text":"好评找茬","time":"2018-07-26"}
         */

        private String id;
        private String status;
        private String addtime;
        private String uid;
        private String price;
        private String tid;
        private String num;
        private String receipt;
        private String order_num;
        private String badyname;
        private String shopname;
        private String isdel;
        private String badyimg;
        private String upload_img;
        private String pay_img;
        private String taobao_num;
        private String pay_price;
        private String type_tkl;
        private String tkl;
        private String shop_tkl;
        private String area;
        private String search_id;
        private String search_val;
        private String search_order;
        private String low_price;
        private String high_price;
        private String search_term;
        private String claim;
        private BadyBean bady;
        private Bady1Bean bady1;
        private PraiseBean praise;
        private List<List<String>> zhaocha;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
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

        public String getReceipt() {
            return receipt;
        }

        public void setReceipt(String receipt) {
            this.receipt = receipt;
        }

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public String getBadyname() {
            return badyname;
        }

        public void setBadyname(String badyname) {
            this.badyname = badyname;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getIsdel() {
            return isdel;
        }

        public void setIsdel(String isdel) {
            this.isdel = isdel;
        }

        public String getBadyimg() {
            return badyimg;
        }

        public void setBadyimg(String badyimg) {
            this.badyimg = badyimg;
        }

        public String getUpload_img() {
            return upload_img;
        }

        public void setUpload_img(String upload_img) {
            this.upload_img = upload_img;
        }

        public String getPay_img() {
            return pay_img;
        }

        public void setPay_img(String pay_img) {
            this.pay_img = pay_img;
        }

        public String getTaobao_num() {
            return taobao_num;
        }

        public void setTaobao_num(String taobao_num) {
            this.taobao_num = taobao_num;
        }

        public String getPay_price() {
            return pay_price;
        }

        public void setPay_price(String pay_price) {
            this.pay_price = pay_price;
        }

        public String getType_tkl() {
            return type_tkl;
        }

        public void setType_tkl(String type_tkl) {
            this.type_tkl = type_tkl;
        }

        public String getTkl() {
            return tkl;
        }

        public void setTkl(String tkl) {
            this.tkl = tkl;
        }

        public String getShop_tkl() {
            return shop_tkl;
        }

        public void setShop_tkl(String shop_tkl) {
            this.shop_tkl = shop_tkl;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getSearch_id() {
            return search_id;
        }

        public void setSearch_id(String search_id) {
            this.search_id = search_id;
        }

        public String getSearch_val() {
            return search_val;
        }

        public void setSearch_val(String search_val) {
            this.search_val = search_val;
        }

        public String getSearch_order() {
            return search_order;
        }

        public void setSearch_order(String search_order) {
            this.search_order = search_order;
        }

        public String getLow_price() {
            return low_price;
        }

        public void setLow_price(String low_price) {
            this.low_price = low_price;
        }

        public String getHigh_price() {
            return high_price;
        }

        public void setHigh_price(String high_price) {
            this.high_price = high_price;
        }

        public String getSearch_term() {
            return search_term;
        }

        public void setSearch_term(String search_term) {
            this.search_term = search_term;
        }

        public String getClaim() {
            return claim;
        }

        public void setClaim(String claim) {
            this.claim = claim;
        }

        public BadyBean getBady() {
            return bady;
        }

        public void setBady(BadyBean bady) {
            this.bady = bady;
        }

        public Bady1Bean getBady1() {
            return bady1;
        }

        public void setBady1(Bady1Bean bady1) {
            this.bady1 = bady1;
        }

        public PraiseBean getPraise() {
            return praise;
        }

        public void setPraise(PraiseBean praise) {
            this.praise = praise;
        }

        public List<List<String>> getZhaocha() {
            return zhaocha;
        }

        public void setZhaocha(List<List<String>> zhaocha) {
            this.zhaocha = zhaocha;
        }

        public static class BadyBean {
            /**
             * badyimg : ["http://zsqz2.hytqhy.com/Uploads/order/2018-06-25/5b5183ddb57cd.jpg"]
             * badyname : 天短袖t恤男学生半袖上衣服潮流翻领polo衫2018新款男装体恤衫
             * badyprice : 100
             * badynum : 1
             * badyurl : https://detail.m.tmall.com/item.htm?spm=a1z10.1-b.w4004-17895562035.21.4bc81a3d7v6tqn&abtest=_AB-LR73-PR73&pvid=5b2d0248-b960-40ca-a40a-758a1571f2f2&pos=11&abbucket=_AB-M73_B11&acm=03068.1003.1.702815&id=567903220152&scm=1007.12941.28043.100200300000000&sm=true&smToken=99a63a78aadf4e0d9dc3bdb28e9372
             * badyspec : 颜色
             * badyspecvalue : 红色
             * spec : [{"spec":"颜色","spacevalue":"红色"}]
             */

            private String badyname;
            private String badyprice;
            private String badynum;
            private String badyurl;
            private String badyspec;
            private String badyspecvalue;
            private List<String> badyimg;
            private List<SpecBean> spec;

            public String getBadyname() {
                return badyname;
            }

            public void setBadyname(String badyname) {
                this.badyname = badyname;
            }

            public String getBadyprice() {
                return badyprice;
            }

            public void setBadyprice(String badyprice) {
                this.badyprice = badyprice;
            }

            public String getBadynum() {
                return badynum;
            }

            public void setBadynum(String badynum) {
                this.badynum = badynum;
            }

            public String getBadyurl() {
                return badyurl;
            }

            public void setBadyurl(String badyurl) {
                this.badyurl = badyurl;
            }

            public String getBadyspec() {
                return badyspec;
            }

            public void setBadyspec(String badyspec) {
                this.badyspec = badyspec;
            }

            public String getBadyspecvalue() {
                return badyspecvalue;
            }

            public void setBadyspecvalue(String badyspecvalue) {
                this.badyspecvalue = badyspecvalue;
            }

            public List<String> getBadyimg() {
                return badyimg;
            }

            public void setBadyimg(List<String> badyimg) {
                this.badyimg = badyimg;
            }

            public List<SpecBean> getSpec() {
                return spec;
            }

            public void setSpec(List<SpecBean> spec) {
                this.spec = spec;
            }

            public static class SpecBean {
                /**
                 * spec : 颜色
                 * spacevalue : 红色
                 */

                private String spec;
                private String spacevalue;

                public String getSpec() {
                    return spec;
                }

                public void setSpec(String spec) {
                    this.spec = spec;
                }

                public String getSpacevalue() {
                    return spacevalue;
                }

                public void setSpacevalue(String spacevalue) {
                    this.spacevalue = spacevalue;
                }
            }
        }

        public static class Bady1Bean {
            /**
             * badyimg : ["http://zsqz2.hytqhy.com/Uploads/order/2018-06-25/5b5183ddb57cd.jpg"]
             * badyname : 天短袖t恤男学生半袖上衣服潮流翻领polo衫2018新款男装体恤衫
             * badyprice : 100
             * badynum : 1
             * badyurl : https://detail.m.tmall.com/item.htm?spm=a1z10.1-b.w4004-17895562035.21.4bc81a3d7v6tqn&abtest=_AB-LR73-PR73&pvid=5b2d0248-b960-40ca-a40a-758a1571f2f2&pos=11&abbucket=_AB-M73_B11&acm=03068.1003.1.702815&id=567903220152&scm=1007.12941.28043.100200300000000&sm=true&smToken=99a63a78aadf4e0d9dc3bdb28e9372
             * spec : []
             */

            private String badyname;
            private String badyprice;
            private String badynum;
            private String badyurl;
            private List<String> badyimg;
            private List<?> spec;

            public String getBadyname() {
                return badyname;
            }

            public void setBadyname(String badyname) {
                this.badyname = badyname;
            }

            public String getBadyprice() {
                return badyprice;
            }

            public void setBadyprice(String badyprice) {
                this.badyprice = badyprice;
            }

            public String getBadynum() {
                return badynum;
            }

            public void setBadynum(String badynum) {
                this.badynum = badynum;
            }

            public String getBadyurl() {
                return badyurl;
            }

            public void setBadyurl(String badyurl) {
                this.badyurl = badyurl;
            }

            public List<String> getBadyimg() {
                return badyimg;
            }

            public void setBadyimg(List<String> badyimg) {
                this.badyimg = badyimg;
            }

            public List<?> getSpec() {
                return spec;
            }

            public void setSpec(List<?> spec) {
                this.spec = spec;
            }
        }

        public static class PraiseBean {
            /**
             * text : 好评找茬
             * time : 2018-07-26
             */

            private String text;
            private String time;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }

}
