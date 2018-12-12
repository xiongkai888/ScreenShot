package com.lanmei.screenshot.ui.mine.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.lanmei.screenshot.R;
import com.lanmei.screenshot.api.ScreenShotApi;
import com.lanmei.screenshot.bean.BankListBean;
import com.lanmei.screenshot.certificate.CameraActivity;
import com.lanmei.screenshot.event.DataUploadingEvent;
import com.lanmei.screenshot.utils.AKDialog;
import com.xson.common.app.BaseActivity;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.ImageUtils;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 上传身份证（资料填写）
 */
public class UploadingIdActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.front_iv)
    ImageView frontIv;
    @InjectView(R.id.reverse_iv)
    ImageView reverseIv;
    @InjectView(R.id.bank_ka_iv)
    ImageView bankKaIv;//银行卡
    @InjectView(R.id.spinner)
    Spinner spinner;//
    int type;

    @Override
    public int getContentViewId() {
        return R.layout.activity_uploading_id;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.information_fill_in);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        loadBank();//银行卡列表

        EventBus.getDefault().register(this);
    }

    private void loadBank() {
        ScreenShotApi api = new ScreenShotApi("app/getbank_type");
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

    private String bankName;

    private void initSpinner(List<BankListBean.BankBean> dataList) {
        if (StringUtils.isEmpty(dataList)) {
            return;
        }
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
                bankName = (String) parent.getItemAtPosition(position);
                L.d(L.TAG,"bankName:"+bankName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(adapter);
    }

    boolean isCamera = false;

    @OnClick({R.id.front_iv, R.id.reverse_iv, R.id.bank_ka_iv, R.id.next_step_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.front_iv://身份证正面
                dialog(CameraActivity.TYPE_IDCARD_FRONT);
                break;
            case R.id.reverse_iv://身份证反面
                dialog(CameraActivity.TYPE_IDCARD_BACK);
                break;
            case R.id.bank_ka_iv://银行卡
                dialog(CameraActivity.TYPE_COMPANY_LANDSCAPE);
                break;
            case R.id.next_step_bt://下一步
                uploading();
                break;
        }
    }

    private void uploading() {
        if (StringUtils.isEmpty(frontPathStr)) {
            UIHelper.ToastMessage(this, getString(R.string.choose_id_front));
            return;
        }
        if (StringUtils.isEmpty(reversePathStr)) {
            UIHelper.ToastMessage(this, getString(R.string.choose_id_reverse));
            return;
        }
        if (StringUtils.isEmpty(bankPathStr)) {
            UIHelper.ToastMessage(this, getString(R.string.choose_bank_ka));
            return;
        }
        if (StringUtils.isEmpty(bankName)) {
            UIHelper.ToastMessage(this, "请选择银行名称");
            loadBank();
            return;
        }
        Bundle bundle = getIntent().getBundleExtra("bundle");
        bundle.putString("frontPathStr", frontPathStr);
        bundle.putString("reversePathStr", reversePathStr);
        bundle.putString("bankPathStr", bankPathStr);
        bundle.putString("bankName", bankName);
        IntentUtil.startActivity(this, InformationDetailsActivity.class, bundle);
    }

    private void dialog(int type) {
        this.type = type;
        AKDialog.showBottomListDialog(this, this, new AKDialog.AlbumDialogListener() {
            @Override
            public void photograph() {
                isCamera = true;
                applyWritePermission();

            }

            @Override
            public void photoAlbum() {
                isCamera = false;
                applyWritePermission();

            }
        });
    }

    public void applyWritePermission() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (isCamera) {
            permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
        if (Build.VERSION.SDK_INT >= 23) {
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check == PackageManager.PERMISSION_GRANTED) {
                //调用相机
                if (isCamera) {
                    CameraActivity.openCertificateCamera(this, type);
                } else {
                    startImagePick();
                }

            } else {
                requestPermissions(permissions, 1);
            }
        } else {
            if (isCamera) {
                CameraActivity.openCertificateCamera(this, type);
            } else {
                startImagePick();
            }
        }
    }

    private void startImagePick() {
        Intent intent = ImageUtils.getImagePickerIntent();
        startActivityForResult(intent, CHOOSE_FROM_GALLAY);
    }

    private static final int CHOOSE_FROM_GALLAY = 1;
    private static final int RESULT_FROM_CROP = 3;

    private File croppedImage;

    //解决Android 7.0之后的Uri安全问题
    private Uri getUriForFile(File file) {
        if (file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, "com.lanmei.screenshot.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        String image;
        switch (requestCode) {
            case CHOOSE_FROM_GALLAY:
                image = ImageUtils.getImageFileFromPickerResult(this, data);
                startActionCrop(image);
                break;
//            case CHOOSE_FROM_CAMERA:
//                //注意小米拍照后data 为null
//                image = tempImage.getPath();
//                L.d("cameraPreview", image);
//                startActionCrop(image);
//                break;
            case RESULT_FROM_CROP:
                showNewPhoto();//
                break;
            case CameraActivity.REQUEST_CODE:
                String path = data.getStringExtra("result");
                if (!TextUtils.isEmpty(path)) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    if (bitmap == null) {
                        return;
                    }
                    if (type == CameraActivity.TYPE_IDCARD_FRONT) {//正面
                        frontPathStr = path;
                        frontIv.setImageBitmap(bitmap);
                    } else if (type == CameraActivity.TYPE_IDCARD_BACK) {//反面
                        reverseIv.setImageBitmap(bitmap);
                        reversePathStr = path;
                    } else {
                        bankKaIv.setImageBitmap(bitmap);
                        bankPathStr = path;
                    }
                }
                break;
            default:
                break;
        }
    }

    String frontPathStr;//选择正面剪切后的路径()
    String reversePathStr;//选择反面剪切后的路径()
    String bankPathStr;//选择银行卡剪切后的路径()

    private void showNewPhoto() {
        if (!StringUtils.isEmpty(croppedImage) && croppedImage.exists()) {
            try {
                ImageUtils.compressImage(croppedImage.getPath(), 300, 300, Bitmap.CompressFormat.JPEG);
            } catch (Exception e) {
                L.e(e);
            }
        }
        if (croppedImage != null) {
            switch (type) {
                case CameraActivity.TYPE_IDCARD_FRONT:
                    frontPathStr = setImageViewType(croppedImage.getAbsolutePath(), frontIv);
                    break;
                case CameraActivity.TYPE_IDCARD_BACK:
                    reversePathStr = setImageViewType(croppedImage.getAbsolutePath(), reverseIv);
                    break;
                case CameraActivity.TYPE_COMPANY_LANDSCAPE:
                    bankPathStr = setImageViewType(croppedImage.getAbsolutePath(), bankKaIv);
                    break;
            }

        }
    }

    private String setImageViewType(String path, ImageView iv) {
        Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(path, iv.getWidth(), iv.getHeight());
        iv.setImageBitmap(bitmap);
        return path;
    }

    private void startActionCrop(String image) {
        if (TextUtils.isEmpty(image)) {
            UIHelper.ToastMessage(this, R.string.image_not_exists);
            return;
        }
        File imageFile = new File(image);
        if (!imageFile.exists()) {
            UIHelper.ToastMessage(this, R.string.image_not_exists);
            return;
        }
        croppedImage = ImageUtils.getTempFile(this, "head");
        if (croppedImage == null) {
            UIHelper.ToastMessage(this, R.string.cannot_create_temp_file);
            return;
        }
        Intent intent = ImageUtils.getImageCropIntent(getUriForFile(imageFile), Uri.fromFile(croppedImage));
        startActivityForResult(intent, RESULT_FROM_CROP);
    }

    //上传个人信息后退出该界面
    @Subscribe
    public void dataUploadingEvent(DataUploadingEvent event){
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
