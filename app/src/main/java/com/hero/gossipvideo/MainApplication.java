package com.hero.gossipvideo;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.ltc.lib.BasicApplication;

/**
 * Created by Administrator on 2015/12/12.
 */
public class MainApplication extends BasicApplication {

    public void onCreate() {
        super.onCreate();
        sInstance = this;

        //facebook 开源图片框架
        Fresco.initialize(this);
    }
}
