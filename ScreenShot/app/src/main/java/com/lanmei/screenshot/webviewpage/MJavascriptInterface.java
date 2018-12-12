package com.lanmei.screenshot.webviewpage;

import android.content.Context;

import com.lanmei.screenshot.utils.CommonUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/2/10.
 */

public class MJavascriptInterface {
    private Context context;
    private List<String> imageUrls;

    public MJavascriptInterface(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img) {
        CommonUtils.showPhotoBrowserActivity(context,imageUrls,img);
    }
}
