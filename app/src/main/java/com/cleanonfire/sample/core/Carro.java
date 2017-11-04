package com.cleanonfire.sample.core;

import com.cleanonfire.annotations.data.db.FieldInfo;
import com.cleanonfire.annotations.data.db.ForeignKey;
import com.cleanonfire.annotations.data.db.IgnoreField;
import com.cleanonfire.annotations.data.db.PrimaryKey;
import com.cleanonfire.annotations.data.db.Table;

import java.util.Date;

import static com.cleanonfire.annotations.data.db.ForeignKey.DeletePolicy.ON_DELETE_CASCADE;
import static com.cleanonfire.annotations.data.db.ForeignKey.UpdatePolicy.ON_UPDATE_RESTRICT;

/**
 * Created by heitorgianastasio on 26/10/17.
 */
@Table(tableName = "carros")
public class Carro {
    @PrimaryKey(autoincrement = true)
    @FieldInfo(columnName = "idDoCarro")
    private int id;

    @ForeignKey(target = Modelo.class, name = "modelo", update = ON_UPDATE_RESTRICT, delete = ON_DELETE_CASCADE)
    @FieldInfo(columnName = "modelo_id")
    private long modeloId;

    private String cor;

    private Date fabricacao;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getModeloId() {
        return modeloId;
    }

    public void setModeloId(long modeloId) {
        this.modeloId = modeloId;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Date getFabricacao() {
        return fabricacao;
    }

    public void setFabricacao(Date fabricacao) {
        this.fabricacao = fabricacao;
    }
}
