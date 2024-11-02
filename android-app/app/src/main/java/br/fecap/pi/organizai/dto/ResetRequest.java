package br.fecap.pi.organizai.dto;

public class ResetRequest {
    private String email;
    private String newPassword;

    public ResetRequest(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }
}
