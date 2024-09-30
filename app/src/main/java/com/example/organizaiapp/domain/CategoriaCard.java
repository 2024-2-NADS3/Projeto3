package com.example.organizaiapp.domain;

public class CategoriaCard {

    private String categoria;
    private double valor;

    public CategoriaCard() {
    }

    public CategoriaCard(String categoria, double valor) {
        this.categoria = categoria;
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
