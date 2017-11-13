package com.cleanonfire.sample.data.di;

import com.cleanonfire.sample.core.carros.repository.CarrosRepository;
import com.cleanonfire.sample.data.carros.CarrosRepositoryImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by heitorgianastasio on 13/11/17.
 */
@Module
public abstract class DataModule {
    @Binds
    public abstract CarrosRepository carrosRepository(CarrosRepositoryImpl repository);
}
