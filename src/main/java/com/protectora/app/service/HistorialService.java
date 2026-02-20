package com.protectora.app.service;

import com.protectora.app.model.HistorialVacunacion;
import com.protectora.app.model.Vacuna;
import com.protectora.app.repository.mongo.HistorialRepository;
import com.protectora.app.repository.mongo.LogEvento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialService {
    @Autowired private HistorialRepository historialRepo;
    @Autowired private MongoTemplate mongoTemplate;

    public void agregarVacuna(Long animalId, Vacuna nuevaVacuna) {
        // Busca historial o crea uno nuevo si no existe
        HistorialVacunacion hv = historialRepo.findByAnimalId(animalId)
                .orElse(new HistorialVacunacion());
        
        if (hv.getAnimalId() == null) hv.setAnimalId(animalId);
        
        hv.getListaVacunas().add(nuevaVacuna);
        historialRepo.save(hv);
        
        // Log de evento sanitario en Mongo
        mongoTemplate.save(new LogEvento("VACUNACION_REGISTRADA", "Animal ID: " + animalId));
    }

    // Consulta de Agregación: Conteo por tipo de acción
    public List<org.bson.Document> estadisticasLogs() {
        Aggregation agg = Aggregation.newAggregation(
            Aggregation.group("accion").count().as("total"),
            Aggregation.sort(Sort.Direction.DESC, "total")
        );
        return mongoTemplate.aggregate(agg, "audit_logs", org.bson.Document.class).getMappedResults();
    }
}