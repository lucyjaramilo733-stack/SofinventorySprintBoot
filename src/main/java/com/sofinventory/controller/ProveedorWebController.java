package com.sofinventory.controller;

import com.sofinventory.entity.Proveedor;
import com.sofinventory.service.ProveedorService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * Controlador Web para la gestión administrativa de Proveedores.
 * Maneja el ciclo de vida de los proveedores (CRUD) y la lógica de visualización
 * para las vistas de Thymeleaf.
 */
@Controller
@RequestMapping("/proveedores")
@RequiredArgsConstructor
public class ProveedorWebController {

    private final ProveedorService proveedorService;

    /**
     * Lista los proveedores categorizados por su estado operativo.
     * Implementa un control de sesión básico para asegurar que solo usuarios
     * autenticados accedan al panel de gestión.
     */
    @GetMapping
    public String listar(HttpSession session, Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        // Se separan activos de inactivos para facilitar la toma de decisiones en compras
        model.addAttribute("activos", proveedorService.listarActivos());
        model.addAttribute("inactivos", proveedorService.listarInactivos());
        return "proveedores/lista";
    }

    @GetMapping("/nuevo")
    public String formNuevo(HttpSession session, Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        // Cargamos los tipos de documento dinámicamente desde la base de datos
        model.addAttribute("tiposDocumento", proveedorService.listarTiposDocumento());
        return "proveedores/form";
    }

    /**
     * Procesa el registro de un nuevo proveedor.
     * Se utiliza captura de excepciones para manejar errores de integridad de datos
     * (ej. documentos duplicados) y retornar feedback visual al usuario.
     */
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
            // Mapeo manual de parámetros a la entidad para mantener control sobre el objeto persistido
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
            // En caso de error, devolvemos los datos al formulario para no perder la entrada del usuario
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tiposDocumento", proveedorService.listarTiposDocumento());
            return "proveedores/form";
        }
    }
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
    /**
     * Actualiza la información de un proveedor existente.
     * Mantiene la integridad de la relación con el Tipo de Documento mediante su ID.
     */
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
            model.addAttribute("tiposDocumento", proveedorService.listarTiposDocumento());
            return "proveedores/editar";
        }
    }
    /**
     * Aplica un borrado lógico al proveedor para preservar el histórico de compras relacionadas.
     */
    @GetMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        proveedorService.desactivarProveedor(id);
        return "redirect:/proveedores";
    }

    @GetMapping("/reactivar/{id}")
    public String reactivar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        proveedorService.reactivarProveedor(id);
        return "redirect:/proveedores";
    }
    /**
     * Borrado físico del registro.
     * Solo recomendado si el proveedor no tiene transacciones asociadas en el sistema.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        proveedorService.eliminarProveedor(id);
        return "redirect:/proveedores";
    }
}