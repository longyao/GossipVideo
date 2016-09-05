package com.hero.gossipvideo.store;

import com.hero.gossipvideo.db.model.News;
import com.ltc.lib.encrypt.MD5;
import com.ltc.lib.utils.LogUtil;
import com.ltc.lib.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/4/23.
 */
public class NewsStore extends FileStore<News> {

    private static final String STORE_OBJ_SUFFIX = ".obj";

    public void addFavoriteNews(News news) {
        String fileName = genFavoriteNewsFileName(news);
        if (!Utils.isEmpty(fileName)) {
            saveObjToFile(news, new File(DirHelper.getNewsFavoriteDir(),fileName));
        }
    }

    public void deleteFavoriteNews(News news) {
        String fileName = genFavoriteNewsFileName(news);
        if (!Utils.isEmpty(fileName)) {
            deleteObjFromFile(new File(DirHelper.getNewsFavoriteDir(),fileName));
        }
    }

    public boolean isNewsExistsStore(News news) {
        String fileName = genFavoriteNewsFileName(news);
        return isObjExists(new File(DirHelper.getNewsFavoriteDir(),fileName));
    }

    public List<News> getNewsListFromStore() {

        File newsObjDir = DirHelper.getNewsFavoriteDir();
        File[] files = newsObjDir.listFiles();

        if (files == null || files.length == 0) {
            return null;
        }

        List<News> results = new ArrayList<News>();
        for (File f : files) {
            News news = getObjFromFile(f);
            results.add(news);
        }

        LogUtil.d("store result size = " + results.size());
        return results;
    }

    private static String genFavoriteNewsFileName(News news) {

        if (news == null) {
            return null;
        }

        String name = MD5.strToMD5(news.unique);
        if (!Utils.isEmpty(name)) {
            return name + STORE_OBJ_SUFFIX;
        } else {
            return null;
        }
    }
}
