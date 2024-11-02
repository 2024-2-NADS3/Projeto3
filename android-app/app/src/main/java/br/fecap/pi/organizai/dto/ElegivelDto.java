package br.fecap.pi.organizai.dto;

public class ElegivelDto {
    private boolean cadastroUnico;
    private boolean bolsaFamilia;
    private boolean beneficioIdoso;
    private boolean fomentoRural;

    // Getters e Setters
    public boolean isCadastroUnico() {
        return cadastroUnico;
    }

    public void setCadastroUnico(boolean cadastroUnico) {
        this.cadastroUnico = cadastroUnico;
    }

    public boolean isBolsaFamilia() {
        return bolsaFamilia;
    }

    public void setBolsaFamilia(boolean bolsaFamilia) {
        this.bolsaFamilia = bolsaFamilia;
    }

    public boolean isBeneficioIdoso() {
        return beneficioIdoso;
    }

    public void setBeneficioIdoso(boolean beneficioIdoso) {
        this.beneficioIdoso = beneficioIdoso;
    }

    public boolean isFomentoRural() {
        return fomentoRural;
    }

    public void setFomentoRural(boolean fomentoRural) {
        this.fomentoRural = fomentoRural;
    }
}
