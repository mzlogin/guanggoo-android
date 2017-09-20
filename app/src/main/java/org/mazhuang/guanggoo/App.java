package org.mazhuang.guanggoo;

import android.app.Application;

import com.tencent.bugly.Bugly;

/**
 * Created by mazhuang on 2017/9/16.
 */

public class App extends Application {

    private static Application sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        Bugly.init(getApplicationContext(), "6ace1b5b58", false);
    }

    public static Application getInstance() {
        return sInstance;
    }
}
