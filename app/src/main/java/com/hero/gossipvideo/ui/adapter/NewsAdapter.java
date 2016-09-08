package com.hero.gossipvideo.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hero.gossipvideo.R;
import com.hero.gossipvideo.db.model.News;
import com.hero.gossipvideo.ui.NewsDetailActivity;
import com.hero.gossipvideo.ui.WebViewActivity;

/**
 * Created by Administrator on 2015/12/26.
 */
public class NewsAdapter extends BaseRecyclerViewAdapter<News> {

    private static final int MIN_CAR_COUNT = 7;

    public NewsAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsHolder(mInflater.inflate(R.layout.adapter_news, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final News news = getList().get(position);
        final NewsHolder mHolder = (NewsHolder) holder;

        mHolder.mNewsSource.setText(news.category);
        mHolder.mNewsReadNum.setText(news.readNum+"阅读");
        mHolder.mNewsDesc.setText(news.title);

        if (TextUtils.isEmpty(news.picSmall)) {
            mHolder.mNewsImg.setVisibility(View.GONE);
        } else {
            mHolder.mNewsImg.setVisibility(View.VISIBLE);
            mHolder.mNewsImg.setImageURI(Uri.parse(news.picSmall));
        }
    }

    public class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SimpleDraweeView mNewsImg;
        TextView mNewsDesc;
        TextView mNewsSource;
        TextView mNewsReadNum;

        public NewsHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mNewsImg = (SimpleDraweeView) view.findViewById(R.id.img_news);
            mNewsDesc = (TextView) view.findViewById(R.id.tv_news_desc);
            mNewsSource = (TextView) view.findViewById(R.id.tv_news_source);
            mNewsReadNum = (TextView) view.findViewById(R.id.tv_read_num);
        }

        @Override
        public void onClick(View v) {
            final int index = getAdapterPosition();
            final News news = mList.get(index);
            NewsDetailActivity.invoke(mContext, news);
        }
    }
}
