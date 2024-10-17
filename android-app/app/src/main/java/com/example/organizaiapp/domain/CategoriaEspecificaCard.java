package com.example.organizaiapp.domain;

import java.util.Date;

public class CategoriaEspecificaCard {

    private Long idCategoria;
    private String dataDoGasto;
    private String descricaoDoGasto;
    private double valor;

    public CategoriaEspecificaCard() {
    }

    public CategoriaEspecificaCard(String dataDoGasto, String descricaoDoGasto, double valor) {
        this.dataDoGasto = dataDoGasto;
        this.descricaoDoGasto = descricaoDoGasto;
        this.valor = valor;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDataDoGasto() {
        return dataDoGasto;
    }

    public void setDataDoGasto(String dataDoGasto) {
        this.dataDoGasto = dataDoGasto;
    }

    public String getDescricaoDoGasto() {
        return descricaoDoGasto;
    }

    public void setDescricaoDoGasto(String descricaoDoGasto) {
        this.descricaoDoGasto = descricaoDoGasto;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
