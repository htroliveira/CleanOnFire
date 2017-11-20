package com.cleanonfire.sample.data.carros.mappers;

import com.cleanonfire.api.core.Mapper;
import com.cleanonfire.sample.core.carros.model.Carro;
import com.cleanonfire.sample.core.modelos.model.Modelo;
import com.cleanonfire.sample.data.db.CarroEntity;
import com.cleanonfire.sample.data.db.ModeloEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heitorgianastasio on 13/11/17.
 */

public final class CarrosMappers {
    public static Mapper<Carro, CarroEntity> CARRO_TO_ENTITY = carro -> {
        if (carro == null) return null;
        CarroEntity entity = new CarroEntity();
        entity.setId(carro.getId());
        entity.setCor(carro.getCor());
        entity.setFabricacao(carro.getAno());
        if (carro.getModelo() != null) {
            entity.setModeloId(carro.getModelo().getId());
        }
        return entity;
    };


    public static Mapper<List<CarroEntity>, List<Carro>> mapEntityListToModelList(ModeloByCar modeloByCar) {

        return carros -> {
            if (carros == null) return null;
            List<Carro> result = new ArrayList<>();
            for (CarroEntity carro : carros) {
                result.add(mapEntityToModel(modeloByCar).map(carro));
            }
            return result;
        };
    }

    public static Mapper<ModeloEntity, Modelo> ENTITY_TO_MODELO = modeloEntity -> {
        Modelo modelo = new Modelo();
        modelo.setId(modeloEntity.getId());
        modelo.setNome(modeloEntity.getName());
        return modelo;
    };

    public static Mapper<CarroEntity, Carro> mapEntityToModel(ModeloByCar modeloByCar) {
        return carro -> {
            if (carro == null) return null;
            Carro entity = new Carro();
            if (modeloByCar != null)
                entity.setModelo(ENTITY_TO_MODELO.map(modeloByCar.getModelo(carro)));
            entity.setId(carro.getId());
            entity.setCor(carro.getCor());
            entity.setAno(carro.getFabricacao());
            return entity;
        };
    }

    public interface ModeloByCar{
        ModeloEntity getModelo(CarroEntity carroEntity);
    }

}
