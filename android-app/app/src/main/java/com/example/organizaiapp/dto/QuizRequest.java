package com.example.organizaiapp.dto;

import com.google.gson.annotations.SerializedName;

public class QuizRequest {
    @SerializedName("UserId")
    private int userId;
    private Boolean isAnswered;
    private Boolean isCadUni;
    private Double rendaMensal;
    private Integer qtnPorFamilia;
    private Boolean isOlder;
    private Boolean isRural;
    private String dataCriacao;

    public QuizRequest(int userId, Boolean isAnswered, Boolean isCadUni, Double rendaMensal, Integer qtnPorFamilia, Boolean isOlder, Boolean isRural, String dataCriacao) {
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
}
