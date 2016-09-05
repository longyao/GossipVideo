package com.hero.gossipvideo.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.hero.gossipvideo.R;
import com.hero.gossipvideo.ads.gdt.GDTAgent;
import com.hero.gossipvideo.db.NewsDao;
import com.hero.gossipvideo.db.model.News;
import com.hero.gossipvideo.store.NewsStore;
import com.ltc.lib.utils.LogUtil;
import com.ltc.lib.utils.Utils;

import org.third.widget.roundcornerprogressbar.RoundCornerProgressBar;

/**
 * Created by Administrator on 2015/4/15.
 */
public class WebViewActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private WebView mWebView;
    private RoundCornerProgressBar mProgressBar;
    private InnerWebViewClient mWebViewClient;
    private InnerWebChromeClient mWebChromeClient;

    private String mTitle;
    private String mUrl;

    public static void invoke(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static void invoke(Context context, String title, String url, boolean read) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.putExtra("read", read);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_web_viwe);

        getFromExtras();
        initResource();
        initView();
        showAd();
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.loadUrl("file:///android_asset/black.html");
        }
    }

    @Override
    public void onClick(View view) {

    }

    private void getFromExtras() {
        Intent intent = getIntent();
        if (intent != null) {
            mTitle = intent.getStringExtra("title");
            mUrl = intent.getStringExtra("url");
            final boolean read = intent.getBooleanExtra("read", false);
            if (read) {
                findViewById(R.id.v_view).setVisibility(View.VISIBLE);
            }
        }
    }

    private void initResource() {
        mWebViewClient = new InnerWebViewClient();
        mWebChromeClient = new InnerWebChromeClient();
    }

    private void initView() {

        mToolbar = (Toolbar) findViewById(R.id.v_toolbar);
        mToolbar.setTitle(mTitle);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressBar = (RoundCornerProgressBar) findViewById(R.id.v_progress_bar);
        mWebView = (WebView) findViewById(R.id.v_web_view);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setWebChromeClient(mWebChromeClient);

        setWebViewSettings(mWebView);

        if (!Utils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);
            LogUtil.d(mUrl);
        }
    }

    public void setWebViewSettings(WebView webView) {

        webView.setFocusableInTouchMode(true);
        webView.setFocusable(true);
        webView.setAnimationCacheEnabled(false);
        webView.setDrawingCacheBackgroundColor(0x00000000);
        webView.setDrawingCacheEnabled(true);
        webView.setBackgroundColor(getResources().getColor(android.R.color.white));//背景颜色为白色
        //去掉背景图片
//        webView.setBackground(null);
        webView.setWillNotCacheDrawing(false);//使用缓存。
        webView.setAlwaysDrawnWithCacheEnabled(false);//如果子视图有cache，才使用cache绘制
        webView.setSaveEnabled(true);//设置onSaveInstanceState方法是否被调用

        WebSettings settings = webView.getSettings();
        //设置javaScript相关
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);

        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
    }

    private void goBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            goBack();
            return true;
        }
        return true;
    }

    private class InnerWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private class InnerWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mProgressBar.setProgress(newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            mToolbar.setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    private void showAd() {
        FrameLayout container = (FrameLayout) findViewById(R.id.v_ad);
        GDTAgent.getInstance().showBannerAD(this, container, GDTAgent.POST_ID_DISCOVER_BANNER);
    }
}
