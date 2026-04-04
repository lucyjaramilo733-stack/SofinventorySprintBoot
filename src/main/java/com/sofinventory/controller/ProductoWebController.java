package com.sofinventory.controller;

import com.sofinventory.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
/**
 * Controlador Web para la gestión de Productos.
 * Maneja el ciclo de vida de los productos (crear, listar, editar, eliminar, restaurar)
 * y prepara los datos necesarios para las vistas Thymeleaf.
 */
@Controller
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoWebController {

    private final ProductoService productoService;

    // Listar productos activos y eliminados
    @GetMapping
    public String listar(HttpSession session, Model model) {
        if (session.getAttribute("token") == null) return "redirect:/login";
        model.addAttribute("activos", productoService.listarActivos());
        model.addAttribute("eliminados", productoService.listarEliminados());
        return "productos/lista";
    }

    // Formulario para nuevo producto
    @GetMapping("/nuevo")
    public String formNuevo(HttpSession session, Model model) {
        if (session.getAttribute("token") == null) return "redirect:/login";
        model.addAttribute("categorias", productoService.listarCategorias());
        return "productos/form";
    }

    // Guardar nuevo producto
    @PostMapping("/guardar")
    public String guardar(@RequestParam String sku,
                          @RequestParam String nombre,
                          @RequestParam(required = false) String marca,
                          @RequestParam(required = false) String referencia,
                          @RequestParam String unidadMedida,
                          @RequestParam(required = false) String descripcion,
                          @RequestParam(required = false) String observaciones,
                          @RequestParam Long categoriaId,
                          HttpSession session, Model model) {

        if (session.getAttribute("token") == null) return "redirect:/login";
        try {
            String username = (String) session.getAttribute("username");
            productoService.crearProducto(sku, nombre, marca, referencia, unidadMedida, username, descripcion, observaciones, categoriaId);
            return "redirect:/productos";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categorias", productoService.listarCategorias());
            return "productos/form";
        }
    }

    // Formulario para editar producto
    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("token") == null) return "redirect:/login";
        model.addAttribute("producto", productoService.obtenerProducto(id));
        model.addAttribute("categorias", productoService.listarCategorias());
        return "productos/editar";
    }
    // Actualizar producto existente
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @RequestParam String sku,
                             @RequestParam String nombre,
                             @RequestParam(required = false) String marca,
                             @RequestParam(required = false) String referencia,
                             @RequestParam String unidadMedida,
                             @RequestParam(required = false) String descripcion,
                             @RequestParam(required = false) String observaciones,
                             @RequestParam Long categoriaId,
                             HttpSession session, Model model) {

        if (session.getAttribute("token") == null) return "redirect:/login";
        try {
            productoService.editarProducto(id, sku, nombre, marca, referencia, unidadMedida,
                    descripcion, observaciones, categoriaId);
            return "redirect:/productos";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("producto", productoService.obtenerProducto(id));
            model.addAttribute("categorias", productoService.listarCategorias());
            return "productos/editar";
        }
    }
    // Eliminación lógica de producto
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) return "redirect:/login";
        productoService.eliminarProducto(id);
        return "redirect:/productos";
    }
    // Restaurar producto eliminado
    @GetMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) return "redirect:/login";
        productoService.restaurarProducto(id);
        return "redirect:/productos";
    }

    // Eliminación permanente de producto
    @GetMapping("/eliminar-permanente/{id}")
    public String eliminarPermanente(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) return "redirect:/login";
        productoService.eliminarPermanente(id);
        return "redirect:/productos";
    }
}
