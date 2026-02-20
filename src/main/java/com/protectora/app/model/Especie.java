package com.protectora.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "especies")
public class Especie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Enums.TipoEspecie tipo;

    private String familia; // Ej: Canidos, Felinos
    private String cuidadosBasicos;

    public Especie() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Enums.TipoEspecie getTipo() {
        return tipo;
    }

    public void setTipo(Enums.TipoEspecie tipo) {
        this.tipo = tipo;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getCuidadosBasicos() {
        return cuidadosBasicos;
    }

    public void setCuidadosBasicos(String cuidadosBasicos) {
        this.cuidadosBasicos = cuidadosBasicos;
    }
}
