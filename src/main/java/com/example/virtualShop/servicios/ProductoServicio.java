package com.example.virtualShop.servicios;

import com.example.virtualShop.dto.ProductoDto;
import com.example.virtualShop.entidades.Producto;
import com.example.virtualShop.repositorios.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ProductoServicio {
    ProductoRepositorio productoRepositorio;

    @Autowired
    public ProductoServicio(ProductoRepositorio docenteRepositorio) {
        this.productoRepositorio = docenteRepositorio;
    }
    public ProductoDto crear(ProductoDto productoDto) throws IOException {
        Producto producto = Producto.builder()
                .nombre(productoDto.nombre())
                .descripcion(productoDto.descripcion())
                .precio(productoDto.precio())
                .cantidadDisponible(productoDto.cantidadDisponible())
                .colorFlores(productoDto.colorFlores())
                .disponibilidad(productoDto.disponibilidad())
                .imagen(productoDto.imagen() != null ? productoDto.imagen().getBytes() : null)
                .build();

        if (productoRepositorio.save(producto).getId() > 0)
            return productoDto;
        else return null;
    }
    public List<Producto> obtenerTodos(){
        return productoRepositorio.findAll();
    }
    public Producto modificarProducto(Long id, Producto productoActualizado) {
        Producto productoExistente = productoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        productoExistente.setNombre(productoActualizado.getNombre());
        productoExistente.setDescripcion(productoActualizado.getDescripcion());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setDisponibilidad(productoActualizado.isDisponibilidad());

        return productoRepositorio.save(productoExistente);
    }
}

