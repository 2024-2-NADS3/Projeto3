package com.example.organizaiapp.dto;

import com.google.gson.annotations.SerializedName;

public class QuizDto {
    @SerializedName("QuizId")
    private int quizId;
    @SerializedName("UserId")
    private int userId;

    private Boolean isAnswered;
    private Boolean isCadUni;
    private Double rendaMensal;
    private Integer qtnPorFamilia;
    private Boolean isOlder;
    private Boolean isRural;
    private String dataCriacao;

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Boolean getIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(Boolean answered) {
        isAnswered = answered;
    }

    public Boolean getCadUni() {
        return isCadUni;
    }

    public void setCadUni(Boolean cadUni) {
        isCadUni = cadUni;
    }

    public Double getRendaMensal() {
        return rendaMensal;
    }

    public void setRendaMensal(Double rendaMensal) {
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
