package com.hero.gossipvideo.config;

import android.content.res.Resources;
import android.provider.Settings;
import android.util.DisplayMetrics;

import com.hero.gossipvideo.MainApplication;
import com.ltc.lib.utils.NetworkUtil;
import com.ltc.lib.utils.Utils;

/**
 * Created by longtc on 16-3-17.
 */
public class AndroidParam {

    public static String getMac() {
        String mac = NetworkUtil.getLocalMacAddress(MainApplication.getInstance());
        if (!Utils.isEmpty(mac)) {
            return mac;
        } else {
            return "020000000000";
        }
    }

    public static String getAndroidId() {
        String id = Settings.Secure.getString(
                MainApplication.getInstance().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return id == null ? "" : id;
    }

    public static String getUserAgent() {
        return System.getProperty("http.agent");
    }

    public static String getScreenSize() {
        Resources rs = MainApplication.getInstance().getResources();
        DisplayMetrics dm = rs.getDisplayMetrics();
        return dm.widthPixels + "X" + dm.heightPixels;
    }
}
