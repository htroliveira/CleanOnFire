package com.cleanonfire.sample.core;

import com.cleanonfire.annotations.data.db.ForeignKey;
import com.cleanonfire.annotations.data.db.IgnoreField;
import com.cleanonfire.annotations.data.db.PrimaryKey;
import com.cleanonfire.annotations.data.db.Table;

/**
 * Created by heitorgianastasio on 28/10/17.
 */
@Table
public class ModeloPeca {
    @PrimaryKey
    @ForeignKey(target = Peca.class, name = "peca")
    private long idPeca;

    @PrimaryKey
    @ForeignKey(target = Modelo.class,name = "modelo")
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
