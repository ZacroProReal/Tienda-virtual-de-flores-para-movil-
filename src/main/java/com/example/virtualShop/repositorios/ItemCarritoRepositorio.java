package com.example.virtualShop.repositorios;

import com.example.virtualShop.entidades.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemCarritoRepositorio extends JpaRepository<ItemCarrito, Long> {
}
