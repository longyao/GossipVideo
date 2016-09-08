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
import com.hero.gossipvideo.ui.adapter.NewsAdapter;
import com.hero.gossipvideo.ui.view.DividerItemDecoration;
import com.ltc.lib.utils.Utils;

import java.util.List;

/**
 * Created by Administrator on 2015/9/12.
 */
public class NewsHistoryActivity extends BaseActivity {

    private TextView mTipsTv;
    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;

    public static void invoke(Context context) {
        context.startActivity(new Intent(context, NewsHistoryActivity.class));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_history);

        initView();
        showAd();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void clearHistory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("你确定要清空历史记录吗？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NewsDao.getInstance().deleteAll();
                mNewsAdapter.clean();
                mNewsAdapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    private void initView() {

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.v_toolbar);
        mToolbar.setTitle(R.string.nv_news_history);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTipsTv = (TextView) findViewById(R.id.tv_tips);
        mRecyclerView = (RecyclerView) findViewById(R.id.v_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void initData() {
        List<News> histories =  NewsDao.getInstance().getAllOrderBy("updateTime", false);
        if (Utils.isEmpty(histories)) {
            mTipsTv.setVisibility(View.VISIBLE);
        } else {
            mTipsTv.setVisibility(View.GONE);
            mNewsAdapter = new NewsAdapter(this);
            mNewsAdapter.addAll(histories);
            mRecyclerView.setAdapter(mNewsAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_his, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear_his) {
            clearHistory();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAd() {
        FrameLayout container = (FrameLayout) findViewById(R.id.v_ad);
        GDTAgent.getInstance().showBannerAD(this, container, GDTAgent.POST_ID_NEWS_DETAIL_BANNER);
    }
}
