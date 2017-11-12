package com.cleanonfire.sample.presentation.carros;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cleanonfire.R;
import com.cleanonfire.annotations.presentation.adapter.Bind;
import com.cleanonfire.annotations.presentation.adapter.VisualizationModel;
import com.cleanonfire.annotations.presentation.adapter.VisualizationModel.AdapterType;
import com.cleanonfire.api.presentation.adapter.bind.CompoundViewBinder;
import com.cleanonfire.api.presentation.adapter.bind.TextViewBinder;

/**
 * Created by heitorgianastasio on 26/10/17.
 */
@VisualizationModel(
        layoutId = R.layout.item_carro,
        adapterType = AdapterType.RECYCLERVIEW
)
public class CarroVisualization {
    private Long id;

    @Bind(layoutId = R.id.tvModelo, view = TextView.class, binder = TextViewBinder.class)
    private String modeloId;

    @Bind(layoutId = R.id.tvAno, view = Button.class, binder = TextViewBinder.class,clickable = true)
    private String cor;

    @Bind(layoutId = R.id.tvAno, view = CheckBox.class, binder = CompoundViewBinder.class,longClickable = true)
    private boolean fabricacao;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModeloId() {
        return modeloId;
    }

    public void setModeloId(String modeloId) {
        this.modeloId = modeloId;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public boolean getFabricacao() {
        return fabricacao;
    }

    public void setFabricacao(boolean fabricacao) {
        this.fabricacao = fabricacao;
    }
}
