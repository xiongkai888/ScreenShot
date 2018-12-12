package com.lanmei.screenshot.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2018/1/8.
 */

public class ScreenShotApi extends AbstractApi {

    private String path;

    public ScreenShotApi(String path){
        this.path = path;
    }

    @Override
    protected String getPath() {
        return path;
    }
}
