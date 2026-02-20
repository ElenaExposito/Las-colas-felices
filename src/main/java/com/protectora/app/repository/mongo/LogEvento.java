package com.protectora.app.repository.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "audit_logs")
public class LogEvento {
    
    @Id
    private String id;
    
    private String accion;
    private String detalle;
    private LocalDateTime timestamp;

    public LogEvento() {
    }

    public LogEvento(String accion, String detalle) {
        this.accion = accion;
        this.detalle = detalle;
        this.timestamp = LocalDateTime.now(); // Guarda la fecha y hora exacta automáticamente
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}