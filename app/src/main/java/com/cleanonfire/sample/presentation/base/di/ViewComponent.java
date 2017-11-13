package com.cleanonfire.sample.presentation.base.di;

import com.cleanonfire.sample.data.di.DataModule;
import com.cleanonfire.sample.di.ApplicationComponent;
import com.cleanonfire.sample.presentation.main.MainActivity;

import dagger.Component;

/**
 * Created by heitorgianastasio on 13/11/17.
 */
@PerView
@Component(
        modules = DataModule.class,
        dependencies = ApplicationComponent.class
)
public interface ViewComponent {
    void inject(MainActivity activity);
}
