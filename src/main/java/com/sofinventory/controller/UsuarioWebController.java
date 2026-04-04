package com.sofinventory.controller;

import com.sofinventory.entity.Usuario;
import com.sofinventory.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador web para la gestión de usuarios.
 * Permite listar, crear, editar y administrar el estado de los usuarios.
 */
@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioWebController {

    private final UsuarioService usuarioService;

    // Lista de usuarios activos
    @GetMapping
    public String listar(HttpSession session, Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarios", usuarioService.listarActivos());
        model.addAttribute("inactivos", usuarioService.listarInactivos());
        return "usuarios/lista";
    }

    // Formulario crear usuario
    @GetMapping("/nuevo")
    public String formNuevo(HttpSession session, Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        model.addAttribute("roles", usuarioService.listarRoles());
        model.addAttribute("tiposDocumento", usuarioService.listarTiposDocumento());
        return "usuarios/form";
    }

    // Guardar nuevo usuario
    @PostMapping("/guardar")
    public String guardar(@RequestParam String nombreCompleto,
                          @RequestParam String email,
                          @RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String confirmarPassword,
                          @RequestParam String numeroDocumento,
                          @RequestParam Long rolId,
                          @RequestParam Long tipoDocumentoId,
                          HttpSession session,
                          Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        try {
            if (!password.equals(confirmarPassword)) {
                throw new RuntimeException("Las contraseñas no coinciden");
            }
            if (password.length() > 8) {
                throw new RuntimeException("La contraseña no puede tener más de 8 caracteres");
            }

            Usuario usuario = new Usuario();
            usuario.setNombreCompleto(nombreCompleto);
            usuario.setEmail(email);
            usuario.setUsername(username);
            usuario.setNumeroDocumento(numeroDocumento);

            usuarioService.crearUsuario(usuario, rolId, tipoDocumentoId, password);
            return "redirect:/usuarios";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", usuarioService.listarRoles());
            model.addAttribute("tiposDocumento", usuarioService.listarTiposDocumento());
            return "usuarios/form";
        }
    }

    // Formulario editar usuario
    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Long id,
                             HttpSession session, Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuarioService.obtenerUsuario(id));
        model.addAttribute("roles", usuarioService.listarRoles());
        model.addAttribute("tiposDocumento", usuarioService.listarTiposDocumento());
        return "usuarios/editar";
    }

    // Guardar edición
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @RequestParam String nombreCompleto,
                             @RequestParam String email,
                             @RequestParam Long rolId,
                             @RequestParam Long tipoDocumentoId,
                             HttpSession session,
                             Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        try {
            usuarioService.editarUsuario(id, nombreCompleto,
                    email, rolId, tipoDocumentoId);
            return "redirect:/usuarios";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuario", usuarioService.obtenerUsuario(id));
            model.addAttribute("roles", usuarioService.listarRoles());
            model.addAttribute("tiposDocumento", usuarioService.listarTiposDocumento());
            return "usuarios/editar";
        }
    }

    // Desactivar usuario
    @GetMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        usuarioService.desactivarUsuario(id);
        return "redirect:/usuarios";
    }

    // Reactivar usuario
    @GetMapping("/reactivar/{id}")
    public String reactivar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        usuarioService.reactivarUsuario(id);
        return "redirect:/usuarios";
    }

    // Eliminar permanentemente
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        usuarioService.eliminarUsuario(id);
        return "redirect:/usuarios";
    }
}