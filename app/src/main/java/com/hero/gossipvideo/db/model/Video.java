package com.hero.gossipvideo.db.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/3.
 */
public class Video implements Serializable {

    @DatabaseField(columnName = "id", id = true)
    public String id;

    @DatabaseField(columnName = "videoTitle")
    public String videoTitle;

    @DatabaseField(columnName = "videoSize")
    public String videoSize;

    @DatabaseField(columnName = "duration")
    public String duration;

    @DatabaseField(columnName = "imgUrl")
    public String imgUrl;

    @DatabaseField(columnName = "downloadUrl")
    public String downloadUrl;

    @DatabaseField(columnName = "watchCount")
    public int watchCount;

    @DatabaseField(columnName = "shareUrl")
    public String shareUrl;

    @DatabaseField(columnName = "extend")
    public String extend;

}
