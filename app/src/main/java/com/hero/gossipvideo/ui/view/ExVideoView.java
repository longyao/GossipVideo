package com.hero.gossipvideo.ui.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by Administrator on 2014/6/14.
 */
public class ExVideoView extends VideoView {

    private VideoStatusListener mStatusListener;

    public ExVideoView(Context context) {
        super(context);
    }

    public ExVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setVideoStatusListener(VideoStatusListener listener) {
        mStatusListener = listener;
    }

    @Override
    public void setVideoURI(Uri uri) {
        super.setVideoURI(uri);
        if (mStatusListener != null) {
            mStatusListener.open();
        }
    }

    @Override
    public void start() {
        super.start();
        if (mStatusListener != null) {
            mStatusListener.start();
        }
    }

    @Override
    public void pause() {
        super.pause();
        if (mStatusListener != null) {
            mStatusListener.pause();
        }
    }

    public interface VideoStatusListener {

        public void open();

        public void start();

        public void pause();
    }
}
