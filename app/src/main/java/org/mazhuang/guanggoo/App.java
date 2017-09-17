package org.mazhuang.guanggoo;

import android.app.Application;

/**
 * Created by mazhuang on 2017/9/16.
 */

public class App extends Application {

    private static Application sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
    }

    public static Application getInstance() {
        return sInstance;
    }
}
