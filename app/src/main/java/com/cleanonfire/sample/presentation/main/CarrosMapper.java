package com.cleanonfire.sample.presentation.main;

import android.text.format.DateFormat;

import com.cleanonfire.api.core.Mapper;
import com.cleanonfire.sample.core.carros.model.Carro;
import com.cleanonfire.sample.presentation.main.visualization.CarroVisualziation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heitorgianastasio on 13/11/17.
 */

public final class CarrosMapper {
    public static Mapper<Carro,CarroVisualziation> CARRO_TO_VISUALIZATION = new Mapper<Carro, CarroVisualziation>() {
        @Override
        public CarroVisualziation map(Carro carro) {
            if (carro == null) return null;

            CarroVisualziation visualziation = new CarroVisualziation();
            visualziation.setId(carro.getId());
            visualziation.setCor(carro.getCor());
            visualziation.setAno(DateFormat.format("yyyy",carro.getAno()).toString());
            if (carro.getModelo() != null) {
                visualziation.setModelo(carro.getModelo().getNome());
            }
            return visualziation;
        }
    };

    public static Mapper<List<Carro>,List<CarroVisualziation>> CARRO_TO_VISUALIZATION_LIST = new Mapper<List<Carro>, List<CarroVisualziation>>() {
        @Override
        public List<CarroVisualziation> map(List<Carro> carros) {
            if (carros == null) return null;

            List<CarroVisualziation> result = new ArrayList<>();
            for (Carro carro : carros) {
                result.add(CARRO_TO_VISUALIZATION.map(carro));
            }
            return result;
        }
    };
}
