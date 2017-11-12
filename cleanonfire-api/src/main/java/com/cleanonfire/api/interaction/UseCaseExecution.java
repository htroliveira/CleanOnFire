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
    Mapper<TP, P> paramMapper;
    Mapper<R, TR> resultMapper;
    PostThread postThread;
    TP params;


    private UseCaseExecution() {}

    public static <P, R> UseCaseExecution<P, R, P, R> create(UseCase<P, R> useCase) {
        UseCaseExecution<P, R, P, R> execution = new UseCaseExecution<>();
        execution.useCase = useCase;
        return execution;
    }

    public UseCaseExecution<P, R, TP, TR> with(TP params) {
        this.params = params;
        return this;
    }

    public <T> UseCaseExecution<P, R, TP, T> transformResult(Mapper<R, T> mapper) {
        this.resultListener = null;
        this.resultMapper = null;
        UseCaseExecution<P, R, TP, T> transformed = (UseCaseExecution<P, R, TP, T>) this;
        transformed.resultMapper = mapper;
        return transformed;
    }

    public <T> UseCaseExecution<P, R, T, TR> transformParams(Mapper<T, P> mapper) {
        this.params = null;
        this.paramMapper = null;
        UseCaseExecution<P, R, T, TR> transformed = (UseCaseExecution<P, R, T, TR>)this;
        transformed.paramMapper = mapper;
        return transformed;
    }

    public UseCaseExecution<P, R, TP, TR> onResult(OnResultListener<TR> resultListener) {
        this.resultListener = resultListener;
        return this;
    }

    public UseCaseExecution<P, R, TP, TR> onError(OnErrorListener errorListener) {
        this.errorListener = errorListener;
        return this;
    }

    public UseCaseExecution<P, R, TP, TR>  on(PostThread postThread) {
        this.postThread = postThread;
        return this;
    }

    public boolean stop(UseCaseExecutor executor){
        return executor.stop(this);
    }
    public void run(UseCaseExecutor executor) {
        if (useCase == null)
            throw new RuntimeException("You cannot run a null UseCase");
        PostThread postThread = this.postThread!=null? this.postThread : PostThread.MAIN_THREAD();

        executor.run(this, (listener,errorListener) -> {

            try {
                P params = paramMapper != null ? paramMapper.map(this.params) : (P) this.params;
                useCase.execute(params, result -> {
                    TR finalResult = resultMapper != null ? resultMapper.map(result) : (TR) result;
                    listener.onResult(finalResult);
                }, errorListener);

            } catch (ClassCastException e) {
                throw new RuntimeException(e);
            }catch (Exception e){
                errorListener.onError(e);
            }
        },postThread);

    }


}
