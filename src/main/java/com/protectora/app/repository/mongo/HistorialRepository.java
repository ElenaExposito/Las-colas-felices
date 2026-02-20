package com.protectora.app.repository.mongo;

import com.protectora.app.model.HistorialVacunacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface HistorialRepository extends MongoRepository<HistorialVacunacion, String> {
    // Buscar el historial por el ID del animal de SQL
    Optional<HistorialVacunacion> findByAnimalId(Long animalId);
}