package com.hero.gossipvideo.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hero.gossipvideo.R;
import com.hero.gossipvideo.db.model.HistoryVideo;
import com.hero.gossipvideo.db.model.Video;
import com.hero.gossipvideo.ui.PlayerActivity;

/**
 * Created by Administrator on 2015/12/13.
 */
public class VideosAdapter extends BaseRecyclerViewAdapter<Video> {

    public VideosAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideosHolder(mInflater.inflate(R.layout.adapter_videos, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Video v = getList().get(position);
        final VideosHolder h = (VideosHolder) holder;
        h.videoTitle.setText(v.videoTitle);
        h.watchCount.setText(mContext.getString(R.string.video_watch_count, v.watchCount));
        h.duration.setText(second2minute(Integer.parseInt(v.duration)));
        h.videoImg.setImageURI(Uri.parse(v.imgUrl));
    }

    private String second2minute(int second) {
        final int m = second / 60;
        final int s = second - m*60;
        final String min = m > 0 ? (m >= 10 ? String.valueOf(m) : "0" + m) : "00";
        final String sec = s > 10 ? String.valueOf(s) : "0" + s;
        return min + ":" + sec;
    }

    public class VideosHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView videoTitle;
        TextView duration;
        TextView watchCount;
        SimpleDraweeView videoImg;

        public VideosHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            videoTitle = (TextView) v.findViewById(R.id.tv_video_desc);
            duration = (TextView) v.findViewById(R.id.tv_duration);
            watchCount = (TextView) v.findViewById(R.id.tv_watch_count);
            videoImg = (SimpleDraweeView) v.findViewById(R.id.img_video);
        }

        @Override
        public void onClick(View v) {
            final int index = getAdapterPosition();
            if (index >= 0) {
                PlayerActivity.invoke(mContext, mList.get(index));
            }
        }
    }
}
