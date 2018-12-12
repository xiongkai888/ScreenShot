package com.xson.common.api;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.des.Des;

import java.io.File;
import java.lang.reflect.Field;
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

    public static enum Method {
        GET,
        POST,
    }

    public static enum Enctype {
        TEXT_PLAIN,
        MULTIPART,
    }

    //    protected abstract void setPath(String path);

    public void setTime(Context context, long time) {
        datatime = time;
        lm = L.getMD5Str(context, datatime + "");
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
        Field[] field;
        Class clazz = getClass();
        try {
            for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
                field = c.getDeclaredFields();
                for (Field f : field) {
                    f.setAccessible(true);
                    Object value = f.get(this);
                    if (value != null &&
                            !L.API_URL.equals(f.getName()) &&
                            !L.paramsHashMap.equals(f.getName()) &&
                            !L.serialVersionUID.equals(f.getName()) &&
                            !f.getName().contains(L.shadow) &&
                            !L.path.equals(f.getName())) {
                        params.put(f.getName(), value);
                    }
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            L.e(e);
        }
        for (Map.Entry<String, Object> item : paramsHashMap.entrySet()) {
            if (item.getKey() != null && item.getValue() != null) {
                if (StringUtils.isSame(L.uid, item.getKey())) {
                    hasUid = true;
                }
                if (item.getValue() instanceof com.alibaba.fastjson.JSONArray) {
                    params.put(item.getKey(), (com.alibaba.fastjson.JSONArray) item.getValue());
                    L.d(L.TAG, "JSONArray");
                } else {
                    params.put(item.getKey(), item.getValue());
//                    L.d(L.TAG, item.getKey() + "," + item.getValue());
                }

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

    public void handleParams(Context context, Map<String, Object> params) {
        HashMap<String, Object> fileMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof File)
                fileMap.put(entry.getKey(), entry.getValue());
            else if (value instanceof Iterable) { // List<File>, Collection<File>, etc...
                Iterator iter = ((Iterable) value).iterator();
                if (iter.hasNext() && iter.next() instanceof File) {
                    fileMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        if (!fileMap.isEmpty())
            for (String key : fileMap.keySet()) {
                params.remove(key);
            }
        params.putAll(fileMap);
    }

}
