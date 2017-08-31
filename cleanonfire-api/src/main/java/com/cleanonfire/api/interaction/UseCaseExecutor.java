package com.cleanonfire.api.interaction;

import android.os.Handler;
import android.os.Looper;

import com.cleanonfire.api.core.Mapper;
import com.cleanonfire.api.core.UseCase;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by heitor-12 on 08/08/17.
 */

public class UseCaseExecutor {
    ThreadPoolExecutor threadPoolExecutor;

    public UseCaseExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public <P, R, F> void execute(
            final UseCase<P, R> useCase,
            final P param,
            final OnResultListener<F> resultListener,
            OnErrorListener errorListener,
            final Mapper<R, F> mapper) {

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                useCase.execute(param,
                        new OnResultListener<R>() {
                            @Override
                            public void onResult(R r) {
                                final F finalResult = mapper.map(r);
                                postOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        resultListener.onResult(finalResult);
                                    }
                                });
                            }
                        }, new OnErrorListener() {
                            @Override
                            public void onError(final Throwable e) {
                                postOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        onError(e);
                                    }
                                });
                            }
                        });
            }
        });

    }

    private void postOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
