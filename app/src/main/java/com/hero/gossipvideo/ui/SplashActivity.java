package com.hero.gossipvideo.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hero.gossipvideo.R;
import com.hero.gossipvideo.ads.gdt.GDTAgent;
import com.hero.gossipvideo.settings.Settings;
import com.hero.gossipvideo.utils.PreUtils;
import com.qq.e.ads.splash.SplashADListener;

/**
 * Created by Administrator on 2015/12/12.
 */
public class SplashActivity extends BaseActivity {

    private static final String KEY_IN_SPLASH_COUNT = "int_splash_count";

    private ImageView mImageView;
    private RelativeLayout mAdView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        Settings.init(this);

        initView();

        //用户只是进入app一次才显示广告
        final int inCount = PreUtils.getInt(KEY_IN_SPLASH_COUNT, 0);
        if (inCount >= 1) {
            mAdView.setVisibility(View.VISIBLE);
            initAd();
        } else {
            PreUtils.putInt(KEY_IN_SPLASH_COUNT, inCount+1);
            initAnim();
        }
    }

    @Override
    public void onBackPressed() {
        //
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.image);
        mAdView = (RelativeLayout) findViewById(R.id.v_ad);
    }

    private void next() {
        MainActivity.invoke(SplashActivity.this);
        finish();
    }

    private void initAnim() {

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(mImageView, "scaleX",
                1.0f, 1.15f, 1.0f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(mImageView, "scaleY",
                1.0f, 1.15f, 1.0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(3000);
        animSet.setInterpolator(new LinearInterpolator());
        //两个动画同时执行
        animSet.playTogether(anim1, anim2);
        animSet.start();

        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //do nothing
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                next();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //do nothing
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //do nothing
            }
        });
    }

    private void initAd() {
        GDTAgent.getInstance().showSplashAD(this, mAdView, new SplashADListener() {
            @Override
            public void onADDismissed() {
                next();
            }

            @Override
            public void onNoAD(int i) {
                next();
            }

            @Override
            public void onADPresent() {
                //
            }
        });
    }
}
