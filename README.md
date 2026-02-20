# 🐾 Protectora "Las Colas Alegres"

Proyecto que gestiona una protectora de animales utilizando una arquitectura híbrida de persistencia (SQL + NoSQL) con **Spring Boot**.

## 📖 1. Temática y Reglas de Negocio

La aplicación permite gestionar el ciclo de vida de animales rescatados, sus adoptantes y su historial clínico.

- Los datos estructurales y relaciones fijas se gestionan en **SQL**.
- Los eventos de auditoría y registros médicos variables se gestionan en **MongoDB**.

## 🛠️ 2. Decisiones Técnicas (Justificación)

- **Hibernate/JPA (SQL):** Elegido para `Animal`, `Adoptante` y `Especie` porque el modelo relacional garantiza la integridad referencial y permite realizar consultas complejas sobre datos estructurados.
- **MongoDB (NoSQL):** Elegido para `HistorialVacunacion` y `AuditLogs`. Su naturaleza sin esquema (schema-less) permite añadir diferentes tipos de vacunas u observaciones clínicas sin alterar la base de datos, y es ideal para el almacenamiento masivo de logs.
- **Jackson:** Utilizado para la exportación de datos a JSON, permitiendo la portabilidad de la información.

## 📊 3. Modelo de Datos

### Modelo Relacional (SQL)

- **Animal**: id, nombre, edad, tieneChip, numeroChip, estado.
- **Adoptante**: id, dni, nombre, domicilio, telefono.
- **Especie**: id, tipo (Enum), familia, cuidadosBasicos.
  _Relaciones: Un Adoptante tiene N Animales; Una Especie tiene N Animales._

### Modelo Documental (MongoDB)

**Ejemplo de Historial de Vacunación:**

```json
{
  "animalId": 1,
  "listaVacunas": [
    { "nombre": "Rabia", "fecha": "2026-01-10" },
    { "nombre": "Parvovirus", "fecha": "2026-02-15" }
  ],
  "observaciones": "El animal reacciona bien a las vacunas"
}
```

## 🚀 4. Guía de Despliegue

1. **Requisitos**: Java 17, Maven, MongoDB corriendo en localhost:`27017`.
2. **Base de Datos SQL**: El proyecto usa H2 (en memoria) por defecto para facilitar la corrección. Se autogestiona al arrancar.
3. **Ejecución**:

```bash
mvn spring-boot:run
```

4. **Acceso**: La interfaz de consola se iniciará automáticamente en la terminal.

## 🧪 5. Casos de Uso e Integración

- **Integración**: Al registrar o modificar un animal en SQL, el sistema genera automáticamente un documento JSON en la colección `audit_logs` de MongoDB.
- **Consultas avanzadas**:
  - **SQL**: Búsqueda de animales por especie y estado.
  - **Mongo**: Agregación para contar el total de acciones realizadas en el sistema.

## Autores: Antonio, Marisa y Elena
