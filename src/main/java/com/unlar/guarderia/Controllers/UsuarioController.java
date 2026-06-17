package com.unlar.guarderia.Controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unlar.guarderia.Entitites.Usuario;
import com.unlar.guarderia.Services.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Endpoint para registrar un usuario: http://localhost:8080/api/usuarios/registrar
    @PostMapping("/registrar")
    public String registrar(@RequestBody Usuario usuario) {
        return usuarioService.registrarUsuario(usuario);
    }

    // Endpoint para el login: http://localhost:8080/api/usuarios/login
    // Pasamos los parámetros de forma simple por la URL o formulario para probar rápido
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        return usuarioService.iniciarSesion(email, password);
    }

}
