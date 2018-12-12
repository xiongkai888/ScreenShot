package com.lanmei.screenshot.utils;

import android.content.Context;

public class SharedAccount {

	private static SharedPreferencesTool share;
	private SharedAccount(){}
	private static SharedAccount instance = null;
	public static SharedAccount getInstance(Context context){
		if (instance == null) {
			instance = new SharedAccount();
		}
		share = SharedPreferencesTool.getInstance(context, "account");
		return instance;
	}
	

	public void saveMobile(String mobile){
		share.edit().putString("mobile", mobile).commit();
	}
	public void saveServiceId(String serviceId){
		share.edit().putString("serviceId", serviceId).commit();
	}
	public void saveLoginCount(int count){
		share.edit().putInt("count", count).commit();
	}
	public void saveAd(String ad){//轮播图
		share.edit().putString("ad", ad).commit();
	}

	public String getAd(){
		return share.getString("ad", "");
	}

	public String getMobile(){
		return share.getString("mobile", "");
	}
	public String getServiceId(){
		return share.getString("serviceId", "");
	}

	public int getLoginCount(){
		return share.getInt("count", 0);
	}


	public void setNoFirstLogin(boolean isFirstLogin){
		share.edit().putBoolean("isFirstLogin", isFirstLogin).commit();
	}

	public boolean isFirstLogin(){
		return share.getBoolean("isFirstLogin", false);
	}

	public void clear(){
		share.clear();
	}


//	public InviteBean getInviteBean() {
//		if(inviteBean != null)
//			return inviteBean;
//
//		String json = share.getString("inviteBean", null);
//		if(json == null)
//			return null;
//
//		inviteBean = JSONObject.parseObject(json, InviteBean.class);
//		return inviteBean;
//	}
//
//	InviteBean inviteBean;//系统信息（分享）
//
//	public void saveInviteBean(InviteBean inviteBean) {
//		this.inviteBean = inviteBean;
//		SharedPreferences.Editor editor = share.edit().putString("inviteBean", JSONObject.toJSONString(this.inviteBean, false));
//		if(Build.VERSION.SDK_INT >= 9) {
//			editor.apply();
//		} else {
//			editor.commit();
//		}
//	}
}
