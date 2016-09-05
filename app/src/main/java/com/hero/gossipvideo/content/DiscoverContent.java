package com.hero.gossipvideo.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/3/13.
 */
public class DiscoverContent {

    @Expose
    @SerializedName("response")
    public Data data;

    public static class Data {

        @Expose
        @SerializedName("numFound")
        public int totalItems;

        @Expose
        @SerializedName("docs")
        public List<Item> items;
    }

    public static class Item {

        @Expose
        @SerializedName("id")
        public String id;

        @Expose
        @SerializedName("assetName")
        public String title;

        @Expose
        @SerializedName("totalclick")
        public int readNum;

        @Expose
        @SerializedName("newtagname")
        public String tagName;

        @Expose
        @SerializedName("oplusphoto")
        public String pic;

        @Expose
        @SerializedName("playUrl")
        public String playUrl;
    }
}
