package com.example.organizaiapp.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateCategoriasUserRequest {
    @SerializedName("UserId")
    private Integer userId;
    private List<CategoriaDto> categorias;

    public UpdateCategoriasUserRequest(Integer userId, List<CategoriaDto> categorias) {
        this.userId = userId;
        this.categorias = categorias;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<CategoriaDto> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaDto> categorias) {
        this.categorias = categorias;
    }
}
