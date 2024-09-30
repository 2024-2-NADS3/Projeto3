package com.example.organizaiapp.domain;

public class BeneficioCard {

    private Long idBeneficio;
    private String nome;
    private String iconBeneficio;
    private boolean isElegivel;
    private String descricao;

    public BeneficioCard() {
    }

    public BeneficioCard(Long idBeneficio, String nome, boolean isElegivel, String descricao) {
        this.idBeneficio = idBeneficio;
        this.nome = nome;
        this.isElegivel = isElegivel;
        this.descricao = descricao;
    }

    public Long getIdBeneficio() {
        return idBeneficio;
    }

    public void setIdBeneficio(Long idBeneficio) {
        this.idBeneficio = idBeneficio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIconBeneficio() {
        return iconBeneficio;
    }

    public void setIconBeneficio(String iconBeneficio) {
        this.iconBeneficio = iconBeneficio;
    }

    public boolean isElegivel() {
        return isElegivel;
    }

    public void setElegivel(boolean elegivel) {
        isElegivel = elegivel;
    }
}
