package com.cleanonfire.sample.di;

import android.app.Application;
import android.content.Context;

import com.cleanonfire.api.interaction.UseCaseExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by heitorgianastasio on 13/11/17.
 */
@Module
public class ApplicationModule {
    Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    Context context(){return application;}

    @Singleton
    @Provides
    ThreadPoolExecutor poolExecutor(){
        return (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    @Singleton
    @Provides
    UseCaseExecutor useCaseExecutor(ThreadPoolExecutor executor){return new UseCaseExecutor(executor);}
}
