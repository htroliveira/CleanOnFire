package com.cleanonfire.sample.core.carros.model;

import com.cleanonfire.sample.core.modelos.model.Modelo;

import java.util.Date;

/**
 * Created by heitorgianastasio on 13/11/17.
 */

public class Carro {
    private long id;
    private Date ano;
    private String cor;
    private Modelo modelo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getAno() {
        return ano;
    }

    public void setAno(Date ano) {
        this.ano = ano;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }
}
