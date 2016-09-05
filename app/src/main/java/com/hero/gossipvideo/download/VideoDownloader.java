package com.hero.gossipvideo.download;

import android.os.AsyncTask;

import com.hero.gossipvideo.api.ApiManager;
import com.hero.gossipvideo.content.PlayBean;
import com.hero.gossipvideo.db.DownloadVideoDao;
import com.hero.gossipvideo.db.model.DownloadVideo;
import com.hero.gossipvideo.db.model.Video;
import com.hero.gossipvideo.store.DirHelper;
import com.hero.gossipvideo.ui.adapter.DownloadVideoAdapter;
import com.ltc.lib.net.api.HttpResult;
import com.ltc.lib.net.api.HttpWorker;
import com.ltc.lib.net.api.RequestPrepare;
import com.ltc.lib.net.download.DownloadListener;
import com.ltc.lib.net.download.DownloadPrepare;
import com.ltc.lib.net.download.DownloadWorker;
import com.ltc.lib.net.param.Method;
import com.ltc.lib.net.param.Range;
import com.ltc.lib.utils.JsonUtil;
import com.ltc.lib.utils.Utils;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/1/3.
 * 负责视频下载相关的类
 */
public class VideoDownloader {

    private final DownloadWorker mDownloadWorker;
    private final ConcurrentHashMap<String, Video> mDownloadingMap;

    private DownloadVideoAdapter mDownloadVideoAdapter;
    private long mLastUpdateAdapterTime = 0;

    private DownloadUrlTask mTask;

    private static final class VideoDownloaderHolder {
        private static final VideoDownloader sInstance = new VideoDownloader();
    }

    private VideoDownloader() {
        mDownloadWorker = new DownloadWorker();
        mDownloadWorker.setDownloadTaskCount(1);
        mDownloadingMap = new ConcurrentHashMap<String, Video>();
    }

    public void registerDownloadVideoAdapter(DownloadVideoAdapter adapter) {
        mDownloadVideoAdapter = adapter;
    }

    public void unregisterDownloadVideoAdapter() {
        mDownloadVideoAdapter = null;
    }

    public static VideoDownloader getInstance() {
        return VideoDownloaderHolder.sInstance;
    }

    public void download(Video video) {
        if (video != null) {
            if (saveToDb(video)) {
                doDownload(video);
            }
        }
    }

    private boolean saveToDb(Video video) {
        final DownloadVideo v = new DownloadVideo(video);
        if (!DownloadVideoDao.getInstance().isExist(v)) {
            DownloadVideoDao.getInstance().saveOrUpdate(v);
            return true;
        }
        return false;
    }

    public void downloadExistVideosIfNecessary() {

        List<DownloadVideo> hs = DownloadVideoDao.getInstance().getNotDownloadFinishVideos();
        if (Utils.isEmpty(hs)) {
            return;
        }

        for (DownloadVideo dv : hs) {
            doDownload(dv);
        }
    }

    private void doDownload(Video video) {
        if (mTask == null || mTask.getStatus() == AsyncTask.Status.FINISHED) {
            mTask = new DownloadUrlTask(video);
            mTask.execute();
        }
    }

    private void updateDownloadVideoStatus(DownloadPrepare prepare, int status) {
        final Video v = mDownloadingMap.get(prepare.url);
        if (v != null) {
            DownloadVideo dv = new DownloadVideo(v);
            dv.downloadStatus = status;
            DownloadVideoDao.getInstance().saveOrUpdate(dv);
        }
    }

    private void updateAdapterStatus(DownloadPrepare prepare, long total, long current, boolean finish) {
        final long cTime = System.currentTimeMillis();
        final long delta = cTime - mLastUpdateAdapterTime;
        if (delta > 1000 || finish) {
            mLastUpdateAdapterTime = cTime;
            Video v = mDownloadingMap.get(prepare.url);
            if (mDownloadVideoAdapter != null && v != null) {
                mDownloadVideoAdapter.notifyUpdate(v.id, total, current, finish);
            }
        }
    }

    //视频下载监听
    private class MDownloadListener implements DownloadListener {

        @Override
        public void startDownload(DownloadPrepare prepare) {
            updateDownloadVideoStatus(prepare, DownloadVideo.STATUS_DOWNLOADING);
        }

        @Override
        public void updateDownload(DownloadPrepare prepare, long total, long current) {
            updateAdapterStatus(prepare, total, current, total == current);
        }

        @Override
        public void finishDownload(DownloadPrepare prepare) {
            updateDownloadVideoStatus(prepare, DownloadVideo.STATUS_FINISH);
        }

        @Override
        public void cancelDownload(DownloadPrepare prepare) {

        }

        @Override
        public void downloadError(DownloadPrepare prepare) {

        }
    }

    //获取下载链接的任务类
    private class DownloadUrlTask extends AsyncTask<Void, Void, String> {

        private final Video mVideo;

        private DownloadUrlTask(Video video) {
            mVideo = video;
        }

        @Override
        protected String doInBackground(Void... params) {
            return ApiManager.getVideoPlayUrl(mVideo.extend);
        }

        @Override
        protected void onPostExecute(String playUrl) {
            if (!Utils.isEmpty(playUrl)) {
                File saveFile = new File(DirHelper.getVideoDownloadDir(), mVideo.id + ".video");
                DownloadPrepare prepare = DownloadPrepare.create(playUrl, saveFile, new MDownloadListener());
                //是否要断点续传
                File lf = getVideoInLocal(mVideo);
                if (lf != null) {
                    Range r = new Range();
                    r.set(lf.length(), -1);
                    prepare.range = r;
                }
                mDownloadWorker.download(prepare);
                mDownloadingMap.put(playUrl, mVideo);
            }
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
}
