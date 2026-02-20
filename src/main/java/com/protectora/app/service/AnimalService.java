package com.protectora.app.service;

import com.protectora.app.model.Animal;
import com.protectora.app.repository.AnimalRepository;
import com.protectora.app.repository.mongo.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimalService {
    @Autowired private AnimalRepository animalRepo;
    @Autowired private HistorialRepository historialRepo;

    public Animal guardarAnimal(Animal animal) {
        return animalRepo.save(animal);
    }

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
}