# ⚙️ Configuración del Entorno de Desarrollo

## 1. Clonar el repositorio

```bash
git clone https://github.com/lucyjaramilo733-stack/SofinventorySprintBoot
cd sofinventory
```

---

## 2. Configurar la base de datos

El proyecto usa **PostgreSQL**. Cada integrante del equipo tiene su instancia local en un puerto diferente:

| Desarrollador | Puerto PostgreSQL |
|---------------|-----------------|
| Alejandro Sepúlveda Duarte | `5433` |
| Lucy Estefany Izquierdo Jaramillo | `5432` |

```sql
-- Crear la base de datos (ejecutar en psql)
CREATE DATABASE sofinventory_db;
```

---

## 3. Ajustar `application.properties`

El archivo `src/main/resources/application.properties` debe ajustarse según el entorno local de cada desarrollador. **Este archivo no debe subirse con credenciales reales al repositorio.**

```properties
# Alejandro (puerto 5433)
spring.datasource.url=jdbc:postgresql://localhost:5433/sofinventory_db

# Lucy (puerto 5432)
spring.datasource.url=jdbc:postgresql://localhost:5432/sofinventory_db
```

| Propiedad | Descripción |
|-----------|-------------|
| `spring.datasource.username` | Usuario de PostgreSQL local |
| `spring.datasource.password` | Contraseña local (no compartir) |
| `server.port` | Puerto del servidor web (default: `8080`) |
| `jwt.expiration` | Expiración del token en ms (default: `86400000` = 24h) |

> ⚠️ **Nunca subir contraseñas reales al repositorio.** Si el archivo tiene cambios locales de credenciales, agregarlo al `.gitignore` o usar variables de entorno.

---

## 4. Ejecutar el proyecto

El proyecto usa **Gradle Wrapper** — no es necesario tener Gradle instalado.

```bash
# Mac / Linux
./gradlew bootRun

# Windows
gradlew.bat bootRun

# Solo compilar sin correr
./gradlew clean build
```

Acceder en: `http://localhost:8080/login`

---

## 5. Usuario inicial

Al arrancar por primera vez, `DataInitializer.java` crea automáticamente los datos iniciales: roles, permisos y un usuario administrador por defecto. Consultar ese archivo para ver las credenciales de prueba.

---

## 6. Sobre Gradle Wrapper

Los archivos `gradlew`, `gradlew.bat` y la carpeta `gradle/wrapper/` forman el **Gradle Wrapper**. Esto garantiza que ambos integrantes del equipo compilen el proyecto con exactamente la misma versión de Gradle, sin necesidad de instalarlo manualmente.

La carpeta `.gradle/` y `build/` son generadas localmente y están en `.gitignore` — no se suben al repositorio.
