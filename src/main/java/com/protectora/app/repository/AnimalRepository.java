package com.protectora.app.repository;

import com.protectora.app.model.Animal;
import com.protectora.app.model.Enums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    // Consulta avanzada 1: Filtro por estado y especie
    List<Animal> findByEstadoAndEspecie_Tipo(Enums.EstadoAnimal estado, Enums.TipoEspecie tipo);

    // Consulta avanzada 2: JPQL para encontrar animales sin chip
    @Query("SELECT a FROM Animal a WHERE a.tieneChip = false")
    List<Animal> findSinChip();
}