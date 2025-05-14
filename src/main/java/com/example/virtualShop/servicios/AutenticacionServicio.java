package com.example.virtualShop.servicios;

import com.example.virtualShop.entidades.Usuario;
import com.example.virtualShop.repositorios.UsuarioRepositorio;
import com.example.virtualShop.seguridad.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AutenticacionServicio(UsuarioRepositorio usuarioRepositorio, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String autenticarYGenerarToken(String correo, String contrasena) {
        Usuario usuario = usuarioRepositorio.findByCorreo(correo);
        if (usuario != null && passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            return jwtUtil.generarToken(correo);
        }
        return null;
    }
}
