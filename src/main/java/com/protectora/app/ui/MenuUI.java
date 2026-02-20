package com.protectora.app.ui;

import com.protectora.app.service.AnimalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.Scanner;

@Component
public class MenuUI implements CommandLineRunner {
    @Autowired private AnimalService animalService;
    @Autowired private ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- PROTECTORA: LAS COLAS ALEGRE ---");
            System.out.println("1. Listar Animales (SQL) | 2. Exportar a JSON (Jackson) | 0. Salir");
            int op = sc.nextInt();

            switch (op) {
                case 1 -> animalService.listarTodos().forEach(a -> 
                    System.out.println(a.getNombre() + " [" + a.getEstado() + "]"));
                case 2 -> {
                    // Exportación a fichero JSON
                    objectMapper.writerWithDefaultPrettyPrinter()
                                .writeValue(new File("animales_protectora.json"), animalService.listarTodos());
                    System.out.println("Fichero 'animales_protectora.json' generado con éxito.");
                }
                case 0 -> salir = true;
            }
        }
    }
}