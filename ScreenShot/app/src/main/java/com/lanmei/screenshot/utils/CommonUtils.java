package com.lanmei.screenshot.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.lanmei.screenshot.R;
import com.lanmei.screenshot.adapter.BannerHolderView;
import com.lanmei.screenshot.api.ScreenShotApi;
import com.lanmei.screenshot.bean.UserDataBean;
import com.lanmei.screenshot.event.SetUserEvent;
import com.lanmei.screenshot.ui.login.LoginActivity;
import com.lanmei.screenshot.webviewpage.PhotoBrowserActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UserHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class CommonUtils {

    public final static String isOne = "1";
    public final static String isZero = "0";
    public final static String isTwo = "2";
    public final static String isThree = "3";

    public static int quantity = 1;



    public static String getString(BaseBean response) {
        return StringUtils.isEmpty(response.getMsg()) ? response.getInfo() : response.getMsg();
    }

    /**
     * 获取TextView 字符串
     *
     * @param textView
     * @return
     */
    public static String getStringByTextView(TextView textView) {
        return textView.getText().toString().trim();
    }

    /**
     * 获取EditText 字符串
     *
     * @param editText
     * @return
     */
    public static String getStringByEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static boolean isLogin(Context context) {
        if (!UserHelper.getInstance(context).hasLogin()) {
            IntentUtil.startActivity(context, LoginActivity.class);
            return false;
        }
        return true;
    }

    /**
     * 获取年月日
     */
    public static String getData() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
    }

    public static String getUserId(Context context) {
        UserBean bean = UserHelper.getInstance(context).getUserBean();
        if (StringUtils.isEmpty(bean)) {
            return "";
        }
        return bean.getId();
    }

    public static UserBean getUserBean(Context context) {
        return UserHelper.getInstance(context).getUserBean();
    }

    /**
     * @param banner
     * @param list
     * @param isTurning 防止重复  banner.startTurning(3000);
     */
    public static void setBanner(ConvenientBanner banner, List<String> list, boolean isTurning) {
        //初始化商品图片轮播
        banner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new BannerHolderView();
            }
        }, list);
        banner.setPageIndicator(new int[]{R.drawable.shape_item_index_white, R.drawable.shape_item_index_red});
        banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        if (list.size() == 1) {
            return;
        }
        if (!isTurning) {
            return;
        }
        banner.startTurning(3000);
    }



    //获取用户信息
    public static void loadUserInfo(final Context context, final UserInfoListener l) {
        ScreenShotApi api = new ScreenShotApi("app/info");
        api.addParams("uid", api.getUserId(context));
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<UserDataBean<String>>() {
            @Override
            public void onResponse(UserDataBean<String> response) {
                if (context == null) {
                    return;
                }
                UserBean bean = response.getData();
                if (bean != null) {
                    if (l != null) {
                        l.userInfo(bean);
                    }
                    UserHelper.getInstance(context).saveBean(bean);
                    EventBus.getDefault().post(new SetUserEvent());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (l != null) {
                    l.error(error.getMessage());
                }
            }
        });
    }


    public interface UserInfoListener {
        void userInfo(UserBean bean);

        void error(String error);
    }



    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        if (StringUtils.isEmpty(cardId) || cardId.startsWith(isZero)) {
            return false;
        }
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0 || !nonCheckCodeCardId.matches("\\d+")) {
            return 'N';//如果传的不是数据返回N
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 浏览图片
     *
     * @param context
     * @param arry     图片地址数组
     * @param imageUrl 点击的图片地址
     */
    public static void showPhotoBrowserActivity(Context context, List<String> arry, String imageUrl) {
        Intent intent = new Intent();
        intent.putExtra("imageUrls", (Serializable) arry);
        intent.putExtra("curImageUrl", imageUrl);
        intent.setClass(context, PhotoBrowserActivity.class);
        context.startActivity(intent);
    }

}
