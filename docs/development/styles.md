# 🎨 Sistema de Estilos CSS — SOFInventory

## Organización de archivos

Cada módulo del sistema tiene su propio archivo CSS ubicado en `src/main/resources/static/css/`. Esta separación permite que cada desarrollador trabaje en los estilos de su módulo sin generar conflictos en el repositorio.

```
static/css/
├── login.css         ← pantalla de autenticación
├── dashboard.css     ← layout principal y sidebar
├── productos.css     ← módulo de productos
├── categorias.css    ← módulo de categorías
├── almacenes.css     ← módulo de almacenes
├── proveedores.css   ← módulo de proveedores
├── compras.css       ← módulo de compras
├── stock.css         ← módulo de stock
└── usuarios.css      ← módulo de usuarios
```

---

## Paleta de colores

El sistema usa dos paletas diferenciadas según el contexto:

### Interior de la aplicación (dashboard y módulos)

| Token | Color | Uso |
|-------|-------|-----|
| Principal oscuro | `#1a1a2e` | Sidebar, títulos, badges principales |
| Hover sidebar | `#16213e` | Ítem de navegación activo u hover |
| Fondo general | `#f0f2f5` | Fondo del área de contenido |
| Fondo tarjetas | `#ffffff` | Cards y contenedores |
| Texto secundario | `#666` / `#999` | Subtítulos, descripciones |
| Texto muted | `#a0aec0` | Ítems de navegación sin seleccionar |
| Rojo logout | `#e74c3c` | Botón de cerrar sesión |
| Rojo hover | `#c0392b` | Hover del botón logout |

### Pantalla de login

| Token | Color | Uso |
|-------|-------|-----|
| Fondo | `#020f27` | Fondo oscuro profundo |
| Card | `rgba(13, 27, 42, 0.7)` | Glassmorphism con blur |
| Borde neón | `rgba(0, 212, 255, 0.2)` | Borde sutil de la card |
| Acento neón | `#00d4ff` | Focus de inputs, botón de login |
| Labels | `#a0c4ff` | Etiquetas de los campos |

---

## Convenciones de nomenclatura CSS

Se usa **BEM simplificado** combinado con clases utilitarias descriptivas:

```css
/* Componente */
.sidebar { }
.sidebar-header { }
.sidebar-nav { }
.sidebar-footer { }

/* Estado */
.nav-item.active { }
.sidebar.active { }   /* sidebar abierto en mobile */

/* Modificador descriptivo */
.badge-stock.alto { }
.badge-stock.medio { }
.badge-stock.bajo { }
```

---

## Tipografía

```css
/* Aplicación principal */
font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;

/* Pantalla de login */
font-family: 'Segoe UI', Roboto, sans-serif;

/* SKU y códigos */
font-family: monospace;
```

Tamaños usados en la aplicación:

| Elemento | Tamaño |
|----------|--------|
| Título principal (welcome) | `24px` |
| Navegación sidebar | `14px` |
| Texto de tabla | `13px` |
| Badges y etiquetas | `11px - 12px` |
| SKU | `12px monospace` |

---

## Componentes reutilizables

### Badge de stock
Indica el nivel de inventario de un producto con color semántico:

```css
.badge-stock.alto   → fondo verde claro  (#ecfdf5) texto verde oscuro
.badge-stock.medio  → fondo amarillo     (#fef9c3) texto ámbar
.badge-stock.bajo   → fondo rojo claro   (#fef2f2) texto rojo oscuro
```

### Badge SKU
Muestra el código SKU del producto con estilo monospace:

```css
.sku-badge → fondo gris claro (#f0f2f5), fuente monospace, border-radius 6px
```

### Botones de acción suaves
Para acciones secundarias como restaurar o eliminar definitivamente:

```css
.btn-restaurar           → fondo verde muy claro, texto verde
.btn-eliminar-permanente → fondo amarillo suave, texto ámbar
```

---

## Layout principal

El sistema usa un layout de **sidebar fijo + contenido scrollable**:

```
┌─────────────────────────────────────┐
│  SIDEBAR (240px fijo)  │   MAIN     │
│  background: #1a1a2e   │  flex: 1   │
│                        │  overflow-y│
│  Logo                  │            │
│  Navegación            │  Contenido │
│  ...                   │  del módulo│
│  [Cerrar sesión]       │            │
└─────────────────────────────────────┘
```

```css
.layout  → display: flex
.sidebar → width: 240px, position: fixed, height: 100vh
.main    → margin-left: 240px, padding: 32px
```

---

## Animaciones

```css
/* Cards del dashboard — entrada suave */
@keyframes fadeInUp {
    from { opacity: 0; transform: translateY(10px); }
    to   { opacity: 1; transform: translateY(0); }
}

/* Login — entrada desde abajo */
@keyframes slideUp {
    from { opacity: 0; transform: translateY(20px); }
    to   { opacity: 1; transform: translateY(0); }
}

/* Hover en cards del dashboard */
.card:hover → transform: translateY(-4px), sombra más pronunciada
```

---

## Responsive (Mobile)

El breakpoint principal es `max-width: 768px`. En mobile:

- El sidebar se oculta fuera de pantalla (`left: -100%`) y se muestra con la clase `.active`
- Aparece un botón hamburguesa `.btn-menu`
- El `.main` ocupa el 100% del ancho (`margin-left: 0`)
- Las tablas cambian a layout de tarjetas por fila usando `display: block` en `tr` y `td`
- Las cards del dashboard pasan a una sola columna (`grid-template-columns: 1fr`)
