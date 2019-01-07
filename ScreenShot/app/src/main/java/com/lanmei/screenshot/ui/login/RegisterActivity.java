package com.lanmei.screenshot.ui.login;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lanmei.screenshot.R;
import com.lanmei.screenshot.api.ScreenShotApi;
import com.lanmei.screenshot.event.LogoutEvent;
import com.lanmei.screenshot.event.RegisterEvent;
import com.lanmei.screenshot.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.CodeCountDownTimer;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DrawClickableEditText;
import com.xson.common.widget.FormatTextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 注册、忘记密码、重设密码
 */
public class RegisterActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.phone_et)
    DrawClickableEditText phoneEt;
    @InjectView(R.id.code_et)
    DrawClickableEditText codeEt;
    @InjectView(R.id.obtainCode_bt)
    Button obtainCodeBt;
    @InjectView(R.id.pwd_et)
    DrawClickableEditText pwdEt;
    @InjectView(R.id.showPwd_iv)
    ImageView showPwdIv;
    @InjectView(R.id.pwd_again_et)
    DrawClickableEditText pwdAgainEt;
    @InjectView(R.id.showPwd_again_iv)
    ImageView showPwdAgainIv;
    @InjectView(R.id.referrer_phone_et)
    DrawClickableEditText referrerPhoneEt;
    @InjectView(R.id.ll_referrer_phone)
    LinearLayout llReferrerPhone;
    @InjectView(R.id.register_bt)
    Button button;
    @InjectView(R.id.agree_protocol_tv)
    FormatTextView agreeProtocolTv;

    String type;

    private CodeCountDownTimer mCountDownTimer;//获取验证码倒计时


    @Override
    public int getContentViewId() {
        return R.layout.activity_register;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        //初始化倒计时
        mCountDownTimer = new CodeCountDownTimer(this, 60 * 1000, 1000, obtainCodeBt);

        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_register);
        //toolbar的menu点击事件的监听
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        type = getIntent().getStringExtra("value");

        if (StringUtils.isSame(type, CommonUtils.isOne)) {//1是注册2是找回密码
            llReferrerPhone.setVisibility(View.GONE);
            agreeProtocolTv.setVisibility(View.VISIBLE);
            toolbar.setTitle(R.string.register);
        } else if (StringUtils.isSame(type, CommonUtils.isTwo)) {
            llReferrerPhone.setVisibility(View.GONE);
            agreeProtocolTv.setVisibility(View.GONE);
            toolbar.setTitle("找回密码");
            button.setText(R.string.sure);
        } else if (StringUtils.isSame(type, CommonUtils.isThree)) {
            llReferrerPhone.setVisibility(View.GONE);
            agreeProtocolTv.setVisibility(View.GONE);
            toolbar.setTitle("修改密码");
            button.setText(R.string.sure);
            toolbar.getMenu().clear();
            pwdEt.setHint("请输入6-18位旧密码");
            pwdAgainEt.setHint("请输入6-18位新密码");
        }
    }

    //注册或找回密码、修改密码
    private void registerOrRetrievePwd(final String phone, String code, final String pwd) {
        ScreenShotApi api = new ScreenShotApi("app/pcode");
        api.addParams("phone", phone);
        api.addParams("pcode", code);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                register(phone, pwd);
            }
        });

    }

    //注册
    private void register(final String phone, String pwd) {
        String apiString;
        if (StringUtils.isSame(type, CommonUtils.isOne)) {//1是注册2是找回密码
            apiString = "app/registered";//注册
        } else if (StringUtils.isSame(type, CommonUtils.isTwo)) {
            apiString = "app/forgot_pwd";//忘记密码
        } else {
            apiString = "app/upuserpwd";//修改密码
        }
        ScreenShotApi api = new ScreenShotApi(apiString);
        api.addParams("phone", phone);
        api.addParams("password", pwd);
        if (StringUtils.isSame(type, CommonUtils.isThree)) {
            api.addParams("newpwd", pwd);//修改密码
        }
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                if (StringUtils.isSame(type, CommonUtils.isOne)) {//1是注册2是找回密码3修改密码
                    UIHelper.ToastMessage(RegisterActivity.this, "注册成功");
                    EventBus.getDefault().post(new RegisterEvent(phone));
                } else if (StringUtils.isSame(type, CommonUtils.isTwo)) {
                    EventBus.getDefault().post(new RegisterEvent(phone));
                    UIHelper.ToastMessage(RegisterActivity.this, response.getInfo());
                } else {
                    EventBus.getDefault().post(new LogoutEvent());
                    IntentUtil.startActivity(getContext(), LoginActivity.class);
                    UIHelper.ToastMessage(RegisterActivity.this, "修改密码成功");
                }
                finish();
            }
        });
    }


    private boolean isShowPwd = false;//是否显示密码


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        onBackPressed();
        return true;
    }


    @OnClick({R.id.showPwd_iv, R.id.register_bt, R.id.obtainCode_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.showPwd_iv:
                if (!isShowPwd) {//显示密码
                    pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPwdIv.setImageResource(R.drawable.pwd_on);
                } else {//隐藏密码
                    pwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPwdIv.setImageResource(R.drawable.pwd_off);
                }
                isShowPwd = !isShowPwd;
                break;
            case R.id.register_bt://注册
                loadRegister();
                break;
            case R.id.obtainCode_bt://获取验证码
                phone = phoneEt.getText().toString();//电话号码
                if (StringUtils.isEmpty(phone)) {
                    UIHelper.ToastMessage(this, R.string.input_phone_number);
                    return;
                }
                if (!StringUtils.isMobile(phone)) {
                    Toast.makeText(this, R.string.not_mobile_format, Toast.LENGTH_SHORT).show();
                    return;
                }
                loadObtainCode(phone);
                break;
        }
    }


    private String phone;

    //获取验证码
    private void loadObtainCode(String phone) {
        HttpClient httpClient = HttpClient.newInstance(this);
        ScreenShotApi api = new ScreenShotApi("app/login");
        api.addParams("phone", phone);//send
        if (!StringUtils.isSame(type, CommonUtils.isOne)) {//(注册时不要加)
            api.addParams("send", CommonUtils.isTwo);//send 不等于空就行
        }
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                mCountDownTimer.start();
                UIHelper.ToastMessage(RegisterActivity.this, getString(R.string.send_code_succeed));
            }
        });
    }

    //注册
    private void loadRegister() {
        boolean b = StringUtils.isSame(type, CommonUtils.isThree);//修改密码
        phone = CommonUtils.getStringByEditText(phoneEt);//电话号码
        if (StringUtils.isEmpty(phone)) {
            UIHelper.ToastMessage(this, R.string.input_phone_number);
            return;
        }
        if (!StringUtils.isMobile(phone)) {
            Toast.makeText(this, R.string.not_mobile_format, Toast.LENGTH_SHORT).show();
            return;
        }
        String code = CommonUtils.getStringByEditText(codeEt);//
        if (StringUtils.isEmpty(code)) {
            UIHelper.ToastMessage(this, R.string.input_code);
            return;
        }
        String pwd = CommonUtils.getStringByEditText(pwdEt);//
        if (StringUtils.isEmpty(pwd) || pwd.length() < 6) {
            if (!b) {
                UIHelper.ToastMessage(this, R.string.input_password_count);
            } else {
                UIHelper.ToastMessage(this, "请输入6-18位旧密码");
            }
            return;
        }
        String pwdAgain = CommonUtils.getStringByEditText(pwdAgainEt);//
        if (StringUtils.isEmpty(pwdAgain)) {
            if (!b) {
                UIHelper.ToastMessage(this, R.string.input_pwd_again);
            } else {
                UIHelper.ToastMessage(this, "请输入6-18位新密码");
            }
            return;
        } else {
            if (pwdAgain.length() < 6) {
                UIHelper.ToastMessage(this, "请输入6-18位新密码");
                return;
            }
        }
        if (!b) {
            if (!StringUtils.isSame(pwd, pwdAgain)) {
                UIHelper.ToastMessage(this, R.string.password_inconformity);
                return;
            }
        }


//        String phone1 = CommonUtils.getStringByEditText(referrerPhoneEt);//推荐人电话号码

//        if (StringUtils.isSame(type, CommonUtils.isOne)) {
//
//            if (StringUtils.isEmpty(phone1)) {
//                UIHelper.ToastMessage(this, "请输入推荐人手机号码");
//                return;
//            }
//            if (!StringUtils.isMobile(phone1)) {
//                UIHelper.ToastMessage(this, "推荐人手机号码格式不正确");
//                return;
//            }
//        }

        registerOrRetrievePwd(phone, code, pwd);
    }

}
