package com.protectora.app.repository;

import com.protectora.app.model.Animal;
import com.protectora.app.model.Enums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    // Consulta avanzada 1: Filtrar por estado y especie
    List<Animal> findByEstadoAndEspecie_Tipo(Enums.EstadoAnimal estado, Enums.TipoEspecie tipo);

    // Consulta avanzada 2: JPQL para buscar animales de cierta edad en adelante
    @Query("SELECT a FROM Animal a WHERE a.edad >= ?1")
    List<Animal> findAnimalesMayoresDe(int edad);
}