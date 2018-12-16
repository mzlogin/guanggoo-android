package org.mazhuang.guanggoo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.multidex.MultiDexApplication;

import com.tencent.bugly.Bugly;

/**
 *
 * @author mazhuang
 * @date 2017/9/16
 */

public class App extends MultiDexApplication {

    private static App sInstance;
    public GlobalData mGlobal;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        Bugly.init(getApplicationContext(), "6ace1b5b58", false);

        mGlobal = new GlobalData();
    }

    public static App getInstance() {
        return sInstance;
    }

    public static class GlobalData {
        public MutableLiveData<Boolean> hasNotifications = new MutableLiveData<>();

        {
            hasNotifications.setValue(false);
        }
    }
}
