package com.example.organizaiapp.dto;

import java.util.List;

public class CadastroRequest {
    private String nome;
    private String sobrenome;
    private String email;
    private String telefone;
    private String senha;
    private List<CategoriaDto> categorias;

    public CadastroRequest(String nome, String sobrenome, String email, String telefone, String senha, List<CategoriaDto> categorias) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.categorias = categorias;
    }
}
