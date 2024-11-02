package br.fecap.pi.organizai.dto;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class QuizRequest {
    @SerializedName("UserId")
    private int userId;
    private Boolean isAnswered;
    private Boolean isCadUni;
    private BigDecimal rendaMensal;
    private Integer qtnPorFamilia;
    private Boolean isOlder;
    private Boolean isRural;
    private String dataCriacao;

    public QuizRequest(int userId, Boolean isAnswered, Boolean isCadUni, BigDecimal rendaMensal, Integer qtnPorFamilia, Boolean isOlder, Boolean isRural, String dataCriacao) {
        this.userId = userId;
        this.isAnswered = isAnswered;
        this.isCadUni = isCadUni;
        this.rendaMensal = rendaMensal;
        this.qtnPorFamilia = qtnPorFamilia;
        this.isOlder = isOlder;
        this.isRural = isRural;
        this.dataCriacao = dataCriacao;
    }

    public QuizRequest(int userId, Boolean isAnswered, String dataCriacao){
        this.userId = userId;
        this.isAnswered = isAnswered;
        this.dataCriacao = dataCriacao;
    }

    public QuizRequest() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Boolean getAnswered() {
        return isAnswered;
    }

    public void setAnswered(Boolean answered) {
        isAnswered = answered;
    }

    public Boolean getCadUni() {
        return isCadUni;
    }

    public void setCadUni(Boolean cadUni) {
        isCadUni = cadUni;
    }

    public BigDecimal getRendaMensal() {
        return rendaMensal;
    }

    public void setRendaMensal(BigDecimal rendaMensal) {
        this.rendaMensal = rendaMensal;
    }

    public Integer getQtnPorFamilia() {
        return qtnPorFamilia;
    }

    public void setQtnPorFamilia(Integer qtnPorFamilia) {
        this.qtnPorFamilia = qtnPorFamilia;
    }

    public Boolean getOlder() {
        return isOlder;
    }

    public void setOlder(Boolean older) {
        isOlder = older;
    }

    public Boolean getRural() {
        return isRural;
    }

    public void setRural(Boolean rural) {
        isRural = rural;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
