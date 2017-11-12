package com.cleanonfire.api.presentation.mvp;

import android.support.annotation.CallSuper;

import com.cleanonfire.api.core.UseCase;
import com.cleanonfire.api.interaction.UseCaseExecution;
import com.cleanonfire.api.interaction.UseCaseExecutor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by heitorgianastasio on 04/11/17.
 */

public class BasePresenter<T extends BaseViewContract> {
    private UseCaseExecutor executor;
    private T view;
    private final List<UseCaseExecution> executions = new ArrayList<>();


    public BasePresenter(UseCaseExecutor executor) {
        this.executor = executor;
    }

    public final T getView() {
        return view;
    }

    @CallSuper
    public void attachView(T view) {
        this.view = view;
    }

    @CallSuper
    public void dettachView() {
        this.view = null;
        synchronized (executions) {
            Iterator<UseCaseExecution> iter = executions.iterator();
            while (iter.hasNext()) {
                UseCaseExecution useCaseExecution = iter.next();

                if (useCaseExecution.stop(executor))
                    iter.remove();
            }
        }
    }


    protected <P,R>UseCaseExecution<P,R,P,R> executeUseCase(UseCase<P,R> useCase){
        UseCaseExecution<P,R,P,R> execution = UseCaseExecution.create(useCase);
        executions.add(execution);
        return execution;
    }



    protected final UseCaseExecutor getExecutor(){
        return executor;
    }}
