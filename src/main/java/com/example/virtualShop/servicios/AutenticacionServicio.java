package com.example.virtualShop.servicios;

import com.example.virtualShop.entidades.EstadoUsuario;
import com.example.virtualShop.entidades.Usuario;
import com.example.virtualShop.excepciones.UsuarioEliminadoException;
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
        if (usuario != null) {
            if (usuario.getEstado() == EstadoUsuario.ELIMINADO) {
                throw new UsuarioEliminadoException("El usuario ha sido eliminado. Por favor, restaure la cuenta para iniciar sesión.");
            }

            if (passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                return jwtUtil.generarToken(correo);
            }
        }
        return null;
    }


    public String solicitarTokenRestauracion(String correo) {
        Usuario usuario = usuarioRepositorio.findByCorreo(correo);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        if (usuario.getEstado() != EstadoUsuario.ELIMINADO) {
            throw new IllegalArgumentException("La cuenta ya está activa");
        }

        // Generar token JWT para restauración
        return jwtUtil.generarTokenRestauracion(correo);
    }

    public void restaurarCuentaConToken(String token) {
        if (!jwtUtil.validarTokenRestauracion(token)) {
            throw new IllegalArgumentException("Token de restauración inválido o expirado");
        }

        String correo = jwtUtil.extraerCorreo(token);
        Usuario usuario = usuarioRepositorio.findByCorreo(correo);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        if (usuario.getEstado() != EstadoUsuario.ELIMINADO) {
            throw new IllegalArgumentException("La cuenta ya está activa");
        }

        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuarioRepositorio.save(usuario);
    }
}
