package com.example.virtualShop.servicios;

import com.example.virtualShop.dto.ItemCarritoDto;
import com.example.virtualShop.dto.ProductoDto;
import com.example.virtualShop.entidades.*;
import com.example.virtualShop.repositorios.CarritoRepositorio;
import com.example.virtualShop.repositorios.ItemCarritoRepositorio;
import com.example.virtualShop.repositorios.ProductoRepositorio;
import com.example.virtualShop.repositorios.UsuarioRepositorio;
import com.example.virtualShop.servicios.AutenticacionServicio;
import com.example.virtualShop.seguridad.JwtUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarritoServicio {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private CarritoRepositorio carritoRepositorio;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Autowired
    private ItemCarritoRepositorio itemCarritoRepositorio;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public void agregarProducto(String token, Long productoId) {
        Usuario usuario = obtenerUsuarioDesdeToken(token);

        Producto producto = productoRepositorio.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Carrito carrito = usuario.getCarrito();
        if (carrito == null) {
            carrito = new Carrito();
            carrito.setUsuario(usuario);
            carrito.setFechaCreacion(LocalDateTime.now());
            carrito.setEstado(EstadoCarrito.ACTIVO);
            carrito.setCantidadGeneralProduc(0);
            carrito.setCostoGenearl(0);
            carrito = carritoRepositorio.save(carrito);
        }

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

        entityManager.refresh(carrito);

        int total = carrito.getItems().stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
        carrito.setCantidadGeneralProduc(total);

        int costoTotal = carrito.getItems().stream()
                .mapToInt(i -> i.getCantidad() * i.getProducto().getPrecio().intValue())
                .sum();
        carrito.setCostoGenearl(costoTotal);

        carritoRepositorio.save(carrito);
    }

    @Transactional
    public void eliminarProducto(String token, Long productoId) {
        Usuario usuario = obtenerUsuarioDesdeToken(token);

        Carrito carrito = usuario.getCarrito();
        if (carrito == null) {
            throw new RuntimeException("El carrito no existe");
        }

        ItemCarrito item = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(productoId))
                .findFirst()
                .orElse(null);

        if (item == null) {
            throw new RuntimeException("El producto no se encuentra en el carrito");
        }

        if (item.getCantidad() > 1) {
            item.setCantidad(item.getCantidad() - 1);
            itemCarritoRepositorio.save(item);
        } else {
            carrito.getItems().remove(item);
            itemCarritoRepositorio.delete(item);
        }

        int total = carrito.getItems().stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
        carrito.setCantidadGeneralProduc(total);

        int costoTotal = carrito.getItems().stream()
                .mapToInt(i -> i.getCantidad() * i.getProducto().getPrecio().intValue())
                .sum();
        carrito.setCostoGenearl(costoTotal);

        carritoRepositorio.save(carrito);
    }

    @Transactional
    public List<ItemCarritoDto> obtenerProductosDelCarrito(String token) {
        Usuario usuario = obtenerUsuarioDesdeToken(token);

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
    @Transactional
    public Usuario obtenerUsuarioDesdeToken(String token) {
        String tokenLimpio = token.replace("Bearer ", "").trim();
        String correo = jwtUtil.extraerCorreo(tokenLimpio);
        Usuario usuario = usuarioRepositorio.findByCorreo(correo);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado con el token");
        }
        return usuario;
    }
}
