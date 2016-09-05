package com.hero.gossipvideo.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by longtc on 16-3-10.
 */
public class PlayUrl implements Serializable {

    @Expose
    @SerializedName("videoFiles")
    public Map<String, Media> videos;

    public static class Media {

        @Expose
        @SerializedName("mediaUrl")
        public String mediaUrl;
    }
}
