package com.xson.common.bean;

/**
 * @author xson
 * 带状态和描述的bean
{
"status":0,
"msg":"成功"
}
 */
public class BaseBean {


    /**
     * status : 1
     * code : 11
     * msg : 验证码发送成功
     */

    private int status;
    private int code;
    private String msg;
    private String info;

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
