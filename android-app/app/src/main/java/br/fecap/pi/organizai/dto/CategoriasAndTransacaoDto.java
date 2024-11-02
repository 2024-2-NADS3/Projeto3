package br.fecap.pi.organizai.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriasAndTransacaoDto {
    @SerializedName("categoria")
    private CategoriaDto categoria;
    @SerializedName("transacoes")
    private List<TransacaoDto> transacoes;

    public CategoriasAndTransacaoDto(CategoriaDto categoria, List<TransacaoDto> transacoes) {
        this.categoria = categoria;
        this.transacoes = transacoes;
    }

    public CategoriaDto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDto categoria) {
        this.categoria = categoria;
    }

    public List<TransacaoDto> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<TransacaoDto> transacoes) {
        this.transacoes = transacoes;
    }
}
