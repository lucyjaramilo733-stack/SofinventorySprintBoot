# 🏃 Sprint 01 — Estado actual del proyecto

**Fecha de corte:** Abril 2026
**Equipo:** 2 personas

---

## ✅ Módulos completados

### Autenticación y seguridad
- Login con usuario y contraseña
- Autenticación basada en JWT con filtro por request
- Roles y permisos por usuario
- Control de sesión y logout
- Bloqueo de cuenta (`bloqueado_hasta`)

### Gestión de catálogos
- CRUD completo de **Categorías** con soft delete y restauración
- CRUD completo de **Productos** con SKU único, marca, referencia, unidad de medida y soft delete
- CRUD completo de **Almacenes** con activación/desactivación
- CRUD completo de **Proveedores** con activación/desactivación

### Compras
- Registro de compras con número de factura, proveedor y detalle de productos
- Cálculo automático de subtotal, IVA y total
- Vista de detalle de compra
- Anulación de compras

### Stock
- Vista consolidada de stock por almacén y producto
- Ajuste manual de cantidad con registro de cambio
- Configuración de umbrales mínimo y máximo
- Alertas visuales de stock bajo, agotado y excedido

### Usuarios
- CRUD completo de usuarios con rol asignado
- Activación y desactivación de cuentas
- Registro de último login y auditoría de creación/modificación

### Dashboard
- Vista principal del sistema post-login

---

## 🔄 En progreso / Pendiente

| Funcionalidad | Estado | Notas |
|---------------|--------|-------|
| Módulo de ventas | ⏳ Pendiente | No iniciado aún |
| Reportes y exportación | ⏳ Pendiente | — |
| Auditoría (`AuditLog`) | 🔄 Parcial | Entidad creada, integración pendiente |
| Validaciones de formulario | 🔄 Parcial | Algunas vistas sin validación completa |
| Manejo de errores global | ⏳ Pendiente | Sin página de error personalizada aún |

---

## 🧩 Decisiones tomadas en este sprint

- Se decidió usar **soft delete** para mantener historial de compras sin romper integridad referencial
- Se separó el controlador web del controlador REST de autenticación para mantener claridad entre los dos tipos de endpoints
- El layout compartido `fragments/layout.html` evita duplicar la navegación en cada vista
- Cada módulo tiene su propio archivo CSS para facilitar el trabajo colaborativo sin conflictos

---

## ⚠️ Deuda técnica identificada

- `application.properties` contiene credenciales hardcodeadas — migrar a variables de entorno antes del despliegue
- Falta manejo global de excepciones (`@ControllerAdvice`)
- No hay pruebas unitarias escritas aún más allá del test de contexto inicial
