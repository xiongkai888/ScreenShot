package com.lanmei.screenshot.event;

import android.graphics.Bitmap;

/**
 * Created by xkai on 2018/7/18.
 * 截图（截屏）事件
 */

public class ScreenShotEvent {

    private Bitmap bitmap;
    private String path;//截图图片路径

    public String getPath() {
        return path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public ScreenShotEvent(Bitmap bitmap,String path){
        this.bitmap = bitmap;
        this.path = path;
    }
}
