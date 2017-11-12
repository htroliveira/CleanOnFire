package com.cleanonfire.api.interaction;



import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;



/**
 * Created by heitor-12 on 02/08/17.
 */
public class UseCaseExecutor {
    private ThreadPoolExecutor executor;
    private Map<UseCaseExecution, Future> executionFutureMap = new HashMap<>();

    public UseCaseExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    public <D> void run(UseCaseExecution<?, ?, ?, D> target, UseCaseRunnable<D> runnable, PostThread postThread) {
        Future future = executor.submit(() -> runnable.runUseCase(
                result -> postThread.post(() -> target.resultListener.onResult(result)),
                e -> postThread.post(() -> target.errorListener.onError(e)))
        );

        executionFutureMap.put(target, future);

    }

    public boolean stop(UseCaseExecution execution) {
        Future future = executionFutureMap.remove(execution);
        if (future == null) return false;

        boolean futureIsCanceled = false;
        while (!futureIsCanceled && !future.isDone()) {

            futureIsCanceled = future.cancel(true);
        }
        return true;
    }


    interface UseCaseRunnable<D> {
        void runUseCase(OnResultListener<D> resultListener, OnErrorListener errorListener);
    }
}
