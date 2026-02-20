package com.protectora.app.ui;

import com.protectora.app.model.Animal;
import com.protectora.app.model.Enums;
import com.protectora.app.model.Especie;
import com.protectora.app.service.AnimalService;
import com.protectora.app.service.HistorialService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.List;
import java.util.Scanner;

@Component
public class MenuUI implements CommandLineRunner {
    @Autowired private AnimalService animalService;
    @Autowired private HistorialService historialService;
    @Autowired private ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        System.out.println("\n[SISTEMA] Comprobando copia de seguridad JSON...");
        File jsonFile = new File("animales_protectora.json");
        
        if (jsonFile.exists()) {
            try {
                // Leemos el JSON y lo convertimos en una lista de Animales
                List<Animal> animalesImportados = objectMapper.readValue(jsonFile,
                    new com.fasterxml.jackson.core.type.TypeReference<List<Animal>>(){});
                
                for (Animal a : animalesImportados) {
                    // Ponemos los IDs a null para que Hibernate no se queje y los genere de nuevo
                    a.setId(null);
                    if (a.getEspecie() != null) {
                        a.getEspecie().setId(null);
                    }
                    // Guardamos en SQL (esto también generará logs automáticos en Mongo)
                    animalService.registrarNuevoAnimal(a);
                }
                System.out.println("[OK] Se han importado " + animalesImportados.size() + " animales desde el archivo JSON.");
            } catch (Exception e) {
                System.out.println("[WARNING] No se pudo leer el archivo JSON: " + e.getMessage());
            }
        } else {
            System.out.println("[INFO] No hay archivo JSON previo. La base de datos SQL empieza vacía.");
        }

