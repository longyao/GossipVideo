package com.hero.gossipvideo.content;

import android.view.ViewGroup;

import com.hero.gossipvideo.db.model.HistoryVideo;
import com.hero.gossipvideo.db.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/3.
 */
public class VideoTransform {

    public static List<Video> transformHistoryVideoToVideo(List<HistoryVideo> his) {
        List<Video> res = new ArrayList<Video>(his.size());
        for (HistoryVideo v : his) {
            res.add(v);
        }
        return res;
    }

    public static List<Video> transformRemoteVideoToLocalVideo(List<VideoContent.InnerList> ins) {
        List<Video> res = new ArrayList<Video>(ins.size());
        for (VideoContent.InnerList vs : ins) {
            if (vs.getOneVideo() != null) {
                res.add(vs.getOneVideo().trans());
            }
        }
        return res;
    }

}
