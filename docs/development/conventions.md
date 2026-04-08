# 📐 Estándares y Convenciones

## Convenciones de código Java

### Nomenclatura

| Elemento | Convención | Ejemplo |
|----------|-----------|---------|
| Clases | PascalCase | `ProductoService` |
| Métodos | camelCase | `guardarProducto()` |
| Variables | camelCase | `fechaCreacion` |
| Constantes | UPPER_SNAKE_CASE | `JWT_SECRET` |
| Tablas BD | snake_case | `stock_almacen` |
| Columnas BD | snake_case | `numero_factura` |

### Estructura de capas

El proyecto sigue una arquitectura en capas estricta:

```
Controller → Service → Repository → Entity
```

- Los **controllers** solo gestionan la navegación y el modelo de la vista
- La **lógica de negocio** va exclusivamente en los services
- Los **repositories** solo contienen consultas a la base de datos

---

## Convenciones de Git

### Ramas

| Rama | Uso |
|------|-----|
| `main` | Código estable, listo para presentar |
| `develop` | Integración de funcionalidades en desarrollo |
| `feature/nombre` | Desarrollo de una funcionalidad nueva |

### Mensajes de commit (Conventional Commits)

```
tipo: descripción corta en presente

feat: agregar módulo de compras
fix: corregir validación de stock mínimo
docs: actualizar setup.md
refactor: extraer lógica de JWT a clase separada
style: formatear ProductoController
```

| Tipo | Cuándo usarlo |
|------|--------------|
| `feat` | Nueva funcionalidad |
| `fix` | Corrección de bug |
| `docs` | Solo documentación |
| `refactor` | Cambio de código sin nueva funcionalidad |
| `style` | Formato, espacios, punto y coma |

---

## Manejo de eliminación

El sistema usa **soft delete** (eliminación lógica) en las entidades principales. Los registros no se borran físicamente, se marca `fecha_eliminacion`. Esto aplica a: `Producto`, `Categoria`, `Almacen`, `Proveedor`, `Usuario`.
