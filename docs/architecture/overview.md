# 🏗️ Arquitectura General — SOFInventory

## Tipo de aplicación

SOFInventory es una aplicación web **MVC monolítica** construida con Spring Boot. Usa **Thymeleaf** como motor de plantillas del lado del servidor, lo que significa que las vistas se renderizan en el backend y se envían como HTML al navegador.

No es una API REST pura (aunque tiene un endpoint REST en `/api/auth/login`). La navegación entre módulos es tradicional: el servidor responde con páginas HTML completas.

---

## Diagrama de capas

```
┌─────────────────────────────────────────┐
│              NAVEGADOR                  │
│         (HTML + CSS + JS)               │
└────────────────┬────────────────────────┘
                 │ HTTP
┌────────────────▼────────────────────────┐
│           SPRING BOOT                   │
│                                         │
│  ┌─────────────────────────────────┐    │
│  │  Security Layer (JWT + Filter)  │    │
│  └────────────────┬────────────────┘    │
│                   │                     │
│  ┌────────────────▼────────────────┐    │
│  │       Controllers (Web)         │    │
│  │  /productos  /compras  /stock   │    │
│  └────────────────┬────────────────┘    │
│                   │                     │
│  ┌────────────────▼────────────────┐    │
│  │          Services               │    │
│  │     (lógica de negocio)         │    │
│  └────────────────┬────────────────┘    │
│                   │                     │
│  ┌────────────────▼────────────────┐    │
│  │    Repositories (Spring Data)   │    │
│  └────────────────┬────────────────┘    │
└───────────────────┼─────────────────────┘
                    │ JPA / Hibernate
┌───────────────────▼─────────────────────┐
│           PostgreSQL                    │
│         sofinventory_db                 │
└─────────────────────────────────────────┘
```

---

## Módulos del sistema

| Módulo | Descripción | Estado |
|--------|-------------|--------|
| **Autenticación** | Login con JWT, sesión por cookie/session | ✅ Completo |
| **Dashboard** | Vista general del sistema | ✅ Completo |
| **Categorías** | CRUD con soft delete | ✅ Completo |
| **Productos** | CRUD con SKU, marca, soft delete | ✅ Completo |
| **Almacenes** | CRUD con activación/desactivación | ✅ Completo |
| **Proveedores** | CRUD con activación/desactivación | ✅ Completo |
| **Compras** | Registro con detalle y anulación | ✅ Completo |
| **Stock** | Ajuste manual y umbrales min/max | ✅ Completo |
| **Usuarios** | CRUD con roles y activación | ✅ Completo |

---

## Decisiones de diseño

**¿Por qué Thymeleaf y no React/Angular?**
El proyecto está orientado a PyMES con infraestructura simple. Una aplicación monolítica es más fácil de desplegar y mantener sin necesidad de separar frontend y backend.

**¿Por qué soft delete?**
Las entidades de negocio (productos, proveedores, etc.) nunca se eliminan físicamente para mantener la integridad referencial con compras históricas.

**¿Por qué JWT si es una app web con sesión?**
Se implementó JWT para mantener la opción de exponer una API REST en el futuro sin cambiar la capa de seguridad.


