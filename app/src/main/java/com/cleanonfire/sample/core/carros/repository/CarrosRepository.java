package com.cleanonfire.sample.core.carros.repository;

import com.cleanonfire.sample.core.carros.model.Carro;

import java.util.List;

/**
 * Created by heitorgianastasio on 13/11/17.
 */

public interface CarrosRepository {
    List<Carro> getAll();
    Carro getById(long id);
}
