package com.example.organizaiapp.dto;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class TransacaoDto {
    @SerializedName("TransacaoId")
    private Integer transacaoId;
    @SerializedName("UsuarioId")
    private Integer usuarioId;
    @SerializedName("CategoriaId")
    private Long categoriaId;

    private boolean isReceita;
    private Double valor;
    private String descricao;
    private String data;

    public TransacaoDto(Integer usuarioId, Long categoriaId, boolean isReceita, Double valor, String descricao, String data) {
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
        this.isReceita = isReceita;
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
    }
    public Integer getTransacaoId() {
        return transacaoId;
    }

    public void setTransacaoId(Integer transacaoId) {
        this.transacaoId = transacaoId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public boolean isReceita() {
        return isReceita;
    }

    public void setReceita(boolean receita) {
        isReceita = receita;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
