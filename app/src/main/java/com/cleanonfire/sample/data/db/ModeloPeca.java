package com.cleanonfire.sample.data.db;

import com.cleanonfire.annotations.data.db.ForeignKey;
import com.cleanonfire.annotations.data.db.PrimaryKey;
import com.cleanonfire.annotations.data.db.Table;

/**
 * Created by heitorgianastasio on 28/10/17.
 */
@Table(tableName = "modelo_tem_peca")
public class ModeloPeca {
    @PrimaryKey
    @ForeignKey(target = PecaEntity.class, name = "peca")
    private long idPeca;

    @PrimaryKey
    @ForeignKey(target = ModeloEntity.class,name = "modelo")
    private int idModelo;

    private int quantidade;

    public long getIdPeca() {
        return idPeca;
    }

    public void setIdPeca(long idPeca) {
        this.idPeca = idPeca;
    }

    public int getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(int idModelo) {
        this.idModelo = idModelo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
