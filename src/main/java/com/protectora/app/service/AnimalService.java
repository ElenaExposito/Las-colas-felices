package com.protectora.app.service;

import com.protectora.app.model.Animal;
import com.protectora.app.model.Enums;
import com.protectora.app.repository.mongo.LogEvento;
import com.protectora.app.repository.AnimalRepository;
import com.protectora.app.repository.mongo.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {
    @Autowired private AnimalRepository animalRepo;
    @Autowired private HistorialRepository historialRepo;
    @Autowired private MongoTemplate mongoTemplate; // Para guardar el log rápido

    public Animal registrarNuevoAnimal(Animal animal) {
        // 1. Guardamos en SQL
        Animal guardado = animalRepo.save(animal);
        
        // 2. INTEGRAMOS: Creamos log en Mongo
        LogEvento log = new LogEvento("CREAR_ANIMAL", 
            "Se ha registrado a " + guardado.getNombre() + " con ID: " + guardado.getId());
        mongoTemplate.save(log);
        
        return guardado;
    }

    public List<Animal> listarTodos() {
        return animalRepo.findAll();
    }

    public void eliminarAnimal(Long id) {
        animalRepo.deleteById(id);
        mongoTemplate.save(new LogEvento("ANIMAL_ELIMINADO", "ID: " + id));
    }

    public Animal actualizarEstado(Long id, Enums.EstadoAnimal nuevoEstado) {
        Animal animal = animalRepo.findById(id).orElseThrow();
        animal.setEstado(nuevoEstado);
        Animal actualizado = animalRepo.save(animal);

        // Integración: Log de modificación
        mongoTemplate.save(new LogEvento("ANIMAL_MODIFICADO",
                "Animal " + actualizado.getNombre() + " ahora está " + nuevoEstado));

        return actualizado;
    }
    //Consultas avanzadas
    public List<Animal> buscarDisponibles(Enums.TipoEspecie tipo) {
        return animalRepo.findByEstadoAndEspecie_Tipo(Enums.EstadoAnimal.DISPONIBLE, tipo);
    }
}