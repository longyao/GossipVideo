package com.hero.gossipvideo.store;

import com.ltc.lib.utils.DirUtil;

import java.io.File;

/**
 * Created by Administrator on 2016/1/1.
 */
public class DirHelper {

    private static final String DIR_NEWS_FAV_NAME = "GossipVideo/news_favorite";
    private static final String DIR_VIDEOS_DOWNLOAD = "GossipVideo/videos_download";
    private static final String DIR_ROOT = "GossipVideo";

    public static File getNewsFavoriteDir() {
        File file = new File(DirUtil.getSDCardRootDir(), DIR_NEWS_FAV_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getVideoDownloadDir() {
        File file = new File(DirUtil.getSDCardRootDir(), DIR_VIDEOS_DOWNLOAD);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getRootDir() {
        File file = new File(DirUtil.getSDCardRootDir(), DIR_ROOT);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
