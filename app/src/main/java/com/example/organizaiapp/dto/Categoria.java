package com.example.organizaiapp.dto;

public class Categoria {
    private int icone;  // ID do recurso de ícone da categoria (R.drawable)
    private String nome; // Nome da categoria

    public Categoria(int icone, String nome) {
        this.icone = icone;
        this.nome = nome;
    }

    public int getIcone() {
        return icone;
    }

    public void setIcone(int icone) {
        this.icone = icone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
