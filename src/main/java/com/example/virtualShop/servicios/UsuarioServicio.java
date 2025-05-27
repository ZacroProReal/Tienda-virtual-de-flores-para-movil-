package com.example.virtualShop.servicios;

import com.example.virtualShop.dto.UsuarioDto;
import com.example.virtualShop.dto.UsuarioPerfilDto;
import com.example.virtualShop.entidades.Carrito;
import com.example.virtualShop.entidades.EstadoCarrito;
import com.example.virtualShop.entidades.EstadoUsuario;
import com.example.virtualShop.entidades.Usuario;
import com.example.virtualShop.repositorios.CarritoRepositorio;
import com.example.virtualShop.repositorios.UsuarioRepositorio;
import com.example.virtualShop.seguridad.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.virtualShop.entidades.EstadoUsuario.ACTIVO;

@Service
public class UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final CarritoRepositorio carritoRepositorio;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
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
                .estado(ACTIVO)
                .rol(2)
                .build();

        Usuario usuarioGuardado = usuarioRepositorio.save(usuario);

        // Asignacion de carrito a cada usuario Registrado
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

    // Nuevo método para buscar usuario por correo y estado activo o modificado
    public Usuario buscarPorCorreo(String correo) {
        List<EstadoUsuario> estadosPermitidos = List.of(EstadoUsuario.ACTIVO, EstadoUsuario.MODIFICADO);
        return usuarioRepositorio.findByCorreoAndEstadoIn(correo, estadosPermitidos)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + correo));
    }

    @Transactional
    // Modificar usuario por correo (no por id)
    public Usuario modificarUsuarioPorCorreo(String correo, Usuario usuarioActualizado) {
        Usuario usuarioExistente = buscarPorCorreo(correo);

        String contrasenaCodificada = passwordEncoder.encode(usuarioActualizado.getContrasena());

        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellido(usuarioActualizado.getApellido());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
        usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
        usuarioExistente.setEstado(EstadoUsuario.MODIFICADO);
        usuarioExistente.setContrasena(contrasenaCodificada);
        usuarioExistente.setFechaNacimiento(usuarioActualizado.getFechaNacimiento());

        return usuarioRepositorio.save(usuarioExistente);
    }
    @Transactional
    // Eliminar usuario por correo (sin id)
    public Usuario eliminarUsuarioPorCorreo(String correo) {
        Usuario usuario = buscarPorCorreo(correo);
        usuario.setEstado(EstadoUsuario.ELIMINADO);
        return usuarioRepositorio.save(usuario);
    }
    @Transactional
    public List<Usuario> listarUsuarios() {
        List<EstadoUsuario> estadosPermitidos = List.of(EstadoUsuario.ACTIVO, EstadoUsuario.MODIFICADO);
        return usuarioRepositorio.findByEstadoIn(estadosPermitidos);
    }
    @Transactional
    public Usuario buscarUsuario(String nombre) {
        List<EstadoUsuario> estadosPermitidos = List.of(EstadoUsuario.ACTIVO, EstadoUsuario.MODIFICADO);
        return usuarioRepositorio.findByNombreAndEstadoIn(nombre, estadosPermitidos);
    }
    @Transactional
    public UsuarioPerfilDto obtenerPerfilDesdeToken(String token) {
        Usuario usuario = obtenerUsuarioDesdeToken(token);
        return new UsuarioPerfilDto(
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getCorreo(),
                usuario.getTelefono(),
                usuario.getFechaNacimiento()
        );
    }
    @Transactional
    public Usuario obtenerUsuarioDesdeToken(String token) {
        String tokenLimpio = token.replace("Bearer ", "").trim();
        String correo = jwtUtil.extraerCorreo(tokenLimpio);
        Usuario usuario = usuarioRepositorio.findByCorreo(correo);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado con el token");
        }
        return usuario;
    }
}
