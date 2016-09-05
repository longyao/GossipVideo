package com.hero.gossipvideo.db;


import com.hero.gossipvideo.db.model.News;
import com.ltc.lib.db.dao.AbsSQLiteDao;

/**
 * Created by Administrator on 2015/9/12.
 */
public class NewsDao extends AbsSQLiteDao<News, Long> {

    private final static class ArticleDaoHolder {
        private static final NewsDao mInstance = new NewsDao();
    }

    public static NewsDao getInstance() {
        return ArticleDaoHolder.mInstance;
    }

    public NewsDao() {
        super(News.class);
    }

    public void saveOrUpdateNews(final News news) {

        News old = getForEq("unique", news.unique);
        news.updateTime = System.currentTimeMillis();

        if (old == null) {
            save(news);
        } else {
            update(news);
        }
    }
}
