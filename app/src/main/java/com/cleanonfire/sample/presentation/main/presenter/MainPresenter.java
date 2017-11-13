package com.cleanonfire.sample.presentation.main.presenter;

import com.cleanonfire.api.interaction.OnResultListener;
import com.cleanonfire.api.interaction.UseCaseExecutor;
import com.cleanonfire.api.presentation.mvp.BasePresenter;
import com.cleanonfire.sample.core.carros.usecase.GetAllCarros;
import com.cleanonfire.sample.presentation.base.di.PerView;
import com.cleanonfire.sample.presentation.main.CarrosMapper;
import com.cleanonfire.sample.presentation.main.viewcontract.MainViewContract;
import com.cleanonfire.sample.presentation.main.visualization.CarroVisualziation;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by heitorgianastasio on 13/11/17.
 */
@PerView
public class MainPresenter extends BasePresenter<MainViewContract> {
    GetAllCarros getAllCarros;

    @Inject
    public MainPresenter(UseCaseExecutor executor, GetAllCarros getAllCarros) {
        super(executor);
        this.getAllCarros = getAllCarros;
    }

    public void getCarros() {
        executeUseCase(getAllCarros)
                .transformResult(CarrosMapper.CARRO_TO_VISUALIZATION_LIST)
                .onResult(new OnResultListener<List<CarroVisualziation>>() {
                    @Override
                    public void onResult(List<CarroVisualziation> carroVisualziations) {
                        getView().renderCarros(carroVisualziations);
                    }
                })
                .run(getExecutor());
    }

}
