package com.cleanonfire.sample.di;

import android.app.Application;

/**
 * Created by heitorgianastasio on 13/11/17.
 */

public class DaggerInjector {
    private static ApplicationComponent component;

    public static ApplicationComponent getComponent() {
        if (component == null) {
            throw new RuntimeException("DaggerInjector was not initialized");
        }
        return component;
    }

    public static void init(Application application) {
       component = DaggerApplicationComponent
               .builder()
               .applicationModule(new ApplicationModule(application))
               .build();
    }
}
