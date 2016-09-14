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
import com.hero.gossipvideo.ui.WebViewActivity;
import com.ltc.lib.utils.Utils;

/**
 * Created by Administrator on 2015/12/26.
 */
public class DiscoversAdapter extends BaseRecyclerViewAdapter<DiscoverContent.Item> {

    public DiscoversAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsHolder(mInflater.inflate(R.layout.adapter_discovers, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final DiscoverContent.Item item = getList().get(position);
        final NewsHolder mHolder = (NewsHolder) holder;

        mHolder.mDiscoverSource.setText(item.tagName);
        mHolder.mDiscoverDesc.setText(item.title);

        if (Utils.isEmpty(item.playUrl)) {
            mHolder.mDiscoverReadNum.setText((item.readNum + Utils.random(10000)) + "阅读");
            mHolder.mPlayImg.setVisibility(View.GONE);
        } else {
            mHolder.mDiscoverReadNum.setText(item.readNum + "播放");
            mHolder.mPlayImg.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(item.pic)) {
            mHolder.mDiscoverImg.setVisibility(View.GONE);
        } else {
            mHolder.mDiscoverImg.setVisibility(View.VISIBLE);
            mHolder.mDiscoverImg.setImageURI(Uri.parse(item.pic));
        }
    }

    public class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SimpleDraweeView mDiscoverImg;
        ImageView mPlayImg;
        TextView mDiscoverDesc;
        TextView mDiscoverSource;
        TextView mDiscoverReadNum;

        public NewsHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mDiscoverImg = (SimpleDraweeView) view.findViewById(R.id.img_discover);
            mPlayImg = (ImageView) view.findViewById(R.id.img_discover_play);
            mDiscoverDesc = (TextView) view.findViewById(R.id.tv_discover_desc);
            mDiscoverSource = (TextView) view.findViewById(R.id.tv_discover_source);
            mDiscoverReadNum = (TextView) view.findViewById(R.id.tv_read_num);
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
