package com.lanmei.screenshot.ui.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lanmei.screenshot.R;
import com.lanmei.screenshot.event.LogoutEvent;
import com.lanmei.screenshot.event.SetUserEvent;
import com.lanmei.screenshot.ui.login.RegisterActivity;
import com.lanmei.screenshot.ui.mine.activity.AccountDetailsActivity;
import com.lanmei.screenshot.ui.mine.activity.CompleteInformationActivity;
import com.lanmei.screenshot.utils.AKDialog;
import com.lanmei.screenshot.utils.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.utils.UserHelper;
import com.xson.common.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by xkai on 2018/7/13.
 * 我的
 */

public class mineFragment extends BaseFragment {

    @InjectView(R.id.pic_iv)
    CircleImageView picIv;
    @InjectView(R.id.name_tv)
    TextView nameTv;
    @InjectView(R.id.data_tv)
    TextView dataTv;//完善资料
    @InjectView(R.id.balance_tv)
    TextView balanceTv;//余额

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setUser();
    }


    @OnClick({R.id.pic_iv, R.id.in_data_iv, R.id.ll_account, R.id.ll_identity, R.id.ll_amend_pwd,R.id.ll_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_iv:
            case R.id.in_data_iv:
//                IntentUtil.startActivity(context, PersonalDataSubActivity.class);
                break;
            case R.id.ll_account://账号余额
                IntentUtil.startActivity(context, AccountDetailsActivity.class);//CompleteInformationActivity
                break;
            case R.id.ll_identity://待完善资料
                UserBean userBean = CommonUtils.getUserBean(context);
                if (StringUtils.isEmpty(userBean)) {
                    return;
                }
                String perfect = userBean.getPerfect();//dataTv
                if (StringUtils.isEmpty(perfect)) {
                    IntentUtil.startActivity(context, CompleteInformationActivity.class);//
                    return;
                }
                switch (perfect) {
                    case "1":
                        UIHelper.ToastMessage(context, getString(R.string.audit_wait));
                        break;
                    case "0":
                    case "2":
                        IntentUtil.startActivity(context, CompleteInformationActivity.class);//
                        break;
                }
//                IntentUtil.startActivity(context, CompleteInformationActivity.class);//
                break;
            case R.id.ll_amend_pwd://修改密码
                IntentUtil.startActivity(context, RegisterActivity.class,CommonUtils.isThree);
                break;
            case R.id.ll_logout://退出登录
                AKDialog.getAlertDialog(context, "确定要退出登录?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserHelper.getInstance(getContext()).cleanLogin();
                        EventBus.getDefault().post(new LogoutEvent());//退出登录
                    }
                });
                break;
        }
    }

    @Subscribe
    public void setUserEvent(SetUserEvent event) {
        setUser();
    }


    private void setUser() {
        UserBean userBean = CommonUtils.getUserBean(context);
        if (StringUtils.isEmpty(userBean)) {
            return;
        }
        ImageHelper.load(context, userBean.getPic(), picIv, null, true, R.drawable.default_pic, R.drawable.default_pic);
        nameTv.setText(userBean.getNickname());
        balanceTv.setText(String.format(context.getString(R.string.money), userBean.getBalance()));
//        dataTv.setVisibility(View.VISIBLE);
        String perfect = userBean.getPerfect();//dataTv
        L.d("CompressPhotoUtils", "perfect:" + perfect);
        if (StringUtils.isEmpty(perfect)) {
            return;
        }
        switch (perfect) {
            case "0":
                dataTv.setText(R.string.perfect_data_wait);//资料待完善
                break;
            case "1":
                dataTv.setText(R.string.audit_wait);//
                break;
            case "2":
                dataTv.setText(R.string.not_approved);//
                break;
            case "3":
                dataTv.setText(R.string.get_approved);//
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
