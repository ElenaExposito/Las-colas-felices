package com.protectora.app; // <--- Importante: el paquete raíz

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication // <--- Aquí es donde se añade
@EnableJpaRepositories(basePackages = "com.protectora.app.repository") // Escanea repos SQL
@EnableMongoRepositories(basePackages = "com.protectora.app.repository.mongo") // Escanea repos Mongo
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("¡Protectora Las Colas Alegres en marcha!");
    }
}