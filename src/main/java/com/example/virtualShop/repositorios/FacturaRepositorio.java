package com.example.virtualShop.repositorios;

import com.example.virtualShop.entidades.Factura;
import com.example.virtualShop.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaRepositorio extends JpaRepository<Factura, Long> {
    Optional<Factura> findTopByReceptorOrderByFechaEmisionDesc(Usuario receptor);
}
