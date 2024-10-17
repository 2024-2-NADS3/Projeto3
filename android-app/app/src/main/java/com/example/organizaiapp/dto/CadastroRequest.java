package com.example.organizaiapp.dto;

public class CadastroRequest {
    private String nome;
    private String sobrenome;
    private String email;
    private String telefone;
    private String senha;

    public CadastroRequest(String nome, String sobrenome, String email, String telefone, String senha) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
    }
}
