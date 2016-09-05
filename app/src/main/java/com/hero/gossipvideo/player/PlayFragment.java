package com.hero.gossipvideo.player;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hero.gossipvideo.R;
import com.hero.gossipvideo.ui.view.ExVideoView;
import com.ltc.lib.utils.LogUtil;

import java.io.File;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;


/**
 * Created by Administrator on 2015/6/14.
 */
public class PlayFragment extends Fragment implements View.OnClickListener, MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener,
        MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnPreparedListener,MediaPlayer.OnSeekCompleteListener {

    protected static final int ACTION_PLAY_VIDEO = 0;
    protected static final int ACTION_SCREEN_CTR = 1;

    protected static final int SCREEN_DEFAULT = 0;
    protected static final int SCREEN_FULL = 1;
    protected int mScreenMode = SCREEN_DEFAULT;

    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    private MediaPlayer.OnPreparedListener mOnPreparedListener;
    private MediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;

    //锁定屏幕为横屏或者竖屏
    protected boolean mLockScreen = false;

    protected Activity mContext;
    protected ActionHandler mActionHandler;

    protected ProgressBar mLoadingPro;
    protected ViewGroup mVideoArea;
    protected ExVideoView mVideoView;
    protected MediaController mMediaController;
    protected RelativeLayout mControllerLayout;

    private float mVideoWidth;
    private float mVideoHeight;

    public ExVideoView getVideoView() {
        return mVideoView;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener ls) {
        mOnCompletionListener = ls;
    }

    public void setOnBufferingUpdateListener(MediaPlayer.OnBufferingUpdateListener ls) {
        mOnBufferingUpdateListener = ls;
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener ls) {
        mOnPreparedListener = ls;
    }

    public void setOnSeekCompleteListener(MediaPlayer.OnSeekCompleteListener ls) {
        mOnSeekCompleteListener = ls;
    }

    public RelativeLayout getControllerLayout() {
        return mControllerLayout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        mActionHandler = new ActionHandler();
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle state) {
        View view = inflater.inflate(R.layout.fragment_player, null);

        mLoadingPro = (ProgressBar) view.findViewById(R.id.pb_loading_progress);
        mMediaController = (MediaController) view.findViewById(R.id.v_video_controller);
        mVideoArea = (ViewGroup) view.findViewById(R.id.video_area);
        mVideoView = (ExVideoView) view.findViewById(R.id.video_view);
        mVideoView.setMediaController(mMediaController);
        mControllerLayout = (RelativeLayout) view.findViewById(R.id.v_controller_layout);

        registListener();
        mMediaController.setVisibility(View.GONE);
        return view;
    }

    private void registListener() {
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnBufferingUpdateListener(this);
        mVideoView.setOnSeekCompleteListener(this);
        mVideoView.setOnErrorListener(this);
    }

    public void lockLandSpaceScreen() {
        mLockScreen = true;
        mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void lockPortraitScreen() {
        mLockScreen = true;
        mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void setVideoViewSize(float width, float height) {
        mVideoWidth = width;
        mVideoHeight = height;
        setVideoViewScreenCtr(SCREEN_DEFAULT);
    }

    //设置屏幕横屏和竖屏
    public void setVideoViewScreenCtr(int screenCtr) {

        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int height = 0;
        if (mVideoWidth != 0f && mVideoHeight != 0f) {
            height = (int)(dm.widthPixels * mVideoHeight / mVideoWidth);
        } else {//默认16/9播放界面
            height = dm.widthPixels * 3 / 4;
        }
        LogUtil.d("video height = " + height);

        RelativeLayout.LayoutParams mVideoAreaPm = (RelativeLayout.LayoutParams ) mVideoArea.getLayoutParams();
        RelativeLayout.LayoutParams mVideoViewPm = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
        if (screenCtr == SCREEN_FULL) {
            mVideoViewPm.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            mVideoViewPm.height = height;
            mVideoAreaPm.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            mVideoAreaPm.height = RelativeLayout.LayoutParams.MATCH_PARENT;
//            mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            mVideoViewPm.width = dm.widthPixels;
            mVideoViewPm.height = height;
            mVideoAreaPm.width = dm.widthPixels;
            mVideoAreaPm.height = height;
//            mContext.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        mScreenMode = screenCtr;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mOnPreparedListener != null) {
            mOnPreparedListener.onPrepared(mp);
        }
        mMediaController.setVisibility(View.VISIBLE);
        mMediaController.show();
        if (mLoadingPro.getVisibility() == View.VISIBLE) {
            mLoadingPro.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (mOnBufferingUpdateListener != null) {
            mOnBufferingUpdateListener.onBufferingUpdate(mp, percent);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mOnCompletionListener != null) {
            mOnCompletionListener.onCompletion(mp);
        }
        if (mLoadingPro.getVisibility() == View.VISIBLE) {
            mLoadingPro.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        if (mOnSeekCompleteListener != null) {
            mOnSeekCompleteListener.onSeekComplete(mp);
        }
        if (mLoadingPro.getVisibility() == View.VISIBLE) {
            mLoadingPro.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onClick(View view) {
        //
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        if (dm.widthPixels > dm.heightPixels) {
            setVideoViewScreenCtr(SCREEN_FULL);
        } else {
            setVideoViewScreenCtr(SCREEN_DEFAULT);
        }

        //手动设置横屏和竖屏时，延时设置为sensor
        if (!mLockScreen) {
            if (mContext.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
                mActionHandler.screenCtr();
            }
        }
    }

    public void setFileName(String fileName) {
        if (mMediaController != null) {
            mMediaController.setFileName(fileName);
        }
    }

    public void startPlay(String url) {
        mVideoView.setVideoURI(Uri.parse(url));
        mLoadingPro.setVisibility(View.VISIBLE);
    }

    public void startPlay(File file) {
        mVideoView.setVideoURI(Uri.fromFile(file));
        mLoadingPro.setVisibility(View.VISIBLE);
    }

    public void startPlayPosition(long position) {
        mVideoView.start();
        mVideoView.seekTo(position);
        mLoadingPro.setVisibility(View.VISIBLE);
    }

    public void stopPlay() {
        mVideoView.stopPlayback();
    }

    public void pause() {
        mVideoView.pause();
    }

    private class ActionHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ACTION_SCREEN_CTR :
                    mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    break;
            }
        }

        public void screenCtr() {
            removeMessages(ACTION_SCREEN_CTR);
            sendEmptyMessageDelayed(ACTION_SCREEN_CTR, 5000);
        }
    }
}
