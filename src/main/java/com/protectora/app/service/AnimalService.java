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

    // TODO: amplia esto con los logs
}