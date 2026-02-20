package com.protectora.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "historiales_sanitarios")
public class HistorialVacunacion {
    @Id
    private String id;
    
    private Long animalId; // El link con el ID de SQL
    private List<Vacuna> listaVacunas = new ArrayList<>();
    private String observaciones;

    public HistorialVacunacion() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public List<Vacuna> getListaVacunas() {
        return listaVacunas;
    }

    public void setListaVacunas(List<Vacuna> listaVacunas) {
        this.listaVacunas = listaVacunas;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}