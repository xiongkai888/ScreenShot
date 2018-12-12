package com.lanmei.screenshot;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.squareup.leakcanary.LeakCanary;
import com.xson.common.app.BaseApp;

/**
 * Created by xkai on 2018/4/13.
 */

public class ScreenShotApp extends BaseApp {


    @Override
    protected void installMonitor() {
        LeakCanary.install(this);
    }

    @Override
    public void watch(Object object) {

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

}
