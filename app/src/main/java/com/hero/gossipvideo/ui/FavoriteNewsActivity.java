package com.hero.gossipvideo.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hero.gossipvideo.R;
import com.hero.gossipvideo.ads.gdt.GDTAgent;
import com.hero.gossipvideo.db.NewsDao;
import com.hero.gossipvideo.db.model.News;
import com.hero.gossipvideo.store.NewsStore;
import com.hero.gossipvideo.ui.adapter.NewsAdapter;
import com.hero.gossipvideo.ui.view.DividerItemDecoration;
import com.ltc.lib.utils.Utils;

import java.util.List;

/**
 * Created by Administrator on 2015/9/12.
 */
public class FavoriteNewsActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView mTipsTv;
    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;

    public static void invoke(Context context) {
        context.startActivity(new Intent(context, FavoriteNewsActivity.class));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_favorite);

        initView();
        showAd();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {

        mToolbar = (Toolbar) findViewById(R.id.v_toolbar);
        mToolbar.setTitle(R.string.nv_favorite);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTipsTv = (TextView) findViewById(R.id.tv_tips);
        mRecyclerView = (RecyclerView) findViewById(R.id.v_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void initData() {
        NewsStore store = new NewsStore();
        List<News> histories =  store.getNewsListFromStore();
        if (Utils.isEmpty(histories)) {
            mTipsTv.setVisibility(View.VISIBLE);
        } else {
            mTipsTv.setVisibility(View.GONE);
            mNewsAdapter = new NewsAdapter(this);
            mNewsAdapter.addAll(histories);
            mRecyclerView.setAdapter(mNewsAdapter);
        }
    }

    private void showAd() {
        FrameLayout container = (FrameLayout) findViewById(R.id.v_ad);
        GDTAgent.getInstance().showBannerAD(this, container, GDTAgent.POST_ID_NEWS_DETAIL_BANNER);
    }
}
