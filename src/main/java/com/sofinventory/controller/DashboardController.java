package com.sofinventory.controller;

import com.sofinventory.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Muestra el dashboard si el usuario está autenticado.
 * Redirige al login en caso contrario.
 */
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UsuarioService usuarioService;


    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("token") == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuariosActivos",   usuarioService.contarActivos());
        model.addAttribute("usuariosInactivos", usuarioService.contarInactivos());
        return "dashboard";
    }
}