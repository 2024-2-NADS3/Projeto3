package com.example.organizaiapp.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserDataDto {

    @SerializedName("UserId")
    private int userId;

    private String nome;
    private String sobrenome;
    private String cpf;
    private String email;
    private String telefone;
    private String senha;
    private QuizDto quiz;
    private List<CategoriaDto> categorias;

    // Getters e setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public QuizDto getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizDto quiz) {
        this.quiz = quiz;
    }

    public List<CategoriaDto> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaDto> categorias) {
        this.categorias = categorias;
    }
}
