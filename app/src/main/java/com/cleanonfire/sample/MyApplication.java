package com.cleanonfire.sample;

import android.app.Application;

import com.cleanonfire.sample.di.DaggerInjector;
import com.generated.cleanonfire.db.CleanOnFireDB;

/**
 * Created by heitorgianastasio on 13/11/17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DaggerInjector.init(this);
        CleanOnFireDB.init(this);
    }
}
