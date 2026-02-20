package com.protectora.app.repository;

import com.protectora.app.model.Adoptante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptanteRepository extends JpaRepository<Adoptante, Long> {
    Adoptante findByDni(String dni);
}