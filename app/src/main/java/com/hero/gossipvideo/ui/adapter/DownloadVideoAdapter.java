package com.hero.gossipvideo.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hero.gossipvideo.R;
import com.hero.gossipvideo.db.model.DownloadVideo;
import com.hero.gossipvideo.db.model.HistoryVideo;
import com.hero.gossipvideo.db.model.Video;
import com.hero.gossipvideo.ui.PlayerActivity;
import com.ltc.lib.utils.Utils;

/**
 * Created by Administrator on 2016/1/2.
 */
public class DownloadVideoAdapter extends BaseRecyclerViewAdapter<DownloadVideo> {

    private UpdateStatus mUpdateStatus;

    public DownloadVideoAdapter(Context context) {
        super(context);
        mUpdateStatus = new UpdateStatus();
    }

    //提供给下载器更新进度
    public void notifyUpdate(String id, long total, long current, boolean finish) {

        mUpdateStatus.id = id;
        mUpdateStatus.total = total;
        mUpdateStatus.current = current;
        mUpdateStatus.finish = finish;

        if (finish) {
            updateStatus(id, DownloadVideo.STATUS_FINISH);
        } else {
            updateStatus(id, DownloadVideo.STATUS_DOWNLOADING);
        }

        notifyDataSetChanged();
    }

    //更新内存里面的状态
    public void updateStatus(String id, int status) {

        if (Utils.isEmpty(mList)) {
            return;
        }

        for (DownloadVideo dv : mList) {
            if (dv.id.equals(id)) {
                dv.downloadStatus = status;
                return;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideosHolder(mInflater.inflate(R.layout.adapter_download_video, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VideosHolder vh = (VideosHolder) holder;
        final DownloadVideo v = mList.get(position);
        vh.videoImg.setImageURI(Uri.parse(v.imgUrl));

        if (v.id.equals(mUpdateStatus.id)) {
            if (mUpdateStatus.finish) {
                vh.videoStatus.setText("下载完成");
            } else {
                vh.videoStatus.setText((mUpdateStatus.current * 100 / mUpdateStatus.total) + "%" );
            }
        } else if (v.downloadStatus == DownloadVideo.STATUS_FINISH) {
            vh.videoStatus.setText("下载完成");
        } else if (v.downloadStatus == DownloadVideo.STATUS_DOWNLOADING) {
            vh.videoStatus.setText("下载中");
        } else {
            vh.videoStatus.setText("待下载");
        }
    }

    private class VideosHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SimpleDraweeView videoImg;
        TextView videoStatus;

        public VideosHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            videoImg = (SimpleDraweeView) v.findViewById(R.id.img_video);
            videoStatus = (TextView) v.findViewById(R.id.tv_status);
        }

        @Override
        public void onClick(View v) {
            final int index = getAdapterPosition();
            PlayerActivity.invoke(mContext, mList.get(index));
        }
    }

    private class UpdateStatus {
        String id;
        long total;
        long current;
        boolean finish;
    }
}
