package com.sofinventory.controller;

import com.sofinventory.entity.TipoControlCategoria;
import com.sofinventory.service.CategoriaService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * Controlador Web para la gestión administrativa de Categorías.
 * Maneja el ciclo de vida de las categorías (crear, listar, editar, eliminar, restaurar)
 * y la lógica de visualización para las vistas Thymeleaf.
 */
@Controller
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaWebController {

    private final CategoriaService categoriaService;

    // Lista de categorías
    @GetMapping
    public String listar(HttpSession session, Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        model.addAttribute("activas", categoriaService.listarActivas());
        model.addAttribute("eliminadas", categoriaService.listarEliminadas());
        return "categorias/lista";
    }

    // Formulario nueva categoría
    @GetMapping("/nueva")
    public String formNueva(HttpSession session, Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        model.addAttribute("tiposControl", categoriaService.listarTiposControl());
        return "categorias/form";
    }

    // Guardar nueva categoría
    @PostMapping("/guardar")
    public String guardar(@RequestParam String nombre,
                          @RequestParam String tipoControl,
                          @RequestParam(required = false) String descripcion,
                          HttpSession session, Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        try {
            TipoControlCategoria tipo = TipoControlCategoria.valueOf(tipoControl);
            categoriaService.crearCategoria(nombre, tipo, descripcion);
            return "redirect:/categorias";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tiposControl", categoriaService.listarTiposControl());
            return "categorias/form";
        }
    }

    // Formulario editar categoría
    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        model.addAttribute("categoria", categoriaService.obtenerCategoria(id));
        model.addAttribute("tiposControl", categoriaService.listarTiposControl());
        return "categorias/editar";
    }

    // Actualizar categoría
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @RequestParam String nombre,
                             @RequestParam String tipoControl,
                             @RequestParam(required = false) String descripcion,
                             HttpSession session, Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        try {
            TipoControlCategoria tipo = TipoControlCategoria.valueOf(tipoControl);
            categoriaService.editarCategoria(id, nombre, tipo, descripcion);
            return "redirect:/categorias";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categoria", categoriaService.obtenerCategoria(id));
            model.addAttribute("tiposControl", categoriaService.listarTiposControl());
            return "categorias/editar";
        }
    }

    // Eliminación lógica
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        categoriaService.eliminarCategoria(id);
        return "redirect:/categorias";
    }

    // Restaurar categoría
    @GetMapping("/restaurar/{id}")
    public String restaurar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        categoriaService.restaurarCategoria(id);
        return "redirect:/categorias";
    }

    // Eliminar permanentemente
    @GetMapping("/eliminar-permanente/{id}")
    public String eliminarPermanente(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        categoriaService.eliminarPermanente(id);
        return "redirect:/categorias";
    }
}