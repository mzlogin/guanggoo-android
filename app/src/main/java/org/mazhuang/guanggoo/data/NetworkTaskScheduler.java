package org.mazhuang.guanggoo.data;

import com.tencent.bugly.crashreport.CrashReport;
import org.mazhuang.guanggoo.base.NamedThreadFactory;
import org.mazhuang.guanggoo.data.task.BaseTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author mazhuang
 * @date 2017/9/16
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
        mExecutor = new ThreadPoolExecutor(
                1,
                1,
                5,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new NamedThreadFactory(NetworkTaskScheduler.class.getSimpleName())
        );
    }

    public void execute(final Runnable runnable) {
        if (runnable == null) {
            return;
        }

        try {
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        runnable.run();
                    } catch (Throwable e) {
                        // 后台任务的任何异常都不应该让应用崩溃
                        e.printStackTrace();
                        if (runnable instanceof BaseTask) {
                            ((BaseTask<?>) runnable).handleUnexpectedError(e);
                        } else {
                            CrashReport.postCatchedException(e);
                        }
                    }
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
