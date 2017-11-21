package com.cleanonfire.sample.data.carros;

import com.cleanonfire.sample.core.carros.model.Carro;
import com.cleanonfire.sample.core.carros.repository.CarrosRepository;
import com.cleanonfire.sample.core.modelos.model.Modelo;
import com.cleanonfire.sample.data.carros.mappers.CarrosMappers;
import com.cleanonfire.sample.data.db.CarroEntityCleanDAO;
import com.generated.cleanonfire.db.CleanOnFireDB;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by heitorgianastasio on 13/11/17.
 */

public class CarrosRepositoryImpl implements CarrosRepository {
    CarroEntityCleanDAO dao = CleanOnFireDB.get().getCarroEntityCleanDAO();

    @Inject
    public CarrosRepositoryImpl() {
    }

    @Override
    public List<Carro> getAll() {
        CleanOnFireDB.get()
                .getCarroEntityCleanDAO()
                .save();

        return CarrosMappers.mapEntityListToModelList(CarroEntityCleanDAO::getModelo).map(dao.getAll());
        /*return CleanOnFireDB.get().rawQuery(cursorReader -> {
                    Modelo modelo = new Modelo();
                    modelo.setNome(cursorReader.getString("nome"));
                    modelo.setId(cursorReader.getInt("modelo_id"));
                    Carro carro = new Carro();
                    carro.setAno(new Date(cursorReader.getLong("fabricacao")));
                    carro.setId(cursorReader.getLong("_id"));
                    carro.setCor(cursorReader.getString("cor"));
                    carro.setModelo(modelo);
                    return new Carro();
                },
                "SELECT * FROM carros INNER JOIN modelo on carros.modelo_id = modelo.id "
        );*/
    }

    @Override
    public Carro getById(long id) {
        return CarrosMappers.mapEntityToModel(CarroEntityCleanDAO::getModelo).map(dao.getById(id));
    }

}
