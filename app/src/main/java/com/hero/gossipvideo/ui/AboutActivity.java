package com.hero.gossipvideo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.hero.gossipvideo.R;

/**
 * Created by Administrator on 2015/4/26.
 */
public class AboutActivity extends BaseActivity {

    public static void invoke(Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_about);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.v_toolbar);
        mToolbar.setTitle(R.string.nv_about);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
