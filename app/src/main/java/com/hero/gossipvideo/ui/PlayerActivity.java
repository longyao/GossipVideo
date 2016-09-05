package com.hero.gossipvideo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hero.gossipvideo.R;
import com.hero.gossipvideo.ads.gdt.GDTAgent;
import com.hero.gossipvideo.api.ApiManager;
import com.hero.gossipvideo.content.PlayBean;
import com.hero.gossipvideo.content.PlayUrl;
import com.hero.gossipvideo.db.DownloadVideoDao;
import com.hero.gossipvideo.db.HistoryVideoDao;
import com.hero.gossipvideo.db.model.HistoryVideo;
import com.hero.gossipvideo.db.model.Video;
import com.hero.gossipvideo.download.VideoDownloader;
import com.hero.gossipvideo.player.PlayFragment;
import com.hero.gossipvideo.store.DirHelper;
import com.hero.gossipvideo.ui.view.ExVideoView;
import com.ltc.lib.net.api.HttpResult;
import com.ltc.lib.net.api.HttpWorker;
import com.ltc.lib.net.api.RequestPrepare;
import com.ltc.lib.net.param.Method;
import com.ltc.lib.utils.JsonUtil;
import com.ltc.lib.utils.Utils;
import com.squareup.okhttp.internal.Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by Administrator on 2015/12/19.
 */
public class PlayerActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXTRA_VIDEO = "ext_video";

    private PlayFragment mPlayFragment;
    private Video mVideo;

    private View mReplayView;
    private ImageView mReplayImg;
    private TextView mDownloadTv;
    private ImageView mBatteryImg;
    private TextView mTimeTv;

    BatteryReceiver mBatteryReceiver;

    public static void invoke(Context context, Video video) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(EXTRA_VIDEO, video);
        context.startActivity(intent);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Vitamio.isInitialized(this);
        setContentView(R.layout.activity_player);

        registerBatteryReceiver();
        getDataFromExtra();
        initView();
        refreshTime();

        playVideo();

        GDTAgent.getInstance().prepareShowInsertAd(this);
    }

    private void getDataFromExtra() {
        mVideo = (Video)getIntent().getSerializableExtra(EXTRA_VIDEO);
    }

    private void initView() {

        mPlayFragment = (PlayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_player);
        MVideoStatusListener mVideoStatusListener = new MVideoStatusListener();
        mPlayFragment.getVideoView().setVideoStatusListener(mVideoStatusListener);
        mPlayFragment.setOnCompletionListener(mVideoStatusListener);

        mReplayView = findViewById(R.id.v_replay_layout);
        mReplayImg = (ImageView) findViewById(R.id.img_replay);
        mReplayImg.setOnClickListener(this);

        final RelativeLayout layout = mPlayFragment.getControllerLayout();
        LayoutInflater.from(this).inflate(R.layout.container_player_download, layout);
        mDownloadTv = (TextView) layout.findViewById(R.id.tv_download);
        mDownloadTv.setOnClickListener(this);
        mBatteryImg = (ImageView) findViewById(R.id.img_battery);
        mTimeTv = (TextView) findViewById(R.id.tv_time);
    }

    private void playVideo() {

        File file = null;
        if (DownloadVideoDao.getInstance().hasVideoDownloadFinish(mVideo)) {
            file = getVideoInLocal(mVideo);
        }

        if (file != null) {
            startPlay(file.getAbsolutePath());
        } else {
            new PlayTask().execute();
        }
    }

    public File getVideoInLocal(Video video) {

        File[] files = DirHelper.getVideoDownloadDir().listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        for (File f : files) {
            if (f.getName().indexOf(video.id) >= 0) {
                return f;
            }
        }
        return null;
    }

    private class PlayTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return ApiManager.getVideoPlayUrl(mVideo.extend);
        }

        @Override
        protected void onPostExecute(String url) {
            if (!Utils.isEmpty(url)) {
                startPlay(url);
            } else {
                showToast("此视频不能播放");
            }
        }
    }

    private void startPlay(String url) {
        mPlayFragment.setVideoViewSize(0, 0);

        HistoryVideo hs = new HistoryVideo(mVideo);
        hs.updateTime = System.currentTimeMillis();
        HistoryVideoDao.getInstance().saveOrUpdate(hs);

        mPlayFragment.startPlay(url);
        mPlayFragment.setFileName(mVideo.videoTitle);

        listenerPlayDuration();
    }

    @Override
    public void onClick(View v) {
        if (v == mReplayImg) {
            mReplayView.setVisibility(View.GONE);
            mPlayFragment.startPlayPosition(0);
        } else if (v == mDownloadTv) {
            showToast("已添加到下载列表");
            VideoDownloader.getInstance().download(mVideo);
        }
    }

    @Override
    public void onBackPressed() {
        mPlayFragment.stopPlay();
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterBatteryReceiver();
    }

    private void refreshTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat cf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        Date date = calendar.getTime();
        mTimeTv.setText(cf.format(date));
    }

    private class MVideoStatusListener implements ExVideoView.VideoStatusListener,MediaPlayer.OnCompletionListener {
        @Override
        public void open() {
            //do nothing
        }

        @Override
        public void start() {
            //do nothing
        }

        @Override
        public void pause() {
            GDTAgent.getInstance().showInsertAD(PlayerActivity.this);
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            mReplayView.setVisibility(View.VISIBLE);
            GDTAgent.getInstance().showInsertAD(PlayerActivity.this);
        }
    }

    private class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int level = intent.getIntExtra("level", 0);
//                int scale = intent.getIntExtra("scale", 100);
                if (level == 100) {
                    mBatteryImg.setImageResource(R.drawable.ic_battery5_1);
                } else if (level > 80 && level < 100) {
                    mBatteryImg.setImageResource(R.drawable.ic_battery4_1);
                } else if (level > 60 && level <= 80) {
                    mBatteryImg.setImageResource(R.drawable.ic_battery3_1);
                } else if (level > 40 && level <= 60) {
                    mBatteryImg.setImageResource(R.drawable.ic_battery2_1);
                } else if (level > 20 && level <= 40) {
                    mBatteryImg.setImageResource(R.drawable.ic_battery1_1);
                } else {
                    mBatteryImg.setImageResource(R.drawable.ic_battery0_1);
                }
            } else if (intent.ACTION_TIME_TICK.equals(action)) {
                refreshTime();
            }
        }
    }

    private void registerBatteryReceiver() {

        if (mBatteryReceiver != null) {
            unregisterReceiver(mBatteryReceiver);
        }

        mBatteryReceiver = new BatteryReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mBatteryReceiver, intentFilter);
    }

    public void unRegisterBatteryReceiver() {
        if (mBatteryReceiver != null) {
            unregisterReceiver(mBatteryReceiver);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            boolean goOn = true;
            if (mPlayFragment != null) {
                final VideoView vv = mPlayFragment.getVideoView();
                final int durationSec = (int) (vv.getDuration() / 1000);
                final int currentSec = (int) (vv.getCurrentPosition() / 1000);
                if ((durationSec - currentSec) <= 3) {
                    GDTAgent.getInstance().showInsertAD(PlayerActivity.this);
                    goOn = false;
                }
            }
            if (goOn) {
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    private void listenerPlayDuration() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }
}
