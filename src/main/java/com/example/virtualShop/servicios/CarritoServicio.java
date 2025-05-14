package com.example.virtualShop.servicios;

import com.example.virtualShop.dto.ItemCarritoDto;
import com.example.virtualShop.dto.ProductoDto;
import com.example.virtualShop.entidades.*;
//import com.example.virtualShop.entidades.EstadoCarrito;
import com.example.virtualShop.repositorios.CarritoRepositorio;
import com.example.virtualShop.repositorios.ItemCarritoRepositorio;
import com.example.virtualShop.repositorios.ProductoRepositorio;
import com.example.virtualShop.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarritoServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private CarritoRepositorio carritoRepositorio;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Autowired
    private ItemCarritoRepositorio itemCarritoRepositorio;

    @Transactional
    public void agregarProducto(Long usuarioId, Long productoId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Producto producto = productoRepositorio.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Carrito carrito = usuario.getCarrito();
        if (carrito == null) {
            carrito = new Carrito();
            carrito.setUsuario(usuario);
            carrito.setFechaCreacion(LocalDateTime.now());
            carrito.setEstado(EstadoCarrito.ACTIVO);
            carrito.setCantidadGeneralProduc(0);
            carrito = carritoRepositorio.save(carrito);
        }

        // Verifica si el producto ya existe en el carrito
        ItemCarrito itemExistente = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(productoId))
                .findFirst()
                .orElse(null);

        if (itemExistente != null) {
            itemExistente.setCantidad(itemExistente.getCantidad() + 1);
            itemCarritoRepositorio.save(itemExistente);
        } else {
            ItemCarrito item = new ItemCarrito();
            item.setCarrito(carrito);
            item.setProducto(producto);
            item.setCantidad(1);
            itemCarritoRepositorio.save(item);
        }

        // Actualizar la cantidad general de productos
        int total = carrito.getItems().stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();

        carrito.setCantidadGeneralProduc(total);
        carritoRepositorio.save(carrito);
    }

    @Transactional
    public List<ItemCarritoDto> obtenerProductosDelCarrito(Long usuarioId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carrito = usuario.getCarrito();
        if (carrito == null) return List.of();

        return carrito.getItems().stream()
                .map(item -> new ItemCarritoDto(
                        item.getId(),
                        item.getCantidad(),
                        new ProductoDto(
                                item.getProducto().getNombre(),
                                item.getProducto().getDescripcion(),
                                item.getProducto().getPrecio(),
                                item.getProducto().getCantidadDisponible(),
                                item.getProducto().getColorFlores(),
                                item.getProducto().isDisponibilidad(),
                                item.getProducto().getImagen()
                        )
                ))
                .collect(Collectors.toList());
    }
}
