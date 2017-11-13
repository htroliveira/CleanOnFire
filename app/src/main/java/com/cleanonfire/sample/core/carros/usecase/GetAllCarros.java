package com.cleanonfire.sample.core.carros.usecase;

import com.cleanonfire.api.core.UseCase;
import com.cleanonfire.api.interaction.OnErrorListener;
import com.cleanonfire.api.interaction.OnResultListener;
import com.cleanonfire.sample.core.carros.model.Carro;
import com.cleanonfire.sample.core.carros.repository.CarrosRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by heitorgianastasio on 13/11/17.
 */

public class GetAllCarros implements UseCase<Void,List<Carro>> {
    CarrosRepository repository;

    @Inject
    public GetAllCarros(CarrosRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Void aVoid, OnResultListener<List<Carro>> resultListener, OnErrorListener errorListener) {
        try {
            List<Carro> carros = repository.getAll();
            resultListener.onResult(carros);
        } catch (Exception e){
            errorListener.onError(e);
        }
    }
}
