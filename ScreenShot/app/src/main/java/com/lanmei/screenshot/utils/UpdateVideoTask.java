package com.lanmei.screenshot.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.ProgressHUD;

import oss.ManageOssUpload;

/**
 * Created by xkai on 2018/7/20.
 */

public class UpdateVideoTask extends AsyncTask<Void, Integer, String> {

    ProgressHUD mProgressHUD;
    String path;
    Context context;
    UploadingVideoCallBack callBack;

    public UpdateVideoTask(Context context, String path, UploadingVideoCallBack callBack) {
        this.path = path;
        this.context = context;
        this.callBack = callBack;
        mProgressHUD = ProgressHUD.show(context, "录屏上传中...", true, false, null);
    }

    /**
     * 运行在UI线程中，在调用doInBackground()之前执行
     */
    @Override
    protected void onPreExecute() {
    }

    /**
     * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
     */
    @Override
    protected String doInBackground(Void... params) {
        if (StringUtils.isEmpty(path)) {
            L.d("CompressPhotoUtils", "视频地址为空");
            return null;
        }
        ManageOssUpload manageOssUpload = new ManageOssUpload(context);//图片(视频)上传类
        String urlPic = manageOssUpload.uploadFile_img(path, "video");
        if (StringUtils.isEmpty(urlPic)) {
            //写上传失败逻辑
            Message msg = mHandler.obtainMessage();
            msg.what = 1;
            msg.obj = path;
            mHandler.sendMessage(msg);
        } else {
            L.d("CompressPhotoUtils", "上传成功返回的视频地址:" + urlPic);
        }
        return urlPic;
    }

    /**
     * 运行在ui线程中，在doInBackground()执行完毕后执行
     */
    @Override
    protected void onPostExecute(String result) {
        //            mProgressDialog.cancel();
        mProgressHUD.cancel();
        mProgressHUD = null;
        if (StringUtils.isEmpty(result)) {
            L.d("CompressPhotoUtils", "isEmpty");
            return;
        }
        if (callBack != null){
            callBack.success(result);
        }
    }

    /**
     * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
     */
    @Override
    protected void onProgressUpdate(Integer... values) {

    }



    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://
                    UIHelper.ToastMessage(context, "上传视频失败");
                    L.d("CompressPhotoUtils", "上传视频失败");
                    break;
            }
        }
    };

     public interface UploadingVideoCallBack {
        void success(String path);
    }

}
