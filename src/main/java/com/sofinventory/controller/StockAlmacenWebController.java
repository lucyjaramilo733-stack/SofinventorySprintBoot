package com.sofinventory.controller;

import com.sofinventory.entity.StockAlmacen;
import com.sofinventory.service.StockAlmacenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
/**
 * Controlador web para la gestión de stock en almacenes.
 * Maneja la visualización del inventario, ajustes de stock
 * y configuración de umbrales mínimo y máximo.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockAlmacenWebController {

    private final StockAlmacenService stockAlmacenService;

    // ── GET /stock ────────────────────────────────────────────────────────────
    // Lista principal de stock con filtros por almacén, producto o tipo de alerta
    @GetMapping
    public String lista(
            @RequestParam(required = false) Long almacenId,
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) String alerta,
            Model model) {

        List<StockAlmacen> stocks;

        // Filtro por alerta (agotado, stock bajo o excedido)
        if ("agotado".equals(alerta)) {
            stocks = stockAlmacenService.listarAgotados();
        } else if ("bajo".equals(alerta)) {
            stocks = stockAlmacenService.listarStockBajo();
        } else if ("excedido".equals(alerta)) {
            stocks = stockAlmacenService.listarStockExcedido();
            // Filtro por almacén o producto
        } else if (almacenId != null) {
            stocks = stockAlmacenService.listarPorAlmacen(almacenId);
            model.addAttribute("filtroAlmacenId", almacenId);
        } else if (productoId != null) {
            stocks = stockAlmacenService.listarPorProducto(productoId);
            model.addAttribute("filtroProductoId", productoId);
        } else {
            stocks = stockAlmacenService.listarTodo();
        }

        // Datos para la vista
        model.addAttribute("stocks",        stocks);
        model.addAttribute("filtroAlerta",  alerta);
        model.addAttribute("almacenes",     stockAlmacenService.listarAlmacenes());
        model.addAttribute("productos",     stockAlmacenService.listarProductos());
        model.addAttribute("totalAgotados", stockAlmacenService.contarAgotados());
        model.addAttribute("totalBajo",     stockAlmacenService.contarStockBajo());

        return "stock/lista";
    }

    // ── GET /stock/{id}/ajuste ────────────────────────────────────────────────
    // Muestra el formulario para ajustar (entrada/salida) el stock de un producto
    @GetMapping("/{id}/ajuste")
    public String formularioAjuste(@PathVariable Long id, Model model) {
        model.addAttribute("stock", stockAlmacenService.obtenerPorId(id));
        return "stock/ajuste";
    }

    // ── POST /stock/{id}/ajuste ───────────────────────────────────────────────
    // Procesa el ajuste de stock (entrada o salida) y actualiza la cantidad
    @PostMapping("/{id}/ajuste")
    public String procesarAjuste(
            @PathVariable Long id,
            @RequestParam Integer cantidad,
            @RequestParam String tipoAjuste,
            @RequestParam(required = false) String motivo,
            RedirectAttributes redirectAttributes) {

        try {
            stockAlmacenService.ajustarStock(id, cantidad, tipoAjuste, motivo);
            redirectAttributes.addFlashAttribute("mensaje", "Ajuste aplicado correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "exito");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/stock";
    }

    // ── GET /stock/{id}/umbrales ──────────────────────────────────────────────
    @GetMapping("/{id}/umbrales")
    public String formularioUmbrales(@PathVariable Long id, Model model) {
        model.addAttribute("stock", stockAlmacenService.obtenerPorId(id));
        return "stock/umbrales";
    }

    // ── POST /stock/{id}/umbrales ─────────────────────────────────────────────
    // Muestra el formulario para configurar stock mínimo y máximo
    @PostMapping("/{id}/umbrales")
    public String procesarUmbrales(
            @PathVariable Long id,
            @RequestParam Integer stockMinimo,
            @RequestParam(required = false) Integer stockMaximo,
            RedirectAttributes redirectAttributes) {

        try {
            stockAlmacenService.actualizarUmbrales(id, stockMinimo, stockMaximo);
            redirectAttributes.addFlashAttribute("mensaje", "Umbrales actualizados correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "exito");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/stock";
    }
}
