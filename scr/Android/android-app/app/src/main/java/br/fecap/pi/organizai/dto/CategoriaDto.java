package br.fecap.pi.organizai.dto;

import com.google.gson.annotations.SerializedName;

public class CategoriaDto {
    @SerializedName("CategoriaId")
    private int categoriaId;

    @SerializedName("nomeCat")
    private String nomeCat;

    private int tipo;

    private double total;

    public CategoriaDto(int categoriaId, String nomeCat, int tipo, double total) {
        this.categoriaId = categoriaId;
        this.nomeCat = nomeCat;
        this.tipo = tipo;
        this.total = total;
    }

    // Getters e setters
    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNomeCat() {
        return nomeCat;
    }

    public void setNomeCat(String nomeCat) {
        this.nomeCat = nomeCat;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
