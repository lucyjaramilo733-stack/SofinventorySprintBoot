package com.sofinventory.controller;

import com.sofinventory.dto.LoginRequest;
import com.sofinventory.dto.LoginResponse;
import com.sofinventory.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador web para la autenticación de usuarios.
 * Gestiona el inicio y cierre de sesión mediante vistas.
 */
@Controller
@RequiredArgsConstructor
public class AuthWebController {

    private final AuthService authService;
    ///Muestra la página de inicio de sesión.
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    ///Procesa el inicio de sesión del usuario.
    ///Valida credenciales y almacena información en la sesión.
    @PostMapping("/auth/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        try {
            LoginRequest request = new LoginRequest(username, password);
            LoginResponse response = authService.login(request);

            session.setAttribute("token", response.getToken());
            session.setAttribute("usuario", response.getNombreCompleto());
            session.setAttribute("username", response.getUsername());
            session.setAttribute("rol", response.getRol());

            return "redirect:/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }
    }
    ///Cierra la sesión del usuario y redirige al login.
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}