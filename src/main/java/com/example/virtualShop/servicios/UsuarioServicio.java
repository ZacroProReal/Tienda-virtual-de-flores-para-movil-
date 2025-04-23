package com.example.virtualShop.servicios;

import com.example.virtualShop.dto.UsuarioDto;
import com.example.virtualShop.entidades.Carrito;
import com.example.virtualShop.entidades.Usuario;
import com.example.virtualShop.repositorios.CarritoRepositorio;
import com.example.virtualShop.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final CarritoRepositorio carritoRepositorio;

    @Autowired
    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio, CarritoRepositorio carritoRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.carritoRepositorio = carritoRepositorio;
    }

    public UsuarioDto registrarUsuario(UsuarioDto usuarioDto) throws IOException {
        Usuario usuario = Usuario.builder()
                .nombre(usuarioDto.nombre())
                .apellido(usuarioDto.apellido())
                .telefono(usuarioDto.telefono())
                .correo(usuarioDto.correo())
                .contrasena(usuarioDto.contrasena())
                .fechaNacimiento(usuarioDto.fechaNacimiento())
                .rol(2) // Aseguramos que es un usuario con rol 2
                .build();

        Usuario usuarioGuardado = usuarioRepositorio.save(usuario);
        //Asignacion de carrito a cada usuario Registrado
        if (usuarioGuardado.getId() > 0) {
            if (usuarioGuardado.getRol() == 2) {
                Carrito carrito = Carrito.builder()
                        .usuario(usuarioGuardado)
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

}
