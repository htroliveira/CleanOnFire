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
    public static Mapper<Carro,CarroEntity> CARRO_TO_ENTITY = new Mapper<Carro, CarroEntity>() {
        @Override
        public CarroEntity map(Carro carro) {
            if (carro == null) return null;
            CarroEntity entity = new CarroEntity();
            entity.setId(carro.getId());
            entity.setCor(carro.getCor());
            entity.setFabricacao(carro.getAno());
            if (carro.getModelo() != null) {
                entity.setModeloId(carro.getModelo().getId());
            }
            return entity;
        }
    };

    public static Mapper<CarroEntity,Carro> ENTITY_TO_CARRO = new Mapper<CarroEntity, Carro>() {
        @Override
        public Carro map(CarroEntity carro) {
            if (carro == null) return null;

            Carro entity = new Carro();
            entity.setId(carro.getId());
            entity.setCor(carro.getCor());
            entity.setAno(carro.getFabricacao());
            return entity;
        }
    };

    public static Mapper<List<CarroEntity>,List<Carro>> ENTITY_LIST_TO_CARRO_LIST = new Mapper<List<CarroEntity>,List<Carro>>() {
        @Override
        public List<Carro> map(List<CarroEntity> carros) {
            if (carros == null) return null;
            List<Carro> result = new ArrayList<>();
            for (CarroEntity carro : carros) {
                result.add(ENTITY_TO_CARRO.map(carro));
            }
            return result;
        }
    };

    public static Mapper<ModeloEntity,Modelo> ENTITY_TO_MODELO = new Mapper<ModeloEntity, Modelo>() {
        @Override
        public Modelo map(ModeloEntity modeloEntity) {
            Modelo modelo = new Modelo();
            modelo.setId(modeloEntity.getId());
            modelo.setNome(modeloEntity.getName());
            return modelo;
        }
    };

}
