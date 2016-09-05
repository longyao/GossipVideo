package com.hero.gossipvideo.db;

import com.hero.gossipvideo.db.model.DownloadVideo;
import com.hero.gossipvideo.db.model.Video;
import com.ltc.lib.db.dao.AbsSQLiteDao;

import java.util.List;

/**
 * Created by Administrator on 2016/1/3.
 */
public class DownloadVideoDao extends AbsSQLiteDao<DownloadVideo, String> {

    private static final class DownloadVideoDaoHolder {
        private static final DownloadVideoDao sInstance = new DownloadVideoDao();
    }

    private DownloadVideoDao() {
        super(DownloadVideo.class);
    }

    public static DownloadVideoDao getInstance() {
        return DownloadVideoDaoHolder.sInstance;
    }

    public boolean isExist(DownloadVideo video) {
        DownloadVideo old = getForEq("id", video.id);
        return old != null;
    }

    public List<DownloadVideo> getNotDownloadFinishVideos() {
        return getListForNotEq("downloadStatus", DownloadVideo.STATUS_FINISH);
    }

    public boolean hasVideoDownloadFinish(Video video) {
        if (video != null) {
            DownloadVideo dv = getForEq("id", video.id);
            return ((dv != null) && (dv.downloadStatus == DownloadVideo.STATUS_FINISH));
        }
        return false;
    }
}
