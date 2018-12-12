package com.lanmei.screenshot.webviewpage;

import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xkai on 2017/6/2.
 * webview 图片预览工具
 */

public class WebViewPhotoBrowserUtil {

    /**
     * @param webView
     * @param content 内容
     */
    public static void photoBrowser(Context context, WebView webView, String content) {
        if (com.xson.common.utils.StringUtils.isEmpty(content)) {
            return;
        }
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        List<String> imageUrls = StringUtils.returnImageUrlsFromHtml(content);
        loadHtml(content, webView);
        if (!com.xson.common.utils.StringUtils.isEmpty(imageUrls)) {
//            WebSettings webSettings = webView.getSettings();
//                        webSettings.setJavaScriptEnabled(true);
//                        webSettings.setDefaultFixedFontSize(13);
//                        webSettings.setDefaultTextEncodingName("UTF-8");
//                        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//                        webSettings.setSupportZoom(true);
//                        webSettings.setSaveFormData(false);
            webView.addJavascriptInterface(new MJavascriptInterface(context, imageUrls), "imagelistener");
            webView.setWebViewClient(new MyWebViewClient());
        }
    }

    public static void loadHtml(String content, WebView webView) {
        String html = "<div style=\"width:100%\">" + content + "</div>";
        html = delHTMLTag(html);
        webView.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
    }


    public static String delHTMLTag(String htmlStr) {

        htmlStr = "<div style=\\\"word-wrap:break-word;word-break:break-all;\\\">" + htmlStr + "</div><script>var pic = document.getElementsByTagName(\\\"img\\\");for (var i = 0; i < pic.length; i++) {pic[i].style.maxWidth = \\\"100%%\\\";pic[i].style.maxHeight = \\\"100%%\\\";};</script>";

        String img = "<img[^>]+>";
        Pattern imgp = Pattern.compile(img);
        Matcher html = imgp.matcher(htmlStr);
        while (html.find()) {
            Matcher m = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(
                    html.group());
            while (m.find()) {
                String ig = "<img style=\"box-sizing: border-box; width: 100%; height: auto !important;\" src=\""
                        + m.group(1) + "\" />";
                htmlStr = htmlStr.replace(html.group(), ig);
            }
        }
        return htmlStr; // 返回文本字符串
    }

    public static void loadUrl(String url, WebView webview) {
        WebSettings webSettings = webview.getSettings();
        //加载需要显示的网页
        webview.loadUrl(url);
        //设置Web视图
        webview.setWebViewClient(new WebViewClient());
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //支持插件
        //        webSettings.setPluginsEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
    }

}
