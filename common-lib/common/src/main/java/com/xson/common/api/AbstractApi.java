package com.xson.common.api;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.xson.common.bean.UserBean;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UserHelper;
import com.xson.common.utils.des.Des;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Milk <249828165@qq.com>
 */
public abstract class AbstractApi {

    private int p;
    public static String API_URL = "http://zsqz2.hytqhy.com/api/";
    public HashMap<String, Object> paramsHashMap = new HashMap<String, Object>();
    public long datatime;
    public String lm;
    public String key = "zsqz";//必须明钥

    public static enum Method {
        GET,
        POST,
    }

    public static enum Enctype {
        TEXT_PLAIN,
        MULTIPART,
    }

    public void setTime(Context context, long time) {
        datatime = time;
        lm = L.getMD5Str(context, datatime + "");
    }

    public String getUserId(Context context) {
        UserBean userBean = UserHelper.getInstance(context).getUserBean();
        return StringUtils.isEmpty(userBean) ? "" : userBean.getId();
    }

    protected abstract String getPath();

    public Method requestMethod() {
        return Method.POST;
    }

    public Enctype requestEnctype() {
        return Enctype.TEXT_PLAIN;
    }

    public String getUrl() {
        return API_URL + getPath();
    }

    public void setPage(int page) {
        this.p = page;
    }

    public AbstractApi addParams(String key, Object value) {
        paramsHashMap.put(key, value);
        return this;
    }

    public Map<String, Object> getParams() {
        boolean hasUid = false;
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("key", key);
        for (Map.Entry<String, Object> item : paramsHashMap.entrySet()) {
            if (item.getKey() != null && item.getValue() != null) {
                if (StringUtils.isSame(L.uid, item.getKey())) {
                    hasUid = true;
                }
                params.put(item.getKey(), item.getValue());
//                if (item.getValue() instanceof com.alibaba.fastjson.JSONArray) {
//                    params.put(item.getKey(), (com.alibaba.fastjson.JSONArray) item.getValue());
//                    L.d(L.TAG, "JSONArray");
//                } else {
//                    params.put(item.getKey(), item.getValue());
//                    L.d(L.TAG, item.getKey() + "," + item.getValue());
//                }

            }
        }
        if (p > 0) {
            params.put(L.p, p);
        } else {
            params.remove(L.p);
        }
        if (!hasUid) {
            params.remove(L.datatime);
            params.remove(L.lm);
        }
        if (requestMethod() == Method.GET) {
            return params;
        }
        String dataStr = getData(params);
        params.clear();
        params.put(L.data, dataStr);
        return params;
    }

    public String getData(HashMap<String, Object> params) {
        JSONObject object = new JSONObject();
        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            object.put((String) entry.getKey(), entry.getValue());
        }
        L.d("BeanRequest", object.toJSONString());
        return Des.encrypt(object.toJSONString());
    }
}
