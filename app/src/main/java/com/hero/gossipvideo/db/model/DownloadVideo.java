package com.hero.gossipvideo.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2016/1/3.
 */
@DatabaseTable(tableName = "t_download_video")
public class DownloadVideo extends Video {

    public static final int STATUS_IDLE = 0;
    public static final int STATUS_DOWNLOADING = 1;
    public static final int STATUS_FINISH = 2;

    @DatabaseField(columnName = "downloadStatus")
    public int downloadStatus;

    @DatabaseField(columnName = "updateTime")
    public long updateTime;

    public DownloadVideo() {

    }

    public DownloadVideo(Video video) {
        this.id = video.id;
        this.videoTitle = video.videoTitle;
        this.downloadUrl = video.downloadUrl;
        this.duration = video.duration;
        this.imgUrl = video.imgUrl;
        this.videoSize = video.videoSize;
        this.watchCount = video.watchCount;
        this.updateTime = System.currentTimeMillis();
    }
}
