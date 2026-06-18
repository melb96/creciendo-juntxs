package com.unlar.guarderia.Controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    // Como permitimos el acceso en SecurityConfig, este es el único punto público.
    @PostMapping("/registrar")
    public String registrar(@RequestBody Usuario usuario) {
        return usuarioService.registrarUsuario(usuario);
    }

    // ELIMINADO: El método @PostMapping("/login") ya no existe aquí.
    // Spring Security intercepta el /login automáticamente, por lo que 
    // no necesitas este método en el controlador.
}