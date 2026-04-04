package com.sofinventory.controller;

import com.sofinventory.entity.Proveedor;
import com.sofinventory.service.ProveedorService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/proveedores")
@RequiredArgsConstructor
public class ProveedorWebController {

    private final ProveedorService proveedorService;

    // Lista proveedores
    @GetMapping
    public String listar(HttpSession session, Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        model.addAttribute("activos", proveedorService.listarActivos());
        model.addAttribute("inactivos", proveedorService.listarInactivos());
        return "proveedores/lista";
    }

    // Formulario nuevo proveedor
    @GetMapping("/nuevo")
    public String formNuevo(HttpSession session, Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        model.addAttribute("tiposDocumento", proveedorService.listarTiposDocumento());
        return "proveedores/form";
    }

    // Guardar nuevo proveedor
    @PostMapping("/guardar")
    public String guardar(@RequestParam String numeroDocumento,
                          @RequestParam String razonSocial,
                          @RequestParam(required = false) String nombreComercial,
                          @RequestParam(required = false) String nombreContacto,
                          @RequestParam(required = false) String cargoContacto,
                          @RequestParam(required = false) String email,
                          @RequestParam(required = false) String telefono,
                          @RequestParam(required = false) String direccion,
                          @RequestParam String pais,
                          @RequestParam(required = false) String departamento,
                          @RequestParam(required = false) String ciudad,
                          @RequestParam String tipoProveedor,
                          @RequestParam(required = false) String observaciones,
                          @RequestParam Long tipoDocumentoId,
                          HttpSession session,
                          Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        try {
            Proveedor proveedor = new Proveedor();
            proveedor.setNumeroDocumento(numeroDocumento);
            proveedor.setRazonSocial(razonSocial);
            proveedor.setNombreComercial(nombreComercial);
            proveedor.setNombreContacto(nombreContacto);
            proveedor.setCargoContacto(cargoContacto);
            proveedor.setEmail(email);
            proveedor.setTelefono(telefono);
            proveedor.setDireccion(direccion);
            proveedor.setPais(pais);
            proveedor.setDepartamento(departamento);
            proveedor.setCiudad(ciudad);
            proveedor.setTipoProveedor(tipoProveedor);
            proveedor.setObservaciones(observaciones);

            proveedorService.crearProveedor(proveedor, tipoDocumentoId);
            return "redirect:/proveedores";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tiposDocumento",
                    proveedorService.listarTiposDocumento());
            return "proveedores/form";
        }
    }

    // Formulario editar proveedor
    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Long id,
                             HttpSession session, Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        model.addAttribute("proveedor", proveedorService.obtenerProveedor(id));
        model.addAttribute("tiposDocumento", proveedorService.listarTiposDocumento());
        return "proveedores/editar";
    }

    // Actualizar proveedor
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @RequestParam String razonSocial,
                             @RequestParam(required = false) String nombreComercial,
                             @RequestParam(required = false) String nombreContacto,
                             @RequestParam(required = false) String cargoContacto,
                             @RequestParam(required = false) String email,
                             @RequestParam(required = false) String telefono,
                             @RequestParam(required = false) String direccion,
                             @RequestParam String pais,
                             @RequestParam(required = false) String departamento,
                             @RequestParam(required = false) String ciudad,
                             @RequestParam String tipoProveedor,
                             @RequestParam(required = false) String observaciones,
                             @RequestParam Long tipoDocumentoId,
                             HttpSession session,
                             Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        try {
            Proveedor datos = new Proveedor();
            datos.setRazonSocial(razonSocial);
            datos.setNombreComercial(nombreComercial);
            datos.setNombreContacto(nombreContacto);
            datos.setCargoContacto(cargoContacto);
            datos.setEmail(email);
            datos.setTelefono(telefono);
            datos.setDireccion(direccion);
            datos.setPais(pais);
            datos.setDepartamento(departamento);
            datos.setCiudad(ciudad);
            datos.setTipoProveedor(tipoProveedor);
            datos.setObservaciones(observaciones);

            proveedorService.editarProveedor(id, datos, tipoDocumentoId);
            return "redirect:/proveedores";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("proveedor", proveedorService.obtenerProveedor(id));
            model.addAttribute("tiposDocumento",
                    proveedorService.listarTiposDocumento());
            return "proveedores/editar";
        }
    }

    // Desactivar proveedor
    @GetMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        proveedorService.desactivarProveedor(id);
        return "redirect:/proveedores";
    }

    // Reactivar proveedor
    @GetMapping("/reactivar/{id}")
    public String reactivar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        proveedorService.reactivarProveedor(id);
        return "redirect:/proveedores";
    }

    // Eliminar permanentemente
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        proveedorService.eliminarProveedor(id);
        return "redirect:/proveedores";
    }
}