package com.cleanonfire.api.interaction;

import com.cleanonfire.api.core.Mapper;
import com.cleanonfire.api.core.UseCase;

/**
 * Created by heitorgianastasio on 18/09/17.
 */

public final class UseCaseExecution<P, R, TP, TR> {
    UseCase<P, R> useCase;
    OnResultListener<TR> resultListener;
    OnErrorListener errorListener;
    Mapper<TP, P> paramMaper;
    Mapper<R, TR> resultMaper;
    TP params;

    private UseCaseExecution() {}

    public UseCaseExecution<P, R, TP, TR> with(TP params) {
        this.params = params;
        return this;
    }


    public <T> UseCaseExecution<P, R, TP, T> transformResult(Mapper<R, T> mapper) {
        UseCaseExecution<P, R, TP, T> result = (UseCaseExecution<P, R, TP, T>) this;
        result.resultMaper = mapper;
        return result;
    }

    public <T> UseCaseExecution<P, R, T, TR> transformParams(Mapper<T, P> mapper) {
        UseCaseExecution<P, R, T, TR> result = (UseCaseExecution<P, R, T, TR>) this;
        result.paramMaper = mapper;
        return result;
    }

    public UseCaseExecution<P, R, TP, TR> onResult(OnResultListener<TR> resultListener) {
        this.resultListener = resultListener;
        return this;
    }


    public UseCaseExecution<P, R, TP, TR> onError(OnErrorListener errorListener) {
        this.errorListener = errorListener;
        return this;
    }


    public static <P, R> UseCaseExecution<P, R, P, R> create(UseCase<P, R> useCase) {
        UseCaseExecution<P, R, P, R> execution = new UseCaseExecution<>();
        execution.useCase = useCase;
        return execution;
    }

    public void run(UseCaseExecutor executor) {
        if (params == null || useCase == null)
            throw new RuntimeException();

        if(paramMaper!=null && resultMaper!=null)
            executor.executeTransformingResultsAndParams(useCase,params,resultListener,errorListener,resultMaper,paramMaper);
        else if (paramMaper!=null)
            executor.executeTransformingParams(useCase,params,(OnResultListener<R>)resultListener,errorListener,paramMaper);
        else if (resultMaper != null)
            executor.executeTransformingResults(useCase,(P)params,resultListener,errorListener,resultMaper);
        else
            executor.execute(useCase,(P)params,(OnResultListener<R>)resultListener,errorListener);
    }


}
