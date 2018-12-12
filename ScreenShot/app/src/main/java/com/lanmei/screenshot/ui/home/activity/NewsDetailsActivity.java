package com.lanmei.screenshot.ui.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.lanmei.screenshot.R;
import com.lanmei.screenshot.bean.NewsListBean;
import com.lanmei.screenshot.webviewpage.WebViewPhotoBrowserUtil;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.FormatTime;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;


/**
 * 新闻详情
 */
public class NewsDetailsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.title_tv)
    TextView titleTv;
    @InjectView(R.id.time_tv)
    TextView timeTv;
    @InjectView(R.id.web_view)
    WebView webView;
    private NewsListBean.NewsBean bean;


    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            bean = (NewsListBean.NewsBean) bundle.getSerializable("bean");
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_news_details;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.news_details);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        if (bean == null){
            return;
        }
        titleTv.setText(bean.getTitle());
        FormatTime time = new FormatTime(bean.getAddtime());
        timeTv.setText(time.formatterTime());
        WebViewPhotoBrowserUtil.photoBrowser(this,webView,bean.getContent());

//        ScreenShotApi api = new ScreenShotApi("app/news_details");
//        api.addParams("id",bean.getId());
//        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
//            @Override
//            public void onResponse(BaseBean response) {
//
//            }
//        });

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
