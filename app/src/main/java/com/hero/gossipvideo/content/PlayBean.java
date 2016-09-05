package com.hero.gossipvideo.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/19.
 */
public class PlayBean implements Serializable {

    @Expose
    @SerializedName("video")
    public String id;

    @Expose
    @SerializedName("name")
    public String name;

    @Expose
    @SerializedName("mp4")
    public List<PlayVideo> mp4s;

    public static class PlayVideo implements Serializable {

        @Expose
        @SerializedName("http")
        public String url;

        @Expose
        @SerializedName("code")
        public String code;
    }

}
