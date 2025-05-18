package com.example.virtualShop.servicios;

import com.example.virtualShop.dto.UsuarioDto;
import com.example.virtualShop.entidades.Carrito;
import com.example.virtualShop.entidades.EstadoCarrito;
import com.example.virtualShop.entidades.Usuario;
import com.example.virtualShop.repositorios.CarritoRepositorio;
import com.example.virtualShop.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final CarritoRepositorio carritoRepositorio;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio, CarritoRepositorio carritoRepositorio, PasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.carritoRepositorio = carritoRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioDto registrarUsuario(UsuarioDto usuarioDto) throws IOException {

        if (usuarioRepositorio.existsByCorreo(usuarioDto.correo())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo.");
        }

        String contrasenaCodificada = passwordEncoder.encode(usuarioDto.contrasena());
        Usuario usuario = Usuario.builder()
                .nombre(usuarioDto.nombre())
                .apellido(usuarioDto.apellido())
                .telefono(usuarioDto.telefono())
                .correo(usuarioDto.correo())
                .contrasena(contrasenaCodificada)
                .fechaNacimiento(usuarioDto.fechaNacimiento())
                .rol(2)
                .build();

        Usuario usuarioGuardado = usuarioRepositorio.save(usuario);
        //Asignacion de carrito a cada usuario Registrado
        if (usuarioGuardado.getId() > 0) {
            if (usuarioGuardado.getRol() == 2) {
                Carrito carrito = Carrito.builder()
                        .usuario(usuarioGuardado)
                        .fechaCreacion(LocalDateTime.now())
                        .estado(EstadoCarrito.ACTIVO)
                        .cantidadGeneralProduc(0)
                        .costoGenearl(0)
                        .build();
                carritoRepositorio.save(carrito);
            }
            return usuarioDto;
        } else {
            return null;
        }
    }
    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        usuarioRepositorio.delete(usuario);
    }

    public Usuario modificarUsuario(Long id, Usuario usuarioActualizado) {
        String contrasenaCodificada = passwordEncoder.encode(usuarioActualizado.getContrasena());
        Usuario usuarioExistente = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellido(usuarioActualizado.getApellido());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
        usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
        usuarioExistente.setContrasena(contrasenaCodificada);
        usuarioExistente.setFechaNacimiento(usuarioActualizado.getFechaNacimiento());

        return usuarioRepositorio.save(usuarioExistente);
    }

    public Usuario buscarUsuario(String nombre) {
        return usuarioRepositorio.findByNombre(nombre);
    }

}
