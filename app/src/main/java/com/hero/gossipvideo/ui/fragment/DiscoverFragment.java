package com.hero.gossipvideo.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hero.gossipvideo.R;
import com.hero.gossipvideo.api.ApiManager;
import com.hero.gossipvideo.content.DiscoverContent;
import com.hero.gossipvideo.ui.adapter.DiscoversAdapter;
import com.hero.gossipvideo.ui.view.DividerItemDecoration;
import com.ltc.lib.net.api.HttpResult;
import com.ltc.lib.utils.JsonUtil;
import com.ltc.lib.utils.Utils;

/**
 * Created by Administrator on 2015/12/24.
 */
public class DiscoverFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener {

    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private DiscoversAdapter mDiscoverAdapter;

    private TextView mLoadingMoreTv;
    private TextView mReloadTv;

    private int mTotalItems;
    private DiscoverReqTask mReqTask;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle state) {
        View view = inflater.inflate(R.layout.fragment_discover, null);

        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.v_swipe_refresh);
        mSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light);
        mSwipeRefresh.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.v_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mDiscoverAdapter = new DiscoversAdapter(mContext));
        mRecyclerView.addOnScrollListener(new RecyclerViewScrollListener(mDiscoverAdapter));

        mLoadingMoreTv = (TextView) view.findViewById(R.id.tv_load_more);
        mReloadTv = (TextView) view.findViewById(R.id.tv_reload);
        mReloadTv.setOnClickListener(this);

        reqVideos(REQ_FIRST);

        return view;
    }

    private void reqVideos(int type) {
        if (mReqTask != null && mReqTask.getStatus() != AsyncTask.Status.FINISHED) {
            return;
        }
        mReqTask = new DiscoverReqTask(type);
        mReqTask.execute();
    }

    @Override
    public void onRefresh() {
        reqVideos(REQ_REFRESH);
    }

    @Override
    public void onClick(View v) {
        if (v == mReloadTv) {
            reload();
        }
    }

    public void reqNextPage() {
        final int count = (mDiscoverAdapter == null) ? 0 : mDiscoverAdapter.getItemCount();
        if (count < mTotalItems) {
            reqVideos(REQ_NEXT);
        }
    }

    private void reload() {
        mReloadTv.setVisibility(View.GONE);
        reqVideos(REQ_FIRST);
    }

    private class DiscoverReqTask extends AsyncTask<Void, Void, HttpResult> {

        private int mType;

        public DiscoverReqTask(int type) {
            mType = type;
        }

        @Override
        protected void onPreExecute() {
            if (mType == REQ_FIRST || mType == REQ_REFRESH) {
                mSwipeRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefresh.setRefreshing(true);
                    }
                });
            } else if (mType == REQ_NEXT) {
                mLoadingMoreTv.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected HttpResult doInBackground(Void... params) {
            return ApiManager.getDiscoverItems(getStartIndex());
        }

        private int getStartIndex() {

            if (REQ_FIRST == mType || REQ_REFRESH == mType) {
                return 0;
            } else {
                return mDiscoverAdapter == null ? 0 : mDiscoverAdapter.getItemCount();
            }

        }

        @Override
        protected void onPostExecute(HttpResult result) {

            boolean isEmpty = true;
            if (result != null && result.isSuccess()) {
                final DiscoverContent content = JsonUtil.fromJson(result.data, DiscoverContent.class);
                if (content != null && content.data != null) {
                    mTotalItems = content.data.totalItems;
                    if (mType == REQ_FIRST || mType == REQ_REFRESH) {
                        mDiscoverAdapter.setList(content.data.items);
                    } else {
                        mDiscoverAdapter.addAll(content.data.items);
                    }
                    //如果是第一次加载，判断是否是加载失败，以便重新加载
                    if (mType == REQ_FIRST) {
                        isEmpty = Utils.isEmpty(content.data.items);
                    }
                }
                mDiscoverAdapter.notifyDataSetChanged();
            }

            if (mType == REQ_FIRST || mType == REQ_REFRESH) {
                mSwipeRefresh.setRefreshing(false);
            } else if (mType == REQ_NEXT) {
                mLoadingMoreTv.setVisibility(View.GONE);
            }

            if (mType == REQ_FIRST && isEmpty) {
                mReloadTv.setVisibility(View.VISIBLE);
            }
        }
    }
}
