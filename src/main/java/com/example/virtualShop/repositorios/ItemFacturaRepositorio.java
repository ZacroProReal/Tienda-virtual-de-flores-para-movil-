package com.example.virtualShop.repositorios;

import com.example.virtualShop.entidades.ItemFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemFacturaRepositorio  extends JpaRepository<ItemFactura,Long> {
}
