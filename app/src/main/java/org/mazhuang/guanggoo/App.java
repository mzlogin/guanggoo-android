package org.mazhuang.guanggoo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.tencent.bugly.Bugly;

import org.mazhuang.guanggoo.util.ConfigUtil;

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

        if (ConfigUtil.isDebug() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectNonSdkApiUsage()
                    .penaltyLog()
                    .build());
        }

        mGlobal = new GlobalData();

        Bugly.init(getApplicationContext(), "6ace1b5b58", false);
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
