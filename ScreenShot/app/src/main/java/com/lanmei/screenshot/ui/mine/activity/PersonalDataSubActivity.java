package com.lanmei.screenshot.ui.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lanmei.screenshot.R;
import com.lanmei.screenshot.api.ScreenShotApi;
import com.lanmei.screenshot.helper.CameraHelper;
import com.lanmei.screenshot.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.ImageUtils;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.utils.UserHelper;
import com.xson.common.widget.CircleImageView;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 个人资料
 */
public class PersonalDataSubActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.avatar_iv)
    CircleImageView mAvatarIv;//头像
    @InjectView(R.id.name_tv)
    TextView nameTv;
    @InjectView(R.id.phone_tv)
    TextView phoneTv;
    @InjectView(R.id.address_tv)
    TextView addressTv;

    @InjectView(R.id.save_bt)
    Button mSaveButton;//保存
    @InjectView(R.id.radioGroup)
    RadioGroup mRadgroup;
    @InjectView(R.id.btnMan)
    RadioButton btnMan;
    @InjectView(R.id.btnWoman)
    RadioButton btnWoman;

    String pic;//头像
    String name;//姓名
    String phone;//手机号码
    String address;//详细地址
    String sex;
    String chooseSex;
    CameraHelper cameraHelper;
    UserBean bean;


    @Override
    public int getContentViewId() {
        return R.layout.activity_personal_data_sub;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("个人资料");
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        cameraHelper = new CameraHelper(this);
        cameraHelper.setHeadUrlListener(new CameraHelper.HeadUrlListener() {
            @Override
            public void getUrl(String url) {
                if (isFinishing()){
                    return;
                }
                loadUpDate(url);
            }
        });

        bean = UserHelper.getInstance(this).getUserBean();
        if (bean != null) {
            name = bean.getNickname();
            phone = bean.getPhone();
            address = bean.getAddress();
            chooseSex = sex = bean.getSex();
            if (!StringUtils.isEmpty(sex)) {
                if (StringUtils.isSame(sex, CommonUtils.isOne)) {
                    btnMan.setChecked(true);
                } else if (StringUtils.isSame(sex, CommonUtils.isTwo)) {
                    btnWoman.setChecked(true);
                }
            }
            pic = bean.getPic();
            cameraHelper.setHeadPathStr(pic);

            nameTv.setText(name);
            phoneTv.setText(phone);
            addressTv.setText(address);
            ImageHelper.load(this, pic, mAvatarIv, null, true, R.drawable.default_pic, R.drawable.default_pic);
        }
        mSaveButton.setEnabled(false);
        mSaveButton.setBackgroundResource(R.drawable.button_unable);


        mRadgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.btnMan) {
                    sex = CommonUtils.isOne;
                } else if (checkedId == R.id.btnWoman) {
                    sex = CommonUtils.isTwo;
                }
                L.d("BeanRequest", sex + "");
                dataIsChange();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @OnClick({R.id.ll_personal_icons, R.id.ll_name, R.id.ll_address, R.id.ll_phone, R.id.save_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_personal_icons://上传头像
                cameraHelper.showDialog();
                break;
            case R.id.ll_name://姓名
                startActivityPersonal(100, CommonUtils.getStringByTextView(nameTv));
                break;
            case R.id.ll_phone://电话
//                startActivityPersonal(101, CommonUtils.getStringByTextView(phoneTv));
                break;
            case R.id.ll_address://地址
                startActivityPersonal(102, CommonUtils.getStringByTextView(addressTv));
                break;
            case R.id.save_bt://保存
                UIHelper.ToastMessage(this,R.string.developing);
//                ajaxSaveDate();
                break;
        }
    }

    private void startActivityPersonal(int type, String value) {
        IntentUtil.startActivityForResult(this, PersonalCompileSubActivity.class, value, type);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        L.d("onActivityResult", "requestCode:" + requestCode + ":resultCode:" + resultCode);
        String compile = "";
        if (data != null) {
            compile = data.getStringExtra("compile");
        }
        String image;
        switch (requestCode) {
            case CameraHelper.CHOOSE_FROM_GALLAY:
                image = ImageUtils.getImageFileFromPickerResult(this, data);
                cameraHelper.startActionCrop(image);
                break;
            case CameraHelper.CHOOSE_FROM_CAMERA:
                //注意小米拍照后data 为null
                image = cameraHelper.getTempImage().getPath();
                cameraHelper.startActionCrop(image);
                break;
            case CameraHelper.RESULT_FROM_CROP:
                cameraHelper.uploadNewPhoto(mAvatarIv);//
                dataIsChange();
                break;
            case 100:
                setTextView(nameTv, compile);
                break;
            case 101:
                setTextView(phoneTv, compile);
                break;
            case 102:
                setTextView(addressTv, compile);
                break;
            default:
                break;
        }
    }

    private void setTextView(TextView textView, String compile) {
        textView.setText(compile);
        dataIsChange();
    }



    //资料是否有改动过
    private void dataIsChange() {
        String cName = CommonUtils.getStringByTextView(nameTv);
        String cPhone = CommonUtils.getStringByTextView(phoneTv);
        String cAddress = CommonUtils.getStringByTextView(addressTv);
        if (!StringUtils.isSame(cName, name)
                || !StringUtils.isSame(cAddress, address)
                || !StringUtils.isSame(sex, chooseSex)
                || !StringUtils.isSame(cameraHelper.getHeadPathStr(), pic)
                || !StringUtils.isSame(cPhone, phone)) {
            mSaveButton.setEnabled(true);
            mSaveButton.setBackgroundResource(R.drawable.button_corners);
        } else {
            mSaveButton.setEnabled(false);
            mSaveButton.setBackgroundResource(R.drawable.button_unable);
        }
    }



    private void ajaxSaveDate() {
        if (StringUtils.isSame(cameraHelper.getHeadPathStr(), pic)) {
            loadUpDate("");
        } else {
            cameraHelper.execute();
        }
    }

    private void loadUpDate(String url) {
        final String name = CommonUtils.getStringByTextView(nameTv);
        final String phone = CommonUtils.getStringByTextView(phoneTv);//
        final String address = CommonUtils.getStringByTextView(addressTv);

        HttpClient httpClient = HttpClient.newInstance(this);
        ScreenShotApi api = new ScreenShotApi("app/upmember");
        api.addParams("id", api.getUserId(this));
        if (StringUtils.isEmpty(url)) {
            api.addParams("pic", pic);
        } else {
            api.addParams("pic", url);
        }
        api.addParams("realname", name);
        api.addParams("sex", sex);
        api.addParams("address", address);
        api.addParams("phone", phone);
        api.addParams("user_type", CommonUtils.getUserBean(this).getUser_type());
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(PersonalDataSubActivity.this, "修改资料成功");
                CommonUtils.loadUserInfo(getApplication(), null);
                finish();
            }
        });
    }

}
