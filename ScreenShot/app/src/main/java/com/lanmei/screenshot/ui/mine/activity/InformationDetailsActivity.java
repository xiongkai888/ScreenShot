package com.lanmei.screenshot.ui.mine.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lanmei.screenshot.R;
import com.lanmei.screenshot.api.ScreenShotApi;
import com.lanmei.screenshot.bean.BankListBean;
import com.lanmei.screenshot.event.DataUploadingEvent;
import com.lanmei.screenshot.helper.CameraHelper;
import com.lanmei.screenshot.utils.AKDialog;
import com.lanmei.screenshot.utils.AssetsUtils;
import com.lanmei.screenshot.utils.CommonUtils;
import com.lanmei.screenshot.utils.CompressPhotoUtils;
import com.lanmei.screenshot.utils.UpdateVideoTask;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.FormatTime;
import com.xson.common.utils.ImageUtils;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DrawClickableEditText;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * 资料填写（个人信息填写）
 */
public class InformationDetailsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.date_birth_tv)
    TextView birthdayTv;
    @InjectView(R.id.address_tv)
    TextView addressTv;
    @InjectView(R.id.pic_iv)
    ImageView picIv;

    DatePicker picker;
    AddressPicker addressPicker;
    AddressAsyncTask addressAsyncTask;

    CameraHelper cameraHelper;

    @InjectView(R.id.radioGroup)
    RadioGroup mRadgroup;

    String sex;

    @InjectView(R.id.name_tv)
    DrawClickableEditText nameTv;//姓名
    //    @InjectView(R.id.profession_et)
