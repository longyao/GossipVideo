package com.hero.gossipvideo.ui.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hero.gossipvideo.ui.adapter.BaseRecyclerViewAdapter;

/**
 * Created by longtc on 16/9/3.
 */
public abstract class BaseFragment extends Fragment {

    protected static final int REQ_FIRST = 0;
    protected static final int REQ_REFRESH = 1;
    protected static final int REQ_NEXT = 2;

    protected Context mContext;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    public abstract void reqNextPage();

    public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

        private boolean hasDownScroll = false;

        private final BaseRecyclerViewAdapter mAdapter;

        public RecyclerViewScrollListener(BaseRecyclerViewAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            final LinearLayoutManager m = (LinearLayoutManager) recyclerView.getLayoutManager();
            final int lastVisibleItem = m.findLastVisibleItemPosition();
            if (hasDownScroll && newState == 0 && lastVisibleItem == mAdapter.getItemCount() - 1) {
                reqNextPage();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            hasDownScroll = (dy > 0);
        }
    }
}
