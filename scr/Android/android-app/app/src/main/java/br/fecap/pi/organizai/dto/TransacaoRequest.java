package br.fecap.pi.organizai.dto;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class TransacaoRequest {
    @SerializedName("UsuarioId")
    private Integer usuarioId;
    @SerializedName("CategoriaId")
    private Long categoriaId;

    private boolean isReceita;
    private BigDecimal valor;
    private String descricao;
    private String data;

    public TransacaoRequest(Integer usuarioId, Long categoriaId, boolean isReceita, BigDecimal valor, String descricao, String data) {
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
        this.isReceita = isReceita;
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
    }
}
