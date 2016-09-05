package com.hero.gossipvideo.db.model;

import android.graphics.Point;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.ltc.lib.utils.Utils;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/13.
 */
@DatabaseTable(tableName = "t_history_video")
public class HistoryVideo extends Video {

    @DatabaseField(columnName = "updateTime")
    public long updateTime;

    public HistoryVideo() {

    }

    public HistoryVideo(Video video) {
        this.id = video.id;
        this.videoTitle = video.videoTitle;
        this.downloadUrl = video.downloadUrl;
        this.duration = video.duration;
        this.imgUrl = video.imgUrl;
        this.videoSize = video.videoSize;
        this.watchCount = video.watchCount;
    }

}
