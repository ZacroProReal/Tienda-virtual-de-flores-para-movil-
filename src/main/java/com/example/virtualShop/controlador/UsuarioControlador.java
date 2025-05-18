package com.example.virtualShop.controlador;

import com.example.virtualShop.dto.UsuarioDto;
import com.example.virtualShop.entidades.Usuario;
import com.example.virtualShop.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/registrar")
    public UsuarioDto crear(@RequestBody UsuarioDto usuario) throws IOException {
        return usuarioServicio.registrarUsuario(usuario);
    }
    @GetMapping("/listar")
    public List<Usuario> obtenerTodos(){
        return usuarioServicio.listarUsuarios();
    }
    @PutMapping ("/modificar/{id}")
    public ResponseEntity<Usuario> modificarUsuario(
            @PathVariable Long id,
            @RequestBody Usuario usuarioActualizado
    ) {
        Usuario usuarioModificado = usuarioServicio.modificarUsuario(id, usuarioActualizado);
        return ResponseEntity.ok(usuarioModificado);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        usuarioServicio.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado con éxito");
    }

    @GetMapping("/buscar/{nombre}")
    public Usuario buscarNombre(@PathVariable String nombre){
        return usuarioServicio.buscarUsuario(nombre);
    }

}




