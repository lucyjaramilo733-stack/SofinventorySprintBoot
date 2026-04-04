package com.sofinventory.controller;

import com.sofinventory.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para el dashboard principal.
 * Muestra métricas y resúmenes del sistema como usuarios activos,
 * proveedores, categorías, almacenes y productos.
 */
@Controller
@RequiredArgsConstructor
public class DashboardController {

    // ── Servicios necesarios ──────────────────────────────────────────────────
    private final UsuarioService    usuarioService;
    private final ProveedorService  proveedorService;
    private final CategoriaService  categoriaService;
    private final AlmacenService    almacenService;
    private final ProductoService   productoService;
    private final CompraService     compraService;

    /**
     * Muestra el dashboard principal con todas las métricas del sistema.
     * Verifica que el usuario tenga sesión activa antes de mostrar los datos.
     *
     * @param session HttpSession para verificar autenticación
     * @param model   Modelo para pasar atributos a la vista
     * @return Nombre de la vista dashboard o redirección al login
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Verificar autenticación
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }

        // ── Cargar todas las métricas para las tarjetas del dashboard ──────────

        // Usuarios
        model.addAttribute("usuariosActivos",   usuarioService.contarActivos());
        model.addAttribute("usuariosInactivos", usuarioService.contarInactivos());

        // Proveedores
        model.addAttribute("proveedoresActivos", proveedorService.contarActivos());

        // Categorías
        model.addAttribute("categoriasActivas", categoriaService.contarActivas());

        // Almacenes
        model.addAttribute("almacenesActivos", almacenService.contarActivos());

        // Productos

        // ✅ Productos - El nombre DEBE coincidir con el HTML: "productoActivos"
        model.addAttribute("productoActivos", productoService.contarActivos());

        // ✅ Compras - Total de compras registradas
        model.addAttribute("totalCompras", compraService.contarTotalCompras());

        // ✅ Ventas - Para cuando implementes el módulo
        // model.addAttribute("totalVentas", ventaService.contarTotalVentas());
        model.addAttribute("totalVentas", 0); // Temporal mientras no existe

        return "dashboard";
    }
}
