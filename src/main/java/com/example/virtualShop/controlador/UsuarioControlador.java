package com.example.virtualShop.controlador;

import com.example.virtualShop.dto.UsuarioDto;
import com.example.virtualShop.dto.UsuarioPerfilDto;
import com.example.virtualShop.entidades.Usuario;
import com.example.virtualShop.servicios.UsuarioServicio;
import com.example.virtualShop.seguridad.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/registrar")
    public UsuarioDto crear(@RequestBody UsuarioDto usuario) throws IOException {
        return usuarioServicio.registrarUsuario(usuario);
    }

    @GetMapping("/listar")
    public List<Usuario> obtenerTodos() {
        return usuarioServicio.listarUsuarios();
    }

    // Modificar usuario - sin id, se extrae correo del token JWT
    @PutMapping("/modificar")
    public ResponseEntity<Usuario> modificarUsuario(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody Usuario usuarioActualizado
    ) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validarToken(token)) {
                return ResponseEntity.status(401).build();
            }
            String correo = jwtUtil.extraerCorreo(token);
            Usuario usuarioModificado = usuarioServicio.modificarUsuarioPorCorreo(correo, usuarioActualizado);
            return ResponseEntity.ok(usuarioModificado);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    // Eliminar usuario - sin id, se extrae correo del token JWT
    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarUsuario(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validarToken(token)) {
                return ResponseEntity.status(401).build();
            }
            String correo = jwtUtil.extraerCorreo(token);
            usuarioServicio.eliminarUsuarioPorCorreo(correo);
            return ResponseEntity.ok("Usuario eliminado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Error al eliminar usuario");
        }
    }

    // Buscar usuario por nombre (puedes mantener o eliminar este endpoint)
    @GetMapping("/buscar/{nombre}")
    public Usuario buscarNombre(@PathVariable String nombre) {
        return usuarioServicio.buscarUsuario(nombre);
    }
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioPerfilDto> obtenerPerfil(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "").trim();

        UsuarioPerfilDto perfil = usuarioServicio.obtenerPerfilDesdeToken(token);
        return ResponseEntity.ok(perfil);
    }
}
