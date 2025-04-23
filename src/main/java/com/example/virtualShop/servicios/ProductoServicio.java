package com.example.virtualShop.servicios;

import com.example.virtualShop.dto.ProductoDto;
import com.example.virtualShop.entidades.Producto;
import com.example.virtualShop.entidades.Usuario;
import com.example.virtualShop.repositorios.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class ProductoServicio {
    ProductoRepositorio productoRepositorio;
    Usuario usuario;
    @Autowired
    public ProductoServicio(ProductoRepositorio productoRepositorio) {

        this.productoRepositorio = productoRepositorio;
    }
    public ProductoDto crear(ProductoDto productoDto) throws IOException {
        //if (usuario.getRol() != 1) {throw new SecurityException("No tienes permiso para agregar productos.");}
        Producto producto = Producto.builder()
                .nombre(productoDto.nombre())
                .descripcion(productoDto.descripcion())
                .precio(productoDto.precio())
                .cantidadDisponible(productoDto.cantidadDisponible())
                .colorFlores(productoDto.colorFlores())
                .disponibilidad(productoDto.disponibilidad())
                .imagen(productoDto.imagen())
                .build();

        if (productoRepositorio.save(producto).getId() > 0)
            return productoDto;
        else return null;
    }
    public List<Producto> obtenerTodos(){
        return productoRepositorio.findAll();
    }
    public Producto modificarProducto(Long id, Producto productoActualizado) {
        //if (usuario.getRol() != 1) {throw new SecurityException("No tienes permiso para modificar productos.");}
        Producto productoExistente = productoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        productoExistente.setNombre(productoActualizado.getNombre());
        productoExistente.setDescripcion(productoActualizado.getDescripcion());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setDisponibilidad(productoActualizado.isDisponibilidad());
        productoExistente.setCantidadDisponible(productoActualizado.getCantidadDisponible());
        productoExistente.setColorFlores(productoActualizado.getColorFlores());
        productoExistente.setImagen(productoActualizado.getImagen());

        return productoRepositorio.save(productoExistente);
    }
    @Transactional
    public Producto buscarNombre(String nombre){
        return  productoRepositorio.findByNombre(nombre);
    }
}

