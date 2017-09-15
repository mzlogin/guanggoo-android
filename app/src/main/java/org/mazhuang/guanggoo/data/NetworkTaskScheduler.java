package org.mazhuang.guanggoo.data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mazhuang on 2017/9/16.
 */

public class NetworkTaskScheduler {

    private ExecutorService mExecutor;

    private static class InstanceHolder {
        private static NetworkTaskScheduler sInstance = new NetworkTaskScheduler();
    }

    public static NetworkTaskScheduler getInstance() {
        return InstanceHolder.sInstance;
    }

    private NetworkTaskScheduler() {
        mExecutor = Executors.newSingleThreadExecutor();
    }

    public void execute(Runnable runnable) {
        mExecutor.execute(runnable);
    }
}