//    DrawClickableEditText professionEt;//职业
    @InjectView(R.id.spinner)
    Spinner spinner;//职业
    @InjectView(R.id.phone_tv)
    TextView phoneTv;//手机号
    @InjectView(R.id.weixin_et)
    DrawClickableEditText weixinEt;//微信号
    @InjectView(R.id.qq_et)
    DrawClickableEditText qqEt;//QQ号
    @InjectView(R.id.bank_num_et)
    DrawClickableEditText bankNumEt;//银行卡号
    @InjectView(R.id.bank_name_tv)
    TextView bankNameTv;//银行卡名称
    @InjectView(R.id.other_weixin_et)
    DrawClickableEditText otherWeixinEt;//介绍人微信
    @InjectView(R.id.details_address_et)
    DrawClickableEditText detailsAddressEt;//详细地址


    private String frontPathStr;//身份证正面
    private String reversePathStr;//身份证反面
    private String bankPathStr;//银行卡图片
    private String bankName;//银行名称
    private String screenShotPath;//截屏地址
    private String videoPath;//录屏地址

    private String area;//省市区

    boolean isCompress1,isCompress2,isCompress3,isCompress4,isCompress5,isUploading = false;//是否在压缩图片

    List<String> successPath1;  // 存储上传阿里云成功后的上传路径(身份证正面)
    List<String> successPath2;  // 存储上传阿里云成功后的上传路径(身份证反面)
    List<String> successPath3;  // 存储上传阿里云成功后的上传路径(银行卡)
    List<String> successPath4;  // 存储上传阿里云成功后的上传路径(评论管理截图)
    List<String> successPath5;  // 存储上传阿里云成功后的上传路径(微信收款码)
    private String videoPathOSS;//存储上传阿里云成功后的上传路径(录屏)


    private void submit() {
        final String name = CommonUtils.getStringByEditText(nameTv);
        if (StringUtils.isEmpty(name)) {
            UIHelper.ToastMessage(this, getString(R.string.input_name));
            return;
        }
        if (StringUtils.isEmpty(birth)) {
            UIHelper.ToastMessage(this, getString(R.string.choose_birthday));
            return;
        }
        if (StringUtils.isEmpty(sex)) {
            UIHelper.ToastMessage(this, getString(R.string.choose_sex));
            return;
        }
        if (StringUtils.isEmpty(professionId)) {//
            UIHelper.ToastMessage(this, getString(R.string.input_profession));
            loadProfession();
            return;
        }
        final String weixin = CommonUtils.getStringByEditText(weixinEt);
        if (StringUtils.isEmpty(weixin)) {//
            UIHelper.ToastMessage(this, getString(R.string.input_weixin));
            return;
        }
        final String qq = CommonUtils.getStringByEditText(qqEt);
        if (StringUtils.isEmpty(qq)) {//
            UIHelper.ToastMessage(this, getString(R.string.input_qq));
            return;
        }
        final String bankNum = CommonUtils.getStringByEditText(bankNumEt);
        if (StringUtils.isEmpty(bankNum)) {//
            UIHelper.ToastMessage(this, getString(R.string.input_bank_num));
            return;
        }

        if (!CommonUtils.checkBankCard(bankNum)) {
            UIHelper.ToastMessage(this, "卡号不正确");
            return;
        }

        final String otherWeixin = CommonUtils.getStringByEditText(otherWeixinEt);
        if (StringUtils.isEmpty(otherWeixin)) {//
            UIHelper.ToastMessage(this, getString(R.string.input_other_weixin));
            return;
        }
        if (StringUtils.isEmpty(area)) {//
            UIHelper.ToastMessage(this, getString(R.string.choose_address_click));
            return;
        }
        final String detailsAddress = CommonUtils.getStringByEditText(detailsAddressEt);
        if (StringUtils.isEmpty(detailsAddress)) {//
            UIHelper.ToastMessage(this, getString(R.string.input_details_address));
            return;
        }
        if (StringUtils.isEmpty(cameraHelper.getHeadPathStr())) {//
            UIHelper.ToastMessage(this, "请选择微信收款码");
            return;
        }

        AKDialog.getAlertDialog(getContext(), "确定要提交吗?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submit(name, qq, detailsAddress, weixin, otherWeixin, bankNum);
            }
        });

    }

    private void loadSubmit(String name, String qq, String detailsAddress, String weixin, String otherWeixin, String bankNum) {
        if (!isCompress1 && !isCompress2 && !isCompress3 && !isCompress4 && !isCompress5 && !isUploading) {
            ScreenShotApi api = new ScreenShotApi("app/upmember");
            api.addParams("id", api.getUserId(this));
            api.addParams("realname", name);
            api.addParams("user_type", 0);
            api.addParams("QQ", qq);
            api.addParams("sex", sex);//性别 1|2=>男|女
            api.addParams("area", area);
            api.addParams("address", detailsAddress);
            api.addParams("weixin", weixin);
            api.addParams("introducer_weixin", otherWeixin);
            api.addParams("bank", bankNum);
            api.addParams("bank_type", bankName);
            api.addParams("birth", birth);

            api.addParams("idcon", StringUtils.isEmpty(successPath1) ? "" : successPath1.get(0));//身份证正面
            api.addParams("idface", StringUtils.isEmpty(successPath2) ? "" : successPath2.get(0));//身份证反面
            api.addParams("bankimg", StringUtils.isEmpty(successPath3) ? "" : successPath3.get(0));//银行卡图片
            api.addParams("evaluate", StringUtils.isEmpty(successPath4) ? "" : successPath4.get(0));//评论管理截图
            api.addParams("weixin_code", StringUtils.isEmpty(successPath5) ? "" : successPath5.get(0));//微信是收款码
            api.addParams("video", videoPathOSS);//视频地址


            HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
                @Override
                public void onResponse(BaseBean response) {
                    if (isFinishing()) {
                        return;
                    }
                    CommonUtils.loadUserInfo(getApplication(), null);
                    EventBus.getDefault().post(new DataUploadingEvent());//退出填写资料界面
                    finish();
                }
            });
        }
    }

    //确定提交
    private void submit(final String name, final String qq, final String detailsAddress, final String weixin, final String otherWeixin, final String bankNum) {
        if (!StringUtils.isEmpty(frontPathStr)) {
            isCompress1 = true;//在压缩
            successPath1 = new ArrayList<>();
            new CompressPhotoUtils().CompressPhoto(this, frontPathStr, new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
                @Override
                public void success(List<String> list) {
                    if (isFinishing()) {
                        return;
                    }
                    successPath1 = list;
                    isCompress1 = false;
                    loadSubmit(name, qq, detailsAddress, weixin, otherWeixin, bankNum);
                }
            }, "1");
        }
        if (!StringUtils.isEmpty(reversePathStr)) {
            isCompress2 = true;//在压缩
            successPath2 = new ArrayList<>();
            new CompressPhotoUtils().CompressPhoto(this, reversePathStr, new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
                @Override
                public void success(List<String> list) {
                    if (isFinishing()) {
                        return;
                    }
                    successPath2 = list;
                    isCompress2 = false;
                    loadSubmit(name, qq, detailsAddress, weixin, otherWeixin, bankNum);
                }
            }, "2");
        }
        if (!StringUtils.isEmpty(bankPathStr)) {
            isCompress3 = true;//在压缩
            successPath3 = new ArrayList<>();
            new CompressPhotoUtils().CompressPhoto(this, bankPathStr, new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
                @Override
                public void success(List<String> list) {
                    if (isFinishing()) {
                        return;
                    }
                    successPath3 = list;
                    isCompress3 = false;
                    loadSubmit(name, qq, detailsAddress, weixin, otherWeixin, bankNum);
                }
            }, "3");
        }
        if (!StringUtils.isEmpty(screenShotPath)) {
            isCompress4 = true;//在压缩
            successPath4 = new ArrayList<>();
            new CompressPhotoUtils().CompressPhoto(this, screenShotPath, new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
                @Override
                public void success(List<String> list) {
                    if (isFinishing()) {
                        return;
                    }
                    successPath4 = list;
                    isCompress4 = false;
                    loadSubmit(name, qq, detailsAddress, weixin, otherWeixin, bankNum);
                }
            }, "4");
        }
        if (!StringUtils.isEmpty(cameraHelper.getHeadPathStr())) {
            isCompress5 = true;//在压缩
            successPath5 = new ArrayList<>();
            new CompressPhotoUtils().CompressPhoto(this, cameraHelper.getHeadPathStr(), new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
                @Override
                public void success(List<String> list) {
                    if (isFinishing()) {
                        return;
                    }
                    successPath5 = list;
                    isCompress5 = false;
                    loadSubmit(name, qq, detailsAddress, weixin, otherWeixin, bankNum);
                }
            }, "5");
        }
        if (!StringUtils.isEmpty(videoPath)) {
            isUploading = true;
            videoPathOSS = "";
            new UpdateVideoTask(this, videoPath, new UpdateVideoTask.UploadingVideoCallBack() {
                @Override
                public void success(String path) {
                    if (isFinishing()) {
                        return;
                    }
                    videoPathOSS = path;
                    isUploading = false;
                    loadSubmit(name, qq, detailsAddress, weixin, otherWeixin, bankNum);
                }
            }).execute();
        }
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            frontPathStr = bundle.getString("frontPathStr");
            reversePathStr = bundle.getString("reversePathStr");
            bankPathStr = bundle.getString("bankPathStr");
            bankName = bundle.getString("bankName");
            screenShotPath = bundle.getString("screenShotPath");
            videoPath = bundle.getString("videoPath");
        }
        L.d("AddressPicker", frontPathStr);
        L.d("AddressPicker", reversePathStr);
        L.d("AddressPicker", bankPathStr);
        L.d("AddressPicker", bankName);
        L.d("AddressPicker", screenShotPath);
        L.d("AddressPicker", videoPath);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_information_details;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.information_fill_in);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        phoneTv.setText(CommonUtils.getUserBean(this).getPhone());
        bankNameTv.setText(bankName);

        initDatePicker();//初始化生日DatePicker

        addressAsyncTask = new AddressAsyncTask();//异步获取省市区列表
        addressAsyncTask.execute();

        mRadgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {//性别选择
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.btnMan) {
                    sex = CommonUtils.isOne;
                } else if (checkedId == R.id.btnWoman) {
                    sex = CommonUtils.isTwo;
                }
            }
        });

        cameraHelper = new CameraHelper(this);
        loadProfession();//职业
    }


    private void loadProfession() {
        ScreenShotApi api = new ScreenShotApi("app/getuser_type");
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<BankListBean<String>>() {
            @Override
            public void onResponse(BankListBean<String> response) {
                if (isFinishing()) {
                    return;
                }
                initSpinner(response.getData());
            }
        });
    }


    private List<BankListBean.BankBean> professionList;
    private String professionId;//职业ID

    private void initSpinner(List<BankListBean.BankBean> dataList) {
        if (StringUtils.isEmpty(dataList)) {
            return;
        }
        professionList = dataList;

        List<String> list = new ArrayList<>();
        int size = dataList.size();
        for (int i = 0; i < size; i++) {
            list.add(dataList.get(i).getName());
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!StringUtils.isEmpty(professionList)) {
                    professionId = professionList.get(position).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 一步消息获取地址 信息
     */
    private class AddressAsyncTask extends AsyncTask<String, Integer, ArrayList<Province>> {

        public AddressAsyncTask() {

        }

        @Override
        protected ArrayList<Province> doInBackground(String... params) {
            ArrayList<Province> data = new ArrayList<Province>();
            String json = AssetsUtils.getStringFromAssert(getApplicationContext(), "city.json");
            data.addAll(JSON.parseArray(json, Province.class));
            return data;
        }

        protected void onPostExecute(ArrayList<Province> result) {
            initAddressPicker(result);
        }
    }

    private void initAddressPicker(ArrayList<Province> data) {
        addressPicker = new AddressPicker(this, data);
        addressPicker.setSelectedItem("广东", "广州", "天河");
        addressPicker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
            @Override
            public void onAddressPicked(Province province, City city, County county) {
                area = province.getAreaId() + "," + city.getAreaId() + "," + county.getAreaId();
                addressTv.setText(province.getAreaName() + "  " + city.getAreaName() + "  " + county.getAreaName());
                L.d("AddressPicker", province.getAreaId() + "," + city.getAreaId() + "," + county.getAreaId());
            }
        });
    }


    private String birth;//生日时间戳


    private void initDatePicker() {
        picker = new DatePicker(this);
        FormatTime time = new FormatTime();
        int year = time.getYear();
        int month = time.getMonth();
        int day = time.getDay();
        picker.setRangeStart(1900, 1, 1);
        picker.setRangeEnd(year, month, day);
        picker.setSelectedItem(year - 20, month, day);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                String birthStr = year + "-" + month + "-" + day;
                FormatTime formatTime = new FormatTime();
                try {
                    birth = formatTime.dateToStamp(birthStr);
                    birthdayTv.setText(year + "/" + month + "/" + day);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.date_birth_tv, R.id.address_tv, R.id.pic_iv, R.id.submit_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.date_birth_tv://出生日期
                if (picker != null) {
                    picker.show();
                }
                break;
            case R.id.address_tv://地址（省市区）
                if (addressPicker != null) {
                    addressPicker.show();
                }
                break;
            case R.id.pic_iv:
                cameraHelper.showDialog();
                break;
            case R.id.submit_bt:
                submit();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
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
                cameraHelper.uploadNewPhoto(picIv);//
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        addressAsyncTask.cancel(false);
    }
}
