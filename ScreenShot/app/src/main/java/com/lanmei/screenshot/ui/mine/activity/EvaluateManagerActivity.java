package com.lanmei.screenshot.ui.mine.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.lanmei.screenshot.R;
import com.lanmei.screenshot.event.ScreenShotEvent;
import com.lanmei.screenshot.utils.AKDialog;
import com.lanmei.screenshot.utils.FileUtils;
import com.lanmei.screenshot.webviewpage.WebViewPhotoBrowserUtil;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;

/**
 * 评价管理
 */
public class EvaluateManagerActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.web_view)
    WebView webView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_evaluate_manager;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.evaluate_manager);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        WebViewPhotoBrowserUtil.loadUrl("https://login.m.taobao.com/login.htm",webView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_screen_shot, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_screen_shot:
                AKDialog.getAlertDialog(this, "确定现在截图吗?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Bitmap bitmap = getViewBp(webView);
                        if (!StringUtils.isEmpty(bitmap)) {
                            FileUtils.savePhoto(getContext(), bitmap, new com.lanmei.screenshot.utils.FileUtils.SaveResultCallback() {
                                @Override
                                public void onSavedSuccess(String path) {
                                    EventBus.getDefault().post(new ScreenShotEvent(bitmap, path));
                                    L.d("AddressPicker", path);
                                    finish();
                                }

                                @Override
                                public void onSavedFailed() {

                                }
                            });
                        } else {
                            UIHelper.ToastMessage(getContext(), "截图失败");
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 普通截屏的实现
     *
     * @param v
     * @return
     */
    public Bitmap getViewBp(View v) {
        if (null == v) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(),
                    View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                    v.getHeight(), View.MeasureSpec.EXACTLY));
            v.layout((int) v.getX(), (int) v.getY(),
                    (int) v.getX() + v.getMeasuredWidth(),
                    (int) v.getY() + v.getMeasuredHeight());
        } else {
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        }
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();
        return b;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
