package com.cleanonfire.api.interaction;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by heitorgianastasio on 17/10/17.
 */
public abstract class PostThread {
    private PostThread(){}

    static PostThread mainThread() {
        return new PostThread() {
            @Override
            public void post(Runnable runnable) {
                new Handler(Looper.getMainLooper()).post(runnable);
            }
        };
    }

    static PostThread workerThread(ThreadPoolExecutor executor) {
        return new PostThread() {
            @Override
            public void post(Runnable runnable) {
                executor.execute(runnable);
            }
        };
    }

    static PostThread currentThread() {
        return new PostThread() {
            @Override
            public void post(Runnable runnable) {
                runnable.run();
            }
        };
    }

    public abstract void post(Runnable runnable);
}
