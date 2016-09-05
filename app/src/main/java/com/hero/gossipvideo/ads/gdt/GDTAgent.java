package com.hero.gossipvideo.ads.gdt;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.qq.e.ads.appwall.APPWall;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

/**
 * Created by Administrator on 2016/1/9.
 */
public class GDTAgent {

    public static final String APP_ID = "1105024879";

    public static final String POST_ID_SPLASH = "9010601987562879";
    public static final String POST_ID_HOME_APP_WALL = "5010502967165993";
    public static final String POST_ID_VIDEO_PLAY_INSERT = "7010208947861960";
    public static final String POST_ID_NEWS_DETAIL_BANNER = "9070304937869911";
    public static final String POST_ID_DISCOVER_BANNER = "7000002937865972";

    private static final class GDTAgentHolder {
        private final static GDTAgent sInstance = new GDTAgent();
    }

    public static GDTAgent getInstance() {
        return GDTAgentHolder.sInstance;
    }

    public void showSplashAD(Activity context, ViewGroup container, SplashADListener ls) {
        SplashAD ad = new SplashAD(context, container, APP_ID, POST_ID_SPLASH, ls);
    }

    public void showInsertAD(Activity context) {
        final InterstitialAD ad = new InterstitialAD(context, APP_ID, POST_ID_VIDEO_PLAY_INSERT);
        ad.setADListener(new AbstractInterstitialADListener() {
            @Override
            public void onNoAD(int arg0) {
                //do nothing
            }

            @Override
            public void onADReceive() {
                ad.show();
            }
        });
        ad.loadAD();
    }

    public void prepareShowInsertAd(Activity activity) {
        final InterstitialAD ad = new InterstitialAD(activity, APP_ID, POST_ID_VIDEO_PLAY_INSERT);
        ad.loadAD();
    }

    public void showBannerAD(Activity context, final ViewGroup container, String postId) {
        final BannerView banner = new BannerView(context, ADSize.BANNER, APP_ID, postId);
        //设置广告轮播时间，为0或30~120之间的数字，单位为s,0标识不自动轮播
        banner.setRefresh(10);
        banner.setADListener(new AbstractBannerADListener() {
            @Override
            public void onNoAD(int i) {
                //do nothing
            }

            @Override
            public void onADReceiv() {
                container.addView(banner);
            }
        });
        banner.setShowClose(true);
        banner.loadAD();
    }

    public void showAppWallAD(Context context ) {
        final APPWall wall = new APPWall(context, APP_ID, POST_ID_HOME_APP_WALL);
        wall.doShowAppWall();
    }
}
