package com.cleanonfire.sample.presentation.main.visualization;

import android.widget.TextView;

import com.cleanonfire.R;
import com.cleanonfire.annotations.presentation.adapter.Bind;
import com.cleanonfire.annotations.presentation.adapter.VisualizationModel;
import com.cleanonfire.api.presentation.adapter.bind.CompoundViewBinder;
import com.cleanonfire.api.presentation.adapter.bind.TextViewBinder;

import static com.cleanonfire.annotations.presentation.adapter.VisualizationModel.AdapterType.LISTVIEW;
import static com.cleanonfire.annotations.presentation.adapter.VisualizationModel.AdapterType.RECYCLERVIEW;

/**
 * Created by heitorgianastasio on 13/11/17.
 */
@VisualizationModel(layoutId = R.layout.item_carro,adapterType = RECYCLERVIEW)
public class CarroVisualziation {

    private long id;
    @Bind(layoutId = R.id.tvModelo, view = TextView.class, binder = TextViewBinder.class)
    private String modelo;

    @Bind(layoutId = R.id.tvAno, view = TextView.class, binder = TextViewBinder.class)
    private String ano;

    @Bind(layoutId = R.id.tvCor, view = TextView.class, binder = TextViewBinder.class)
    private String cor;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }
}
