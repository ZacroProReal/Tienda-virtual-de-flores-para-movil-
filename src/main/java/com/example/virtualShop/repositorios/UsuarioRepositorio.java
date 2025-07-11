package com.example.virtualShop.repositorios;


import com.example.virtualShop.entidades.EstadoUsuario;
import com.example.virtualShop.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    @Query(value = """
                SELECT e FROM Usuario e
                WHERE UPPER(e.nombre) = UPPER(?1)""")
    Usuario findByNombre (String nombre);
    Usuario findByApellido (String apellido);
    Usuario findByTelefono(String telefono);
    Usuario findByDireccion(String direccion);
    Usuario findByContrasena(String contrasena);
    Usuario findByFechaNacimiento(LocalDate fechaNacimiento);
    Usuario findByRol(Integer rol);

    List<Usuario> findByEstadoIn(List<EstadoUsuario> estados);

    Optional<Usuario> findByCorreoAndEstadoIn(String correo, List<EstadoUsuario> estados);
    Usuario findByCorreo(String correo);
    boolean existsByCorreo(String correo);

    Usuario findByNombreAndEstadoIn(String nombre, List<EstadoUsuario> estados);
}
