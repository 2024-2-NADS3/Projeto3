package br.fecap.pi.organizai.domain;

public class BeneficioCard {

    private Long idBeneficio;
    private String nome;
    private boolean isElegivel;
    private String descricao;
    private int iconBeneficio; // Corrigido para int e renomeado corretamente

    public BeneficioCard() {
    }

    // Construtor com o ícone correto (int)
    public BeneficioCard(Long idBeneficio, String nome, boolean isElegivel, String descricao, int iconBeneficio) {
        this.idBeneficio = idBeneficio;
        this.nome = nome;
        this.isElegivel = isElegivel;
        this.descricao = descricao;
        this.iconBeneficio = iconBeneficio; // Usando o atributo correto
    }

    // Getter e Setter para idBeneficio
    public Long getIdBeneficio() {
        return idBeneficio;
    }

    public void setIdBeneficio(Long idBeneficio) {
        this.idBeneficio = idBeneficio;
    }

    // Getter e Setter para nome
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Getter e Setter para descrição
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Getter e Setter para isElegivel
    public boolean isElegivel() {
        return isElegivel;
    }

    public void setElegivel(boolean elegivel) {
        isElegivel = elegivel;
    }

    // Getter e Setter para o ícone (int)
    public int getIconBeneficio() {
        return iconBeneficio;
    }

    public void setIconBeneficio(int iconBeneficio) {
        this.iconBeneficio = iconBeneficio;
    }
}