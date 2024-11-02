package br.fecap.pi.organizai.domain;

public class CategoriaCard {

    private int categoriaId;
    private String categoria;
    private double valor;

    public CategoriaCard(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public CategoriaCard(int categoriaId, String categoria, double valor) {
        this.categoriaId = categoriaId;
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

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }
}
