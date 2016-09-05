package com.hero.gossipvideo.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hero.gossipvideo.db.model.Video;
import com.ltc.lib.utils.Utils;

import java.util.List;

/**
 * Created by Administrator on 2015/12/13.
 */
public class VideoContent {

    @Expose
    @SerializedName("bodyList")
    public List<InnerList> videos;

    public static class InnerList {

        @Expose
        @SerializedName("videoList")
        public List<InnerVideo> videoList;

        public InnerVideo getOneVideo() {
            return Utils.isEmpty(videoList) ? null : videoList.get(0);
        }
    }

    public static class InnerVideo {

        @Expose
        @SerializedName("id")
        public String id;

        @Expose
        @SerializedName("title")
        public String videoTitle;


        @Expose
        @SerializedName("image")
        public String imgUrl;

        @Expose
        @SerializedName("playTime")
        public int watchCount;

        @Expose
        @SerializedName("memberItem")
        public MemberItem item;

        public Video trans() {
            Video video = new Video();
            video.id = this.id;
            video.videoTitle = this.videoTitle;
            video.watchCount = this.watchCount;
            video.imgUrl = this.imgUrl;
            if (item != null) {
                video.duration = item.duration;
                video.shareUrl = item.shareUrl;
                video.extend = item.guid;
            }
            return video;
        }
    }

    public static class MemberItem {

        @Expose
        @SerializedName("guid")
        public String guid;

        @Expose
        @SerializedName("duration")
        public String duration;

        @Expose
        @SerializedName("shareUrl")
        public String shareUrl;

        @Expose
        @SerializedName("itemId")
        public String itemId;
    }

}
