# 🗺️ Rutas del Sistema — SOFInventory

El sistema es una aplicación web MVC. Todas las rutas devuelven vistas HTML renderizadas por Thymeleaf, excepto `/api/auth/login` que es un endpoint REST.

---

## Autenticación

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/login` | Vista de login |
| POST | `/auth/login` | Procesa el formulario de login |
| GET | `/logout` | Cierra la sesión |
| POST | `/api/auth/login` | Login REST (retorna JWT en JSON) |

---

## Dashboard

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/dashboard` | Vista principal del sistema |

---

## Categorías `/categorias`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/categorias` | Lista todas las categorías |
| GET | `/categorias/nueva` | Formulario de nueva categoría |
| POST | `/categorias/guardar` | Guarda la nueva categoría |
| GET | `/categorias/editar/{id}` | Formulario de edición |
| POST | `/categorias/actualizar/{id}` | Actualiza la categoría |
| GET | `/categorias/eliminar/{id}` | Soft delete |
| GET | `/categorias/restaurar/{id}` | Restaura eliminada |
| GET | `/categorias/eliminar-permanente/{id}` | Eliminación física |

---

## Productos `/productos`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/productos` | Lista todos los productos |
| GET | `/productos/nuevo` | Formulario de nuevo producto |
| POST | `/productos/guardar` | Guarda el nuevo producto |
| GET | `/productos/editar/{id}` | Formulario de edición |
| POST | `/productos/actualizar/{id}` | Actualiza el producto |
| GET | `/productos/eliminar/{id}` | Soft delete |
| GET | `/productos/restaurar/{id}` | Restaura eliminado |
| GET | `/productos/eliminar-permanente/{id}` | Eliminación física |

---

## Almacenes `/almacenes`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/almacenes` | Lista todos los almacenes |
| GET | `/almacenes/nuevo` | Formulario de nuevo almacén |
| POST | `/almacenes/guardar` | Guarda el nuevo almacén |
| GET | `/almacenes/editar/{id}` | Formulario de edición |
| POST | `/almacenes/actualizar/{id}` | Actualiza el almacén |
| GET | `/almacenes/desactivar/{id}` | Desactiva el almacén |
| GET | `/almacenes/reactivar/{id}` | Reactiva el almacén |
| GET | `/almacenes/eliminar/{id}` | Elimina el almacén |

---

## Proveedores `/proveedores`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/proveedores` | Lista todos los proveedores |
| GET | `/proveedores/nuevo` | Formulario de nuevo proveedor |
| POST | `/proveedores/guardar` | Guarda el nuevo proveedor |
| GET | `/proveedores/editar/{id}` | Formulario de edición |
| POST | `/proveedores/actualizar/{id}` | Actualiza el proveedor |
| GET | `/proveedores/desactivar/{id}` | Desactiva el proveedor |
| GET | `/proveedores/reactivar/{id}` | Reactiva el proveedor |
| GET | `/proveedores/eliminar/{id}` | Elimina el proveedor |

---

## Compras `/compras`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/compras` | Lista todas las compras |
| GET | `/compras/nueva` | Formulario de nueva compra |
| POST | `/compras/guardar` | Registra la compra con sus detalles |
| GET | `/compras/detalle/{id}` | Vista de detalle de una compra |
| GET | `/compras/anular/{id}` | Anula una compra registrada |

---

## Stock `/stock`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/stock` | Vista general del stock por almacén |
| GET | `/stock/{id}/ajuste` | Formulario de ajuste de cantidad |
| POST | `/stock/{id}/ajuste` | Aplica el ajuste de stock |
| GET | `/stock/{id}/umbrales` | Formulario de umbrales min/max |
| POST | `/stock/{id}/umbrales` | Guarda los umbrales configurados |

---

## Usuarios `/usuarios`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/usuarios` | Lista todos los usuarios |
| GET | `/usuarios/nuevo` | Formulario de nuevo usuario |
| POST | `/usuarios/guardar` | Guarda el nuevo usuario |
| GET | `/usuarios/editar/{id}` | Formulario de edición |
| POST | `/usuarios/actualizar/{id}` | Actualiza el usuario |
| GET | `/usuarios/desactivar/{id}` | Desactiva el usuario |
| GET | `/usuarios/reactivar/{id}` | Reactiva el usuario |
| GET | `/usuarios/eliminar/{id}` | Elimina el usuario |
