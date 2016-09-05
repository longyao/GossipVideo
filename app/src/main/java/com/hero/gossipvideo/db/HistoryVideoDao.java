package com.hero.gossipvideo.db;

import com.hero.gossipvideo.db.model.HistoryVideo;
import com.ltc.lib.db.dao.AbsSQLiteDao;

/**
 * Created by Administrator on 2016/1/1.
 */
public class HistoryVideoDao extends AbsSQLiteDao<HistoryVideo, String> {

    private static final class HistoryVideoDaoHolder {
        private static final HistoryVideoDao sInstance = new HistoryVideoDao();
    }

    private HistoryVideoDao() {
        super(HistoryVideo.class);
    }

    public static HistoryVideoDao getInstance() {
        return HistoryVideoDaoHolder.sInstance;
    }
}
