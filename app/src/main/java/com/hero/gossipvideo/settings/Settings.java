package com.hero.gossipvideo.settings;

import android.content.Context;

import com.hero.gossipvideo.download.VideoDownloader;
import com.umeng.update.UmengUpdateAgent;

import io.vov.vitamio.Vitamio;

public class Settings {

    public static void init(Context context) {
        initApp(context);
        VideoDownloader.getInstance().downloadExistVideosIfNecessary();
    }

    public static void initApp(Context context) {
        Vitamio.isInitialized(context.getApplicationContext());
    }

    public static void setCheckUpdate(Context context) {
        UmengUpdateAgent.setDeltaUpdate(false);
        UmengUpdateAgent.update(context);
    }

}
