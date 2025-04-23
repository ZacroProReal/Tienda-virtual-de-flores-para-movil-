package com.example.virtualShop.repositorios;

import com.example.virtualShop.entidades.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


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

}
