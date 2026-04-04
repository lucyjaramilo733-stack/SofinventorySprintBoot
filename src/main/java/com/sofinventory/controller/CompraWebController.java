package com.sofinventory.controller;

import com.sofinventory.service.CompraService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
/**
 * Controlador web para la gestión de compras.
 * Maneja las solicitudes HTTP relacionadas con el módulo de compras,
 * incluyendo listado, creación, visualización de detalles y anulación.
 */
@Controller
@RequestMapping("/compras")
@RequiredArgsConstructor
public class CompraWebController {

    private final CompraService compraService;

    // ── Lista de compras ──────────────────────────────────────────────────────
    @GetMapping
    public String listar(HttpSession session, Model model) {
        if (session.getAttribute("token") == null) return "redirect:/login";
        model.addAttribute("compras", compraService.listarCompras());
        return "compras/lista";
    }

    // ── Formulario nueva compra ───────────────────────────────────────────────
    @GetMapping("/nueva")
    public String formNueva(HttpSession session, Model model) {
        if (session.getAttribute("token") == null) return "redirect:/login";
        model.addAttribute("proveedores", compraService.listarProveedores());
        model.addAttribute("productos",   compraService.listarProductos());
        model.addAttribute("almacenes",   compraService.listarAlmacenes());
        return "compras/form";
    }

    // ── Guardar nueva compra ──────────────────────────────────────────────────
    @PostMapping("/guardar")
    public String guardar(
            @RequestParam String numeroFactura,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaCompra,
            @RequestParam String tipoCompra,
            @RequestParam Long proveedorId,
            @RequestParam List<Long>       productosIds,
            @RequestParam List<Long>       almacenesIds,
            @RequestParam List<Integer>    cantidades,
            @RequestParam List<BigDecimal> costosUnitarios,
            @RequestParam List<BigDecimal> ivasPorcentaje,
            @RequestParam(required = false) List<String>    codigosLote,
            @RequestParam(required = false) List<LocalDate> fechasVencimiento,
            HttpSession session, Model model) {

        if (session.getAttribute("token") == null) return "redirect:/login";

        try {

            String username = (String) session.getAttribute("username");
            compraService.registrarCompra(
                    numeroFactura, fechaCompra, tipoCompra, proveedorId, username,
                    productosIds, almacenesIds, cantidades, costosUnitarios,
                    ivasPorcentaje, codigosLote, fechasVencimiento);
            return "redirect:/compras";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("proveedores", compraService.listarProveedores());
            model.addAttribute("productos",   compraService.listarProductos());
            model.addAttribute("almacenes",   compraService.listarAlmacenes());
            return "compras/form";
        }
    }

    // ── Detalle de una compra ─────────────────────────────────────────────────
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("token") == null) return "redirect:/login";
        model.addAttribute("compra",   compraService.obtenerCompra(id));
        model.addAttribute("detalles", compraService.obtenerDetalles(id));
        return "compras/detalle";
    }

    // ── Anular compra ─────────────────────────────────────────────────────────
    @GetMapping("/anular/{id}")
    public String anular(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("token") == null) return "redirect:/login";
        try {
            compraService.anularCompra(id);
            return "redirect:/compras";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("compras", compraService.listarCompras());
            return "compras/lista";
        }
    }
}