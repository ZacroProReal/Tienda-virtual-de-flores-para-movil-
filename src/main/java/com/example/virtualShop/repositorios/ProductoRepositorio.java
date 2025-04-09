package com.example.virtualShop.repositorios;

import com.example.virtualShop.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
    @Query(value = """
                SELECT e FROM Producto e
                WHERE UPPER(e.nombre) = UPPER(?1)""")
    Producto findByNombre (String nombre);
    Producto findByPrecio (BigDecimal precio);
    Producto findByCantidadDisponible(Integer cantidadDisponible);
    Producto findByColorFlores (String colorFlores);
    Producto findByDisponibilidad(boolean disponibilidad);


}
