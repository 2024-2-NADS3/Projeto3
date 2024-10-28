package com.example.organizaiapp.dto;

public class ResetRequest {
    private String email;
    private String senha;

    public ResetRequest(String email, String novaSenha) {
        this.email = email;
        this.senha = novaSenha;
    }
}
