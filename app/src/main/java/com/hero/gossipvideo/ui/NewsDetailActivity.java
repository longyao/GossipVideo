package com.hero.gossipvideo.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hero.gossipvideo.R;
import com.hero.gossipvideo.ads.gdt.GDTAgent;
import com.hero.gossipvideo.api.ApiManager;
import com.hero.gossipvideo.content.NewsDetailContent;
import com.hero.gossipvideo.db.NewsDao;
import com.hero.gossipvideo.db.model.News;
import com.hero.gossipvideo.store.NewsStore;
import com.ltc.lib.net.api.HttpResult;
import com.ltc.lib.utils.JsonUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by longtc on 16-3-11.
 */
public class NewsDetailActivity extends BaseActivity {

    private WebView mNewsWebView;

    private News mNews;
    private NewsStore mNewsStore;

    public static void invoke(Context context, News news) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("news", news);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_news_detail);

        mNewsStore = new NewsStore();
        getFromExtras();
        initView();
        reqNewsDetail();
        showAd();
    }

    private void initView() {

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mNews.title);

        final SimpleDraweeView backdrop = (SimpleDraweeView) findViewById(R.id.backdrop);
        backdrop.setImageURI(Uri.parse(mNews.picSmall));

        mNewsWebView = (WebView) findViewById(R.id.v_web_view);
        mNewsWebView.setHorizontalScrollBarEnabled(false);
        mNewsWebView.setVerticalScrollBarEnabled(false);
        mNewsWebView.getSettings().setJavaScriptEnabled(true);
        mNewsWebView.getSettings().setSupportZoom(false);
    }

    private void getFromExtras() {
        final Intent intent = getIntent();
        if (intent != null) {
            mNews = (News) intent.getSerializableExtra("news");
            mNews.updateTime = System.currentTimeMillis();
            addRecord(mNews);
        }
    }

    private void reqNewsDetail() {
        new ReqNewsDetailTask().execute();
    }

    private void addRecord(final News news) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NewsDao.getInstance().saveOrUpdateNews(news);
            }
        }).start();
    }

    private class ReqNewsDetailTask extends AsyncTask<Void, Void, HttpResult> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HttpResult doInBackground(Void... params) {
            return ApiManager.getNewsDetailContent(mNews.unique);
        }

        @Override
        protected void onPostExecute(HttpResult rs) {
            if (rs != null && rs.data != null) {
                NewsDetailContent nd = JsonUtil.fromJson(rs.data, NewsDetailContent.class);
                if (nd != null && nd.data != null) {
                    mNewsWebView.loadData(nd.data.getToProcessContents(), "text/html; charset=UTF-8", null);
                }
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.news_favorite);
        if (mNews != null && mNewsStore.isNewsExistsStore(mNews)) {
            item.setIcon(R.drawable.ic_favorited);
        } else {
            item.setIcon(R.drawable.ic_favorite);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_news_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.news_favorite) {
            if (mNews != null && mNewsStore.isNewsExistsStore(mNews)) {
                item.setIcon(R.drawable.ic_favorite);
                mNewsStore.deleteFavoriteNews(mNews);
            } else {
                item.setIcon(R.drawable.ic_favorited);
                mNewsStore.addFavoriteNews(mNews);
            }
        }
        return true;
    }

    private void showAd() {
        final FrameLayout container = (FrameLayout) findViewById(R.id.v_ad);
        GDTAgent.getInstance().showBannerAD(this, container, GDTAgent.POST_ID_NEWS_DETAIL_BANNER);
    }

    private void writeToSDCard(File file, String content) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            fw.write(content);
            fw.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