        while (!salir) {
            System.out.println("\n--- PROTECTORA: LAS COLAS ALEGRES ---");
            System.out.println("1. Listar Animales (SQL)");
            System.out.println("2. Exportar a JSON (Jackson)");
            System.out.println("3. Registrar Nuevo Animal (Completo)");
            System.out.println("4. Eliminar Animal (Borrado)");
            System.out.println("5. Ver Estadísticas de Actividad (Mongo Agregación)");
            System.out.println("6. Añadir vacuna a animal existente");
            System.out.println("7. Ver historial médico");
            System.out.println("8. Buscar logs por tipo");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            int op = sc.nextInt();
            sc.nextLine(); // Limpiar el buffer de Scanner

            switch (op) {
                case 1 -> {
                    System.out.println("\n--- LISTA DE ANIMALES EN SQL ---");
                    animalService.listarTodos().forEach(a -> {
                        String especie = (a.getEspecie() != null) ? a.getEspecie().getTipo().name() : "Desconocida";
                        System.out.println("ID: " + a.getId() + " | Nombre: " + a.getNombre() + 
                        " | Especie: " + especie + " | Estado: " + a.getEstado() +
                        " | Chip: " + (a.isTieneChip() ? a.getNumeroChip() : "No"));
                    });
                }
                
                case 2 -> {
                    File archivoExportacion = new File("animales_protectora.json");
                    
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(archivoExportacion, animalService.listarTodos());
                    System.out.println("\n[OK] Copia de seguridad JSON actualizada correctamente en:");
                    System.out.println(" " + archivoExportacion.getAbsolutePath());
                }

                case 3 -> {
                    System.out.println("\n--- ALTA DE NUEVO ANIMAL ---");
                    Animal nuevo = new Animal();
                    
                    System.out.print("Nombre del animal: ");
                    nuevo.setNombre(sc.nextLine());
                    
                    System.out.print("Edad: ");
                    nuevo.setEdad(sc.nextInt());
                    sc.nextLine(); // Limpiar buffer

                    System.out.print("¿Tiene chip? (S/N): ");
                    boolean tieneChip = sc.nextLine().equalsIgnoreCase("S");
                    nuevo.setTieneChip(tieneChip);
                    if (tieneChip) {
                        System.out.print("Número de chip: ");
                        nuevo.setNumeroChip(sc.nextLine());
                    }

                    System.out.print("Especie (PERRO, GATO, AVE, REPTIL, OTRO): ");
                    String tipoEsp = sc.nextLine().toUpperCase();
                    Especie especie = new Especie();
                    try {
                        especie.setTipo(Enums.TipoEspecie.valueOf(tipoEsp));
                    } catch (Exception e) {
                        System.out.println("Especie no válida, asignando OTRO por defecto.");
                        especie.setTipo(Enums.TipoEspecie.OTRO);
                    }
                    nuevo.setEspecie(especie);

                    nuevo.setEstado(Enums.EstadoAnimal.DISPONIBLE);
                    
                    // Integración 1: Guarda en SQL y genera LOG en Mongo
                    Animal animalGuardado = animalService.registrarNuevoAnimal(nuevo);
                    System.out.println("\n[OK] Animal guardado en SQL y evento auditado en MongoDB.");

                    // Integración 2: Preguntamos si queremos guardar historial médico en Mongo
                    System.out.print("¿Deseas registrar una vacuna inicial en MongoDB? (S/N): ");
                    if(sc.nextLine().equalsIgnoreCase("S")) {
                        System.out.print("Nombre de la vacuna (ej. Rabia): ");
                        String nomVacuna = sc.nextLine();
                        com.protectora.app.model.Vacuna vacuna = new com.protectora.app.model.Vacuna();
                        vacuna.setNombreVacuna(nomVacuna);
                        vacuna.setFechaAplicacion(java.time.LocalDate.now());
                        
                        historialService.agregarVacuna(animalGuardado.getId(), vacuna);
                        System.out.println("[OK] Historial médico creado/actualizado en MongoDB.");
                    }
                }
                
                case 4 -> {
                    System.out.print("\nIntroduce el ID del animal a eliminar: ");
                    Long idDel = sc.nextLong();
                    animalService.eliminarAnimal(idDel);
                    System.out.println("[OK] Animal eliminado de SQL y registro guardado en logs de MongoDB.");
                }

                case 5 -> {
                    System.out.println("\n--- ESTADÍSTICAS DE MONGO (AGREGACIÓN) ---");
                    historialService.obtenerEstadisticasDeAcciones().forEach(doc ->
                        System.out.println("Evento: " + doc.get("_id") + " | Frecuencia: " + doc.get("total")));
                }

                case 6 -> {
                    System.out.println("\n--- AÑADIR VACUNA A ANIMAL EXISTENTE ---");
                    System.out.print("Introduce el ID del animal (SQL): ");
                    Long idAnimal = sc.nextLong();
                    sc.nextLine(); // Limpiar buffer

                    System.out.print("Nombre de la vacuna (ej. Rabia, Parvovirus): ");
                    String nomVacuna = sc.nextLine();
                    
                    com.protectora.app.model.Vacuna vacuna = new com.protectora.app.model.Vacuna();
                    vacuna.setNombreVacuna(nomVacuna);
                    vacuna.setFechaAplicacion(java.time.LocalDate.now());

                    historialService.agregarVacuna(idAnimal, vacuna);
                    System.out.println("[OK] Vacuna añadida al historial del animal en MongoDB.");
                }

                case 7 -> {
                    System.out.println("\n--- VER HISTORIAL MÉDICO (FILTRO MONGO 1) ---");
                    System.out.print("Introduce el ID del animal: ");
                    Long idAnimal = sc.nextLong();
                    sc.nextLine();

                    com.protectora.app.model.HistorialVacunacion historial = historialService.obtenerHistorialPorAnimal(idAnimal);
                    
                    if (historial == null || historial.getListaVacunas().isEmpty()) {
                        System.out.println("El animal no tiene vacunas registradas en MongoDB.");
                    } else {
                        System.out.println("Vacunas del animal ID " + idAnimal + ":");
                        historial.getListaVacunas().forEach(v -> 
                            System.out.println("   - " + v.getNombreVacuna() + " (Aplicada: " + v.getFechaAplicacion() + ")")
                        );
                    }
                }

                case 8 -> {
                    System.out.println("\n--- BUSCAR LOGS POR TIPO (FILTRO MONGO 2) ---");
                    System.out.print("¿Qué tipo de acción quieres buscar? (ej. CREAR_ANIMAL, VACUNACION_REGISTRADA): ");
                    String accion = sc.nextLine();

                    List<com.protectora.app.repository.mongo.LogEvento> logs = historialService.buscarLogsPorAccion(accion);
                    if (logs.isEmpty()) {
                        System.out.println("No hay registros de ese tipo.");
                    } else {
                        System.out.println("Logs encontrados para '" + accion + "':");
                        logs.forEach(l -> System.out.println("   - [" + l.getTimestamp() + "] " + l.getDetalle()));
                    }
                }
                
                case 0 -> salir = true;
                default -> System.out.println("Opción no válida.");
            }
        }
        System.out.println("Cerrando aplicación...");
    }
}