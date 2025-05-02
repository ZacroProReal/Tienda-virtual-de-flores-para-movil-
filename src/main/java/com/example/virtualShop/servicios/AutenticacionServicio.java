package com.example.virtualShop.servicios;

import com.example.virtualShop.entidades.Usuario;
import com.example.virtualShop.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AutenticacionServicio(UsuarioRepositorio usuarioRepositorio, PasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean autenticar(String correo, String contrasena) {
        Usuario usuario = usuarioRepositorio.findByCorreo(correo);
        if (usuario != null) {
            return passwordEncoder.matches(contrasena, usuario.getContrasena());
        }
        return false;
    }
}
