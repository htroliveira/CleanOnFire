package com.cleanonfire.api.interaction;

import android.os.Handler;
import android.os.Looper;

import com.cleanonfire.api.core.Mapper;
import com.cleanonfire.api.core.UseCase;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * Created by heitor-12 on 02/08/17.
 */
public class UseCaseExecutor {
    ThreadPoolExecutor executor;

    public UseCaseExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    public <Param, Result> void execute(UseCase<Param, Result> useCase, Param params, OnResultListener<Result> onResultListener, OnErrorListener errorListener) {

        executeUseCase(useCase, params,
                result -> executeOnUiThread(() -> onResultListener.onResult(result)),
                error -> executeOnUiThread(() -> errorListener.onError(error))
        );
    }

    public <Param, Result, TransformedResult> void executeTransformingResults(UseCase<Param, Result> useCase, Param params, OnResultListener<TransformedResult> onResultListener, OnErrorListener errorListener, Mapper<Result, TransformedResult> mapper) {
        executeUseCase(useCase, params,
                result -> {
                    try {
                        TransformedResult transformedResult = mapper.map(result);
                        executeOnUiThread(() -> onResultListener.onResult(transformedResult));
                    } catch (Throwable e) {
                        executeOnUiThread(() -> errorListener.onError(e));
                    }
                },
                error -> executeOnUiThread(() -> errorListener.onError(error))
        );
    }

    public <UseCaseParam, Param, Result> void executeTransformingParams(UseCase<UseCaseParam, Result> useCase, Param params, OnResultListener<Result> onResultListener, OnErrorListener errorListener, Mapper<Param, UseCaseParam> paramMapper) {
        executeUseCase(useCase, params,
                result -> {
                    try {
                        executeOnUiThread(() -> onResultListener.onResult(result));
                    } catch (Throwable e) {
                        executeOnUiThread(() -> errorListener.onError(e));
                    }
                },
                error -> executeOnUiThread(() -> errorListener.onError(error)),
                paramMapper
        );
    }

    public <UseCaseParam, Param, Result, TransformedResult> void executeTransformingResultsAndParams(UseCase<UseCaseParam, Result> useCase, Param params, OnResultListener<TransformedResult> onResultListener, OnErrorListener errorListener, Mapper<Result, TransformedResult> resultMapper, Mapper<Param, UseCaseParam> paramMapper) {
        executeUseCase(useCase, params,
                result -> {
                    try {
                        TransformedResult transformedResult = resultMapper.map(result);
                        executeOnUiThread(() -> onResultListener.onResult(transformedResult));
                    } catch (Throwable e) {
                        executeOnUiThread(() -> errorListener.onError(e));
                    }
                },
                error -> executeOnUiThread(() -> errorListener.onError(error)),
                paramMapper
        );
    }

    private <Param, Result> void executeUseCase(UseCase<Param, Result> useCase, Param params, OnResultListener<Result> resultListener, OnErrorListener errorListener) {
        executor.execute(() -> useCase.execute(params, resultListener, errorListener));
    }

    private <UseCaseParam, Param, Result> void executeUseCase(UseCase<UseCaseParam, Result> useCase, Param params, OnResultListener<Result> resultListener, OnErrorListener errorListener, Mapper<Param, UseCaseParam> paramMapper) {
        executor.execute(() -> useCase.execute(paramMapper.map(params), resultListener, errorListener));
    }

    private void executeOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
