package com.hero.gossipvideo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hero.gossipvideo.R;
import com.hero.gossipvideo.ads.gdt.GDTAgent;
import com.hero.gossipvideo.db.DownloadVideoDao;
import com.hero.gossipvideo.db.model.DownloadVideo;
import com.hero.gossipvideo.download.VideoDownloader;
import com.hero.gossipvideo.ui.adapter.DownloadVideoAdapter;
import com.ltc.lib.utils.Utils;

import java.util.List;

/**
 * Created by Administrator on 2016/1/2.
 */
public class VideoDownloadActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView mTipsTv;
    private RecyclerView mRecyclerView;
    private DownloadVideoAdapter mVideoAdapter;

    public static void invoke(Context context) {
        context.startActivity(new Intent(context, VideoDownloadActivity.class));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_download_videos);

        initView();
        showAd();
    }

    public void onResume() {
        super.onResume();
        initData();
        VideoDownloader.getInstance().registerDownloadVideoAdapter(mVideoAdapter);
    }

    public void onPause() {
        super.onPause();
        VideoDownloader.getInstance().unregisterDownloadVideoAdapter();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.v_toolbar);
        mToolbar.setTitle(R.string.nv_download);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTipsTv = (TextView) findViewById(R.id.tv_tips);
        mRecyclerView = (RecyclerView) findViewById(R.id.v_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mVideoAdapter = new DownloadVideoAdapter(this));
    }

    private void initData() {
        List<DownloadVideo> ds =  DownloadVideoDao.getInstance().getAllOrderBy("updateTime", false);
        if (Utils.isEmpty(ds)) {
            mTipsTv.setVisibility(View.VISIBLE);
        } else {
            mTipsTv.setVisibility(View.GONE);
            mVideoAdapter.setList(ds);
            mVideoAdapter.notifyDataSetChanged();
        }
    }

    private void showAd() {
        FrameLayout container = (FrameLayout) findViewById(R.id.v_ad);
        GDTAgent.getInstance().showBannerAD(this, container, GDTAgent.POST_ID_NEWS_DETAIL_BANNER);
    }

}
