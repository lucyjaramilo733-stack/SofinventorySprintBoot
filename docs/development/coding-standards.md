# 📋 Estándares de Código — SOFInventory

Este documento define las convenciones de escritura de código Java aplicadas en el proyecto. Los ejemplos están tomados directamente del código fuente.

---

## 1. Estructura de paquetes

```
com.sofinventory/
├── controller/     ← controladores web y REST
├── entity/         ← entidades JPA (mapeo a tablas)
├── repository/     ← interfaces Spring Data JPA
├── service/        ← lógica de negocio
├── security/       ← configuración JWT y Spring Security
└── dto/            ← objetos de transferencia de datos
```

La regla es que **ninguna capa se salta otra**. Un controller nunca llama directamente a un repository — siempre pasa por el service.

---

## 2. Nomenclatura

| Elemento | Convención | Ejemplo real del proyecto |
|----------|-----------|--------------------------|
| Clases | PascalCase | `ProductoService`, `CompraWebController` |
| Métodos | camelCase | `registrarCompra()`, `listarActivos()` |
| Variables | camelCase | `subtotalCompra`, `fechaActualizacion` |
| Parámetros | camelCase | `usernameRegistra`, `proveedorId` |
| Tablas BD | snake_case | `stock_almacen`, `detalle_compra` |
| Columnas BD | snake_case | `numero_factura`, `fecha_eliminacion` |
| Constantes | UPPER_SNAKE_CASE | `JWT_SECRET` |

---

## 3. Anotaciones utilizadas

### Entidades (`@Entity`)

```java
@Data               // Lombok: genera getters, setters, toString, equals
@NoArgsConstructor  // Lombok: constructor vacío requerido por JPA
@AllArgsConstructor // Lombok: constructor con todos los campos
@Entity
@Table(name = "productos")
public class Producto { }
```

### Servicios (`@Service`)

```java
@Service
@RequiredArgsConstructor  // Lombok: inyección por constructor (recomendado sobre @Autowired)
public class ProductoService {

    private final ProductoRepository productoRepository; // inyectado por constructor
}
```

### Controladores web (`@Controller`)

```java
@Controller
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoWebController { }
```

### Controladores REST (`@RestController`)

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController { }
```

### Configuración y seguridad

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig { }

@Component
public class JwtUtils { }
```

---

## 4. Inyección de dependencias

El proyecto usa **inyección por constructor** en todos los services y controllers, habilitada por la anotación `@RequiredArgsConstructor` de Lombok. Esta es la forma recomendada en Spring Boot moderno.

```java
// ✅ Correcto — usado en el proyecto
@RequiredArgsConstructor
public class CompraService {
    private final CompraRepository        compraRepository;
    private final ProveedorRepository     proveedorRepository;
    private final ProductoRepository      productoRepository;
}

// ❌ Evitar — inyección por campo
@Autowired
private CompraRepository compraRepository;
```

---

## 5. Manejo de errores en servicios

Cuando una entidad no se encuentra, se lanza `RuntimeException` con mensaje descriptivo. Esto se aplica de forma consistente en todos los services:

```java
// Patrón usado en el proyecto
public Producto obtenerProducto(Long id) {
    return productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
}

Proveedor proveedor = proveedorRepository.findById(proveedorId)
        .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

Usuario registradoPor = usuarioRepository.findByUsername(usernameRegistra)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
```

---

## 6. Transacciones

Los métodos de service que realizan múltiples operaciones sobre la base de datos se anotan con `@Transactional` para garantizar consistencia:

```java
// Ejemplo: registrar compra actualiza cabecera, detalles y stock en una sola transacción
@Transactional
public Compra registrarCompra(...) { }

// Ejemplo: anular compra revierte el stock de todos los detalles
@Transactional
public void anularCompra(Long id) { }
```

---

## 7. Soft delete (eliminación lógica)

Las entidades principales no se eliminan físicamente. Se marca `fechaEliminacion` con la fecha actual y `fechaActualizacion` se actualiza:

```java
// Eliminar (soft delete)
public void eliminarProducto(Long id) {
    Producto producto = obtenerProducto(id);
    producto.setFechaEliminacion(ZonedDateTime.now());
    producto.setFechaActualizacion(ZonedDateTime.now());
    productoRepository.save(producto);
}

// Restaurar
public void restaurarProducto(Long id) {
    Producto producto = obtenerProducto(id);
    producto.setFechaEliminacion(null);
    producto.setFechaActualizacion(ZonedDateTime.now());
    productoRepository.save(producto);
}
```

Los repositorios filtran por este campo:

```java
// Activos: sin fecha de eliminación
findByFechaEliminacionIsNull()

// Eliminados: con fecha de eliminación
findByFechaEliminacionIsNotNull()
```

---

## 8. Fechas y zonas horarias

Todos los campos de fecha usan `ZonedDateTime` para soportar zonas horarias correctamente. Se evita el uso de `Date` o `LocalDateTime` sin zona:

```java
// ✅ Correcto — usado en el proyecto
private ZonedDateTime fechaCreacion = ZonedDateTime.now();
private ZonedDateTime fechaActualizacion = ZonedDateTime.now();

// Para fechas de negocio sin hora (ej: fecha de compra)
private LocalDate fechaCompra = LocalDate.now();
```

---

## 9. Seguridad — configuración JWT

Las propiedades sensibles se leen desde `application.properties` usando `@Value`:

```java
@Value("${jwt.secret}")
private String jwtSecret;

@Value("${jwt.expiration}")
private Long jwtExpiration;
```

El algoritmo de cifrado de contraseñas es **BCrypt**:

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

---

## 10. Validaciones de negocio

Las validaciones de reglas de negocio se realizan en el service **antes de persistir**, lanzando excepción con mensaje claro:

```java
// Validar SKU único al crear
if (productoRepository.existsBySku(sku)) {
    throw new RuntimeException("Ya existe un producto con ese SKU");
}

// Validar SKU único al editar (excluyendo el propio registro)
if (productoRepository.existsBySkuAndIdNot(sku, id)) {
    throw new RuntimeException("Ya existe un producto con ese SKU");
}

// Validar factura única por proveedor
if (compraRepository.existsByNumeroFacturaAndProveedorId(numeroFactura, proveedorId)) {
    throw new RuntimeException("Ya existe una compra con ese número de factura para este proveedor");
}
```

---

## 11. Cálculos con dinero

Para valores monetarios se usa siempre `BigDecimal` con escala explícita y `RoundingMode.HALF_UP`. Se evita `double` o `float` para evitar errores de precisión:

```java
// ✅ Correcto — usado en CompraService
BigDecimal subtotalLinea = costo
        .multiply(BigDecimal.valueOf(cantidad))
        .setScale(4, RoundingMode.HALF_UP);

BigDecimal ivaLinea = subtotalLinea
        .multiply(ivaPct)
        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
```

Las columnas monetarias en BD tienen precisión `(16, 4)`:

```java
@Column(name = "subtotal", nullable = false, precision = 16, scale = 4)
private BigDecimal subtotal = BigDecimal.ZERO;
```
