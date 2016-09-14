package com.hero.gossipvideo.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hero.gossipvideo.R;
import com.hero.gossipvideo.api.ApiManager;
import com.hero.gossipvideo.content.DiscoverContent;
import com.hero.gossipvideo.db.model.Video;
import com.hero.gossipvideo.ui.PlayerActivity;
import com.hero.gossipvideo.ui.WebViewActivity;
import com.ltc.lib.utils.Utils;

/**
 * Created by Administrator on 2015/12/26.
 */
public class SpareVideosAdapter extends BaseRecyclerViewAdapter<DiscoverContent.Item> {

    public SpareVideosAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideosHolder(mInflater.inflate(R.layout.adapter_videos, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DiscoverContent.Item item = getList().get(position);
        final VideosHolder h = (VideosHolder) holder;
        h.videoTitle.setText(item.title);
        h.watchCount.setText(mContext.getString(R.string.video_watch_count, item.readNum));
        h.duration.setText("05:23");
        if (!TextUtils.isEmpty(item.pic)) {
            h.videoImg.setImageURI(Uri.parse(item.pic));
        }
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
            final DiscoverContent.Item item = mList.get(index);
            if (Utils.isEmpty(item.playUrl)) {
                WebViewActivity.invoke(mContext, item.title, ApiManager.getDiscoverDetailUrl(item.id), true);
            } else {
                WebViewActivity.invoke(mContext, item.title, item.playUrl);
            }
        }
    }
}
