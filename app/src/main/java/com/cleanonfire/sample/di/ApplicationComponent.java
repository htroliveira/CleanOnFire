package com.cleanonfire.sample.di;

import android.content.Context;

import com.cleanonfire.api.interaction.UseCaseExecutor;

import java.util.concurrent.ThreadPoolExecutor;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by heitorgianastasio on 13/11/17.
 */
@Singleton
@Component(
        modules = ApplicationModule.class
)
public interface ApplicationComponent {
    UseCaseExecutor useCaseExecutor();
    ThreadPoolExecutor threadPoolExecutor();
    Context context();
}
