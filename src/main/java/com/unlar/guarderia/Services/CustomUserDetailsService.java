package com.unlar.guarderia.Services;

import com.unlar.guarderia.Entitites.Usuario;
import com.unlar.guarderia.Repositories.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioRepository repo;
    public CustomUserDetailsService(UsuarioRepository repo) { this.repo = repo; }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario u = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No encontrado"));
        return new org.springframework.security.core.userdetails.User(
                u.getEmail(), 
                u.getPassword(), 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + u.getRole()))
        );
    }
}