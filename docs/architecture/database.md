# 🗄️ Base de Datos — SOFInventory

Motor: **PostgreSQL**
Base de datos: `sofinventory_db`

---

## Entidades y relaciones

```
tipo_documento          roles                permisos
──────────────         ──────────           ────────────
id                     id                   id
nombre                 nombre               nombre
                       │                    │
                       └──────┬─────────────┘
                              │ rol_permisos
                              │
usuarios ─────────────────────┘
──────────────
id
numero_documento
nombre_completo
email
username
password
active
ultimo_login
bloqueado_hasta
rol_id → roles
tipo_documento_id → tipo_documento
creado_por_id → usuarios (self)
actualizado_por_id → usuarios (self)
eliminado_por_id → usuarios (self)
fecha_creacion / fecha_actualizacion / fecha_eliminacion


categorias                    productos
──────────────               ────────────────────
id                           id
nombre                       sku (único)
tipo_control                 nombre
descripcion                  marca
[audit fields]               referencia
                             unidad_medida
                             estado
                             descripcion
                             categoria_id → categorias
                             [audit fields]


almacenes                    proveedores
──────────────               ───────────────
id                           id
nombre                       (ver entidad)
codigo (único)
direccion
responsable
telefono
capacidad
estado
[audit fields]


stock_almacen                compras
──────────────               ──────────────────
id                           id
cantidad                     numero_factura
stock_minimo                 fecha_compra
stock_maximo                 tipo_compra (contado/credito)
almacen_id → almacenes       subtotal
producto_id → productos      iva_total
ultima_actualizacion         total
                             estado (recibida/anulada)
                             proveedor_id → proveedores
                             registrado_por_id → usuarios


detalle_compra
──────────────────────
id
compra_id → compras
producto_id → productos
cantidad
precio_unitario
iva_porcentaje
subtotal


audit_log
──────────────────────
id
(registro de cambios del sistema)
```

---

## Notas sobre el modelo

- **Soft delete:** `Producto`, `Categoria`, `Almacen`, `Proveedor` y `Usuario` usan `fecha_eliminacion` en lugar de borrado físico.
- **Auditoría:** Las entidades principales guardan `creado_por_id`, `actualizado_por_id` y `eliminado_por_id` referenciando a `Usuario`.
- **Stock:** La tabla `stock_almacen` es la relación muchos a muchos entre `Producto` y `Almacen`, enriquecida con cantidad y umbrales.
- **Zonas horarias:** Los campos de fecha usan `ZonedDateTime` para soportar múltiples zonas horarias correctamente.
