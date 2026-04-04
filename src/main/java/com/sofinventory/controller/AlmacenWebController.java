package com.sofinventory.controller;

import com.sofinventory.entity.Almacen;
import com.sofinventory.service.AlmacenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Controlador Web para la gestión administrativa de Categorías.
 * Se encarga de manejar el ciclo de vida completo de las categorías:
 * creación, listado, edición, eliminación lógica, restauración y eliminación permanente.
 * Además, prepara los datos necesarios para las vistas Thymeleaf.
 */
@Controller
@RequestMapping("/almacenes")
@RequiredArgsConstructor
public class AlmacenWebController {

    private final AlmacenService almacenService;

    // Lista de categorías activas y eliminadas
    @GetMapping
    public String listar(HttpSession session, Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        model.addAttribute("activos", almacenService.listarActivos());
        model.addAttribute("inactivos", almacenService.listarInactivos());
        return "almacenes/lista";
    }

    // Formulario para nueva categoría
    @GetMapping("/nuevo")
    public String formNuevo(HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        return "almacenes/form";
    }

    // Guardar nuevo almacén
    @PostMapping("/guardar")
    public String guardar(@RequestParam String nombre,
                          @RequestParam String codigo,
                          @RequestParam(required = false) String direccion,
                          @RequestParam(required = false) String responsable,
                          @RequestParam(required = false) String telefono,
                          @RequestParam(required = false) String capacidad,
                          @RequestParam(required = false) String notas,
                          HttpSession session,
                          Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        try {
            Almacen almacen = new Almacen();
            almacen.setNombre(nombre);
            almacen.setCodigo(codigo);
            almacen.setDireccion(direccion);
            almacen.setResponsable(responsable);
            almacen.setTelefono(telefono);
            almacen.setCapacidad(capacidad != null && !capacidad.isBlank()
                    ? new BigDecimal(capacidad) : null);
            almacen.setNotas(notas);

            almacenService.crearAlmacen(almacen);
            return "redirect:/almacenes";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "almacenes/form";
        }
    }

    // Formulario editar almacén
    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Long id,
                             HttpSession session, Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        model.addAttribute("almacen", almacenService.obtenerAlmacen(id));
        return "almacenes/editar";
    }

    // Actualizar almacén
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @RequestParam String nombre,
                             @RequestParam(required = false) String direccion,
                             @RequestParam(required = false) String responsable,
                             @RequestParam(required = false) String telefono,
                             @RequestParam(required = false) String capacidad,
                             @RequestParam(required = false) String notas,
                             HttpSession session,
                             Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        try {
            Almacen datos = new Almacen();
            datos.setNombre(nombre);
            datos.setDireccion(direccion);
            datos.setResponsable(responsable);
            datos.setTelefono(telefono);
            datos.setCapacidad(capacidad != null && !capacidad.isBlank()
                    ? new BigDecimal(capacidad) : null);
            datos.setNotas(notas);

            almacenService.editarAlmacen(id, datos);
            return "redirect:/almacenes";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("almacen", almacenService.obtenerAlmacen(id));
            return "almacenes/editar";
        }
    }

    // Desactivar almacén
    @GetMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        almacenService.desactivarAlmacen(id);
        return "redirect:/almacenes";
    }

    // Reactivar almacén
    @GetMapping("/reactivar/{id}")
    public String reactivar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        almacenService.reactivarAlmacen(id);
        return "redirect:/almacenes";
    }

    // Eliminar permanentemente
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        almacenService.eliminarAlmacen(id);
        return "redirect:/almacenes";
    }
}