package com.lanmei.screenshot.event;

/**
 * Created by xkai on 2018/7/19.
 */

public class CityJsonToStringEvent {

    private String cityJson;

    public String getCityJson() {
        return cityJson;
    }

    public CityJsonToStringEvent(String cityJson){
        this.cityJson = cityJson;
    }
}
