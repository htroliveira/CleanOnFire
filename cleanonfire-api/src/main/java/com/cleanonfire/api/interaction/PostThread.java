package com.cleanonfire.api.interaction;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by heitorgianastasio on 17/10/17.
 */
public interface PostThread {


    static PostThread MAIN_THREAD() {
        return runnable -> new Handler(Looper.getMainLooper()).post(runnable);
    }

    static PostThread WORKER_THREAD(ThreadPoolExecutor executor) {
        return executor::execute;
    }

    static PostThread CURRENT_THREAD() {
        return Runnable::run;
    }

    void post(Runnable runnable);
}
