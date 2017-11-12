package com.cleanonfire.sample.data.db;

import com.cleanonfire.annotations.data.db.FieldInfo;
import com.cleanonfire.annotations.data.db.ForeignKey;
import com.cleanonfire.annotations.data.db.PrimaryKey;
import com.cleanonfire.annotations.data.db.Table;

import java.util.Date;

import static com.cleanonfire.annotations.data.db.ForeignKey.ForeignKeyPolicy.CASCADE;
import static com.cleanonfire.annotations.data.db.ForeignKey.ForeignKeyPolicy.RESTRICT;

/**
 * Created by heitorgianastasio on 26/10/17.
 */
@Table(tableName = "carros")
public class CarroEntity {
    @PrimaryKey(autoincrement = true)
    @FieldInfo(columnName = "idDoCarro")
    private Long id;

    @ForeignKey(target = ModeloEntity.class, name = "modelo", update = RESTRICT, delete = CASCADE)
    @FieldInfo(columnName = "modelo_id")
    private int modeloId;

    private String cor;

    private Date fabricacao;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getModeloId() {
        return modeloId;
    }

    public void setModeloId(int modeloId) {
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
