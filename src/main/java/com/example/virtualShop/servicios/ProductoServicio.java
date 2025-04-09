package com.example.virtualShop.servicios;

import com.example.virtualShop.dto.ProductoDto;
import com.example.virtualShop.entidades.Producto;
import com.example.virtualShop.repositorios.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServicio {
    ProductoRepositorio productoRepositorio;

    @Autowired
    public ProductoServicio(ProductoRepositorio docenteRepositorio) {
        this.productoRepositorio = docenteRepositorio;
    }
    public ProductoDto crear(ProductoDto productoDto) {
        Producto producto = Producto.builder()
                .nombre(productoDto.nombre())
                .build();

        if (productoRepositorio.save(producto).getId() > 0)
            return productoDto;
        else return null;
    }
    public List<Producto> obtenerTodos()
    {
        return productoRepositorio.findAll();
    }
}

