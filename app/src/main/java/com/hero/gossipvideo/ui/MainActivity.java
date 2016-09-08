package com.hero.gossipvideo.ui;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hero.gossipvideo.MainApplication;
import com.hero.gossipvideo.R;
import com.hero.gossipvideo.ads.gdt.GDTAgent;
import com.hero.gossipvideo.settings.Settings;
import com.hero.gossipvideo.ui.fragment.DiscoverFragment;
import com.hero.gossipvideo.ui.fragment.NewsFragment;
import com.hero.gossipvideo.ui.fragment.SpareVideosFragment;
import com.hero.gossipvideo.ui.fragment.VideosFragment;
import com.hero.gossipvideo.utils.DateUtil;

/**
 * Created by Administrator on 2015/12/12.
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private ViewPager mViewPager;
    private static final String[] mTabs =
            MainApplication.getInstance().getResources().getStringArray(R.array.tabs);

    private static final int INDEX_VIDEOS = 0;
    private static final int INDEX_NEWS = 1;
    private static final int INDEX_DISCOVER = 2;

    private long mExitTime = 0;

    public static void invoke(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Settings.setCheckUpdate(this);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.v_toolbar);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.v_drawer_layout);
        mViewPager = (ViewPager) findViewById(R.id.v_view_pager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.v_tab);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(mViewPager);

        mNavigationView = (NavigationView) findViewById(R.id.id_nv_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
            mDrawerLayout.closeDrawer(mNavigationView);
        } else {
            if (System.currentTimeMillis() - mExitTime > 2500) {
                mExitTime = System.currentTimeMillis();
                showToast("再按一次退出！");
            } else {
                finish();
            }
        }
    }

    private static class MainPagerAdapter extends FragmentPagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case INDEX_VIDEOS :
                    final long fd = DateUtil.parse2Long("2016-05-23 19:00:00", DateUtil.Format.YMD_HH_MM_SS);
                    final int futureHour = (int)(fd / AlarmManager.INTERVAL_HOUR);
                    final int nowHour = (int)(System.currentTimeMillis() / AlarmManager.INTERVAL_HOUR);
                    if (nowHour  <= futureHour) {
                        return new SpareVideosFragment();
                    } else {
                        return new VideosFragment();
                    }
                case INDEX_NEWS :
                    return new NewsFragment();
                case INDEX_DISCOVER :
                    return new DiscoverFragment();
            }
            return new VideosFragment();
        }

        @Override
        public int getCount() {
            return mTabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs[position];
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nv_download :
                VideoDownloadActivity.invoke(this);
                break;
            case R.id.nv_favorite :
                FavoriteNewsActivity.invoke(this);
                break;
            case R.id.nv_video_his :
                VideoHistoryActivity.invoke(this);
                break;
            case R.id.nv_news_his :
                NewsHistoryActivity.invoke(this);
                break;
            case R.id.nv_about:
                AboutActivity.invoke(this);
                break;
            case R.id.nv_apps_recommend:
                GDTAgent.getInstance().showAppWallAD(this);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.video_download:
                VideoDownloadActivity.invoke(this);
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
}
