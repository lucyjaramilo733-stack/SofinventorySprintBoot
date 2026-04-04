package com.sofinventory.controller;

import com.sofinventory.dto.LoginRequest;
import com.sofinventory.dto.LoginResponse;
import com.sofinventory.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador encargado de la autenticación de usuarios.
 * Gestiona el proceso de inicio de sesión en el sistema.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint para autenticar un usuario en el sistema.
     * Recibe las credenciales y retorna la información de acceso.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}