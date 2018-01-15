package org.mazhuang.guanggoo.base;

import android.support.annotation.NonNull;

import java.util.Locale;
import java.util.concurrent.ThreadFactory;

/**
 * @author mazhuang
 * @date 2018/1/14
 */

public class NamedThreadFactory implements ThreadFactory {

    private String mThreadNamePrefix;
    private int mCount = 0;

    public NamedThreadFactory(String threadNamePrefix) {
        mThreadNamePrefix = threadNamePrefix;
    }

    @Override
    public Thread newThread(@NonNull Runnable r) {
        Thread thread = new Thread(r);

        mCount++;

        thread.setName(String.format(Locale.CHINA, "%s - %d", mThreadNamePrefix, mCount));

        return thread;
    }
}
