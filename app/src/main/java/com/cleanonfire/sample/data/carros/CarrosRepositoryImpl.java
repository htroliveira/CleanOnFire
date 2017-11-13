package com.cleanonfire.sample.data.carros;

import com.cleanonfire.sample.core.carros.model.Carro;
import com.cleanonfire.sample.core.carros.repository.CarrosRepository;
import com.cleanonfire.sample.data.carros.mappers.CarrosMappers;
import com.cleanonfire.sample.data.db.CarroEntity;
import com.cleanonfire.sample.data.db.CarroEntityCleanDAO;
import com.cleanonfire.sample.data.db.ModeloEntityCleanDAO;
import com.cleanonfire.sample.presentation.main.CarrosMapper;
import com.generated.cleanonfire.db.CleanOnFireDB;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by heitorgianastasio on 13/11/17.
 */

public class CarrosRepositoryImpl implements CarrosRepository {
    CarroEntityCleanDAO dao = CleanOnFireDB.get().getCarroEntityCleanDAO();
    ModeloEntityCleanDAO modeloDao = CleanOnFireDB.get().getModeloEntityCleanDAO();
    @Inject
    public CarrosRepositoryImpl() {}

    @Override
    public List<Carro> getAll() {
        List<Carro> carros = new ArrayList<>();
        for (CarroEntity entity : dao.getAll()) {
            Carro carro = CarrosMappers.ENTITY_TO_CARRO.map(entity);
            carro.setModelo(CarrosMappers.ENTITY_TO_MODELO.map(modeloDao.getById(entity.getModeloId())));
            carros.add(carro);
        }
        return carros;
    }

    @Override
    public Carro getById(long id) {
        return CarrosMappers.ENTITY_TO_CARRO.map(dao.getById(id));
    }
}
