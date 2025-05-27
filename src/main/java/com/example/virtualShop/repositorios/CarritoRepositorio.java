package com.example.virtualShop.repositorios;

import com.example.virtualShop.entidades.Carrito;
import com.example.virtualShop.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CarritoRepositorio extends JpaRepository<Carrito, Long> {

    @Query("""
        SELECT c FROM Carrito c
        WHERE c.estado = ?1
    """)
    Carrito findByEstado(String estado);

    @Query("""
        SELECT c FROM Carrito c
        WHERE c.fechaCreacion = ?1
    """)
    Carrito findByFechaCreacion(java.time.LocalDateTime fechaCreacion);
    Optional<Carrito> findByUsuario(Usuario usuario);

}
