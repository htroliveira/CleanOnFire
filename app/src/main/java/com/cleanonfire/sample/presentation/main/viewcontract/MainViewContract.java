package com.cleanonfire.sample.presentation.main.viewcontract;

import com.cleanonfire.api.presentation.mvp.BaseViewContract;
import com.cleanonfire.sample.presentation.main.visualization.CarroVisualziation;

import java.util.List;

/**
 * Created by heitorgianastasio on 13/11/17.
 */

public interface MainViewContract extends BaseViewContract {
    void renderCarros(
            List<CarroVisualziation> carroVisualziations
    );

}
