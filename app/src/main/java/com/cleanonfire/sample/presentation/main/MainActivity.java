package com.cleanonfire.sample.presentation.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cleanonfire.R;
import com.cleanonfire.sample.di.DaggerInjector;
import com.cleanonfire.sample.presentation.base.BaseActivity;
import com.cleanonfire.sample.presentation.base.di.DaggerViewComponent;
import com.cleanonfire.sample.presentation.main.presenter.MainPresenter;
import com.cleanonfire.sample.presentation.main.viewcontract.MainViewContract;
import com.cleanonfire.sample.presentation.main.visualization.CarroVisualziation;
import com.cleanonfire.sample.presentation.main.visualization.CarroVisualziationAdapter;
import com.cleanonfire.sample.presentation.main.visualization.CarroVisualziationViewHolderBinder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by heitorgianastasio on 02/10/17.
 */
public class MainActivity extends BaseActivity implements MainViewContract {

    View emptyLayout;
    RecyclerView rvCarros;
    List<CarroVisualziation> carros = new ArrayList<>();
    CarroVisualziationAdapter adapter = new CarroVisualziationAdapter(carros, this, new CarroVisualziationViewHolderBinder());

    @Inject
    MainPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_main);
        presenter.attachView(this);
        emptyLayout = findViewById(R.id.emptyLayout);
        rvCarros = findViewById(R.id.rvCarros);
        rvCarros.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvCarros.setAdapter(adapter);
        presenter.getCarros();

    }

    @Override
    public void renderCarros(List<CarroVisualziation> carroVisualziations) {
        carros.addAll(carroVisualziations);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        presenter.dettachView();
        super.onDestroy();
    }

    @Override
    protected void injectDependencies() {
        DaggerViewComponent.builder()
                .applicationComponent(DaggerInjector.getComponent())
                .build()
                .inject(this);
    }
}
