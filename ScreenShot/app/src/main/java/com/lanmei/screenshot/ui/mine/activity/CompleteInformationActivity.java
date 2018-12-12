package com.lanmei.screenshot.ui.mine.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.lanmei.screenshot.R;
import com.lanmei.screenshot.event.DataUploadingEvent;
import com.lanmei.screenshot.event.ScreenShotEvent;
import com.lanmei.screenshot.ui.mine.service.RecordService;
import com.lanmei.screenshot.utils.AKDialog;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 完善资料(资料填写)
 */
public class CompleteInformationActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.screen_shot_iv)
    ImageView screenShotIv;
    @InjectView(R.id.video_view)
    VideoView videoView;
    @InjectView(R.id.video_iv)
    ImageView videoIv;
    @InjectView(R.id.timer)
    Chronometer timer;
    @InjectView(R.id.record_start_tv)
    TextView recordStartTv;
    @InjectView(R.id.record_end_tv)
    TextView recordEndTv;

    private MediaProjectionManager projectionManager;
    private MediaProjection mediaProjection;
    private RecordService recordService;

    private static final int RECORD_REQUEST_CODE = 101;
    private static final int STORAGE_REQUEST_CODE = 102;
    private static final int AUDIO_REQUEST_CODE = 103;


    @Override
    public int getContentViewId() {
        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        return R.layout.activity_complete_information;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.information_fill_in);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        EventBus.getDefault().register(this);

        recordEndTv.setEnabled(false);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_REQUEST_CODE);
        }
        Intent intent = new Intent(this, RecordService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECORD_REQUEST_CODE && resultCode == RESULT_OK) {
            mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            if (mediaProjection == null) {
                return;
            }
            recordService.setVideoPath("");//先设置为空
            recordService.setMediaProject(mediaProjection);
            recordService.startRecord();


            timer.setBase(SystemClock.elapsedRealtime());//计时器清零
//            int hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60);
            timer.setFormat("录制时长: %s");
            timer.start();
            setVideoButtonViewType(false);
            Log.d("serviceConnected", "onActivityResult");

            //退到手机桌面
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
    }

    /**
     * 设置开始录屏和结束录屏按钮的状态
     *
     * @param isStart
     */
    private void setVideoButtonViewType(boolean isStart) {

        recordStartTv.setEnabled(isStart);
        recordEndTv.setEnabled(!isStart);

        recordStartTv.setBackgroundResource(isStart ? R.drawable.button_corners : R.drawable.button_unable);
        recordEndTv.setBackgroundResource(isStart ? R.drawable.button_unable : R.drawable.button_corners);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            RecordService.RecordBinder binder = (RecordService.RecordBinder) service;
            recordService = binder.getRecordService();
            recordService.setConfig(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi);
            Log.d("serviceConnected", "onServiceConnected:" + recordService);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    @OnClick({R.id.pic_method_tv, R.id.comment_interface_tv, R.id.video_method_tv, R.id.record_start_tv, R.id.record_end_tv, R.id.next_step_bt, R.id.video_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_method_tv://查看评论管理截图操作方法
                UIHelper.ToastMessage(this, R.string.developing);
                break;
            case R.id.comment_interface_tv://点击登录评价页面
                IntentUtil.startActivity(this, EvaluateManagerActivity.class);
                break;
            case R.id.video_method_tv://查看实名号视频认证操作方法
                UIHelper.ToastMessage(this, R.string.developing);
                break;
            case R.id.record_start_tv://开始录制
                startRecord();
                break;
            case R.id.record_end_tv://结束录制
                stopRecord();
                break;
            case R.id.next_step_bt://下一步
                nextStep();
                break;
            case R.id.video_view://
                playVideo();
                break;
        }
    }

    //开始录屏
    private void startRecord(){
        AKDialog.getAlertDialog(this, "确定要录屏?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent captureIntent = projectionManager.createScreenCaptureIntent();
                startActivityForResult(captureIntent, RECORD_REQUEST_CODE);
            }
        });
    }

    //下一步
    private void nextStep() {
        if (!StringUtils.isEmpty(recordService) && recordService.isRunning()) {
            UIHelper.ToastMessage(this, "请先结束录屏");
            return;
        }
        if (StringUtils.isEmpty(screenShotPath)) {
            UIHelper.ToastMessage(this, "请点击登录评价页面获取截图");
            return;
        }
        if (!StringUtils.isEmpty(recordService) && StringUtils.isEmpty(recordService.getVideoPath())) {
            UIHelper.ToastMessage(this, "请点击录屏");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("screenShotPath", screenShotPath);
        bundle.putString("videoPath", recordService.getVideoPath());
        IntentUtil.startActivity(this, UploadingIdActivity.class, bundle);
    }

    //播放录屏
    private void playVideo() {
        if (!StringUtils.isEmpty(recordService) && StringUtils.isEmpty(recordService.getVideoPath())) {
            return;
        }
        //跳转到手机本地的视频播放器
        Intent it = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(recordService.getVideoPath());
        it.setDataAndType(uri, "video/*");
        startActivity(it);
//        UIHelper.ToastMessage(this, recordService.getVideoPath());
    }

    //结束录屏
    private void stopRecord() {
        timer.stop();
        setVideoButtonViewType(true);
        recordService.stopRecord();
        screenShotIv.postDelayed(new Runnable() {
            @Override
            public void run() {
                videoView.setVideoPath(recordService.getVideoPath());
                videoView.start();
            }
        }, 500);
        videoIv.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!StringUtils.isEmpty(recordService) && recordService.isRunning()) {//正在录屏时先退出
            timer.stop();
            recordService.stopRecord();
        }
        unbindService(connection);
        EventBus.getDefault().unregister(this);
        Log.d("serviceConnected", "onDestroy:unbindService");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_REQUEST_CODE || requestCode == AUDIO_REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish();
            }
        }
        Log.d("serviceConnected", "onRequestPermissionsResult");
    }


    private String screenShotPath;//截图图片路径

    //截屏成功后调用
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void screenShotEvent(ScreenShotEvent event) {
        screenShotIv.setImageBitmap(event.getBitmap());
        screenShotPath = event.getPath();
        L.d("AddressPicker", "screenShotPath" + event.getPath());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!StringUtils.isEmpty(recordService) && !StringUtils.isEmpty(recordService.getVideoPath()) && !recordService.isRunning()) {
            videoView.stopPlayback();
            videoView.setVideoPath(recordService.getVideoPath());
            videoView.start();
        }
    }

    //上传个人信息后退出该界面
    @Subscribe
    public void dataUploadingEvent(DataUploadingEvent event) {
        finish();
    }

}
