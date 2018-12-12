package com.xson.common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.xson.common.bean.UserBean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Milk <249828165@qq.com>
 */
public class L {

    public static boolean debug = true;
    public final static String TAG = "BeanRequest";
    public final static String p = "p";
    public final static String path = "path";
    public final static String uid = "uid";
    public final static String data = "data";
    public final static String datatime = "datatime";
    public final static String lm = "lm";
    public final static String md5 = "$^&*lanmei$@#1";

    public static int v(String tag, String msg) {
        if (!debug)
            return 0;
        return Log.v(tag, msg);
    }

    public static int v(String tag, String msg, Throwable tr) {
        if (!debug)
            return 0;
        return Log.v(tag, msg, tr);
    }

    public static int d(String tag, String msg) {
        if (!debug || StringUtils.isEmpty(msg))
            return 0;
        if (msg.length() > 4000) {
            int size = msg.length();
            for (int i = 0; i < size; i += 4000) {
                if (i + 4000 < size) {
                    Log.d(tag, msg.substring(i, i + 4000));
                } else {
                    Log.d(tag, msg.substring(i, msg.length()));
                }
            }
        } else {
            Log.d(tag, msg);
        }
        return 0;
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (!debug)
            return 0;
        return Log.d(tag, msg, tr);
    }

    public static int d(String format, Object... args) {
        return d("TK_LOG_D", String.format(format, args));
    }

    public static int d(Throwable t) {
        return d("TK_LOG_D", t.getMessage(), t);
    }

    public static int i(String tag, String msg) {
        if (!debug)
            return 0;
        return Log.i(tag, msg);
    }

    public static int i(String tag, String msg, Throwable tr) {
        if (!debug)
            return 0;
        return Log.i(tag, msg, tr);
    }

    public static int w(String tag, String msg) {
        return Log.w(tag, msg);
    }

    public static int w(String tag, String msg, Throwable tr) {
        return Log.w(tag, msg, tr);
    }

    public static int w(String tag, Throwable tr) {
        return Log.w(tag, tr);
    }

    public static int e(String tag, String msg) {
        return Log.e(tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return Log.e(tag, msg, tr);
    }

    public static int e(String format, Object... args) {
        return e("TK_LOG_E", String.format(format, args));
    }

    public static int e(Throwable t) {
        return Log.e("TK_LOG_E", t.getMessage(), t);
    }

    /**
     * MD5加密
     * @param context
     * @param str  时间戳
     * @return
     */
    public static String getMD5Str(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        str = (md5 + getUserId(context) + str);
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getUserId(Context context) {
        UserBean bean = UserHelper.getInstance(context).getUserBean();
        if (StringUtils.isEmpty(bean)) {
            return "";
        }
        return bean.getId();
    }


}
