package br.fecap.pi.organizai.dto;

import java.util.List;

public class CadastroRequest {
    private String nome;
    private String sobrenome;
    private String email;
    private String telefone;
    private String senha;
    private String aesKey;
    private List<CategoriaDto> categorias;

    public CadastroRequest(String nome, String sobrenome, String email, String telefone, String senha, String aesKey, List<CategoriaDto> categorias) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.aesKey = aesKey;
        this.categorias = categorias;
    }
}
