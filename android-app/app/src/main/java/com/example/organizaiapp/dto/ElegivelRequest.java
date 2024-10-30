package com.example.organizaiapp.dto;

public class ElegivelRequest {
    private int userId;

    // Construtor
    public ElegivelRequest(int userId) {
        this.userId = userId;
    }

    // Getter e Setter
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
