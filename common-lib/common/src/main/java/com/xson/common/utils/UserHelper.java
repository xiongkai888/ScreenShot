package com.xson.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.xson.common.bean.UserBean;

/**
 * @author Milk
 */
public class UserHelper {

    private static UserHelper instance;
    private final SharedPreferences sp;
    private UserBean userBean;//用户信息
//    private RateObjectBean rateObjectBean;

    public static UserHelper getInstance(Context context) {
        if(instance == null) {
            instance = new UserHelper(context);
        }
        return instance;
    }

    private UserHelper(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public boolean hasLogin() {
        return userBean != null || getUserBean() != null;
    }

    public void cleanLogin() {
        userBean = null;
        sp.edit().clear().commit();
    }

    public void savePhone(String phone){
        sp.edit().putString("Phone", phone).commit();
    }
    public void savePwd(String pwd){
        sp.edit().putString("pwd", pwd).commit();
    }
    public void saveToken(String token){
        sp.edit().putString("token", token).commit();
    }
    public String getToken(){
        return sp.getString("token", "");
    }
    public String getPhone(){
        return sp.getString("Phone", "");
    }
    public String getPwd(){
        return sp.getString("pwd", "");
    }

    public UserBean getUserBean() {
        if(userBean != null)
            return userBean;

        String json = sp.getString("bean", null);
        if(json == null)
            return null;

        userBean = JSONObject.parseObject(json, UserBean.class);
        return userBean;
    }

    public void saveBean(UserBean userBean) {
        this.userBean = userBean;
        SharedPreferences.Editor editor = sp.edit().putString("bean", JSONObject.toJSONString(this.userBean, false));
        if(Build.VERSION.SDK_INT >= 9) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public boolean hasStoreCode() {
        String code = sp.getString("storeCode", null);
        return !TextUtils.isEmpty(code);
    }

    public void saveStoreCode(String code) {
        sp.edit().putString("storeCode", code).commit();
    }


}