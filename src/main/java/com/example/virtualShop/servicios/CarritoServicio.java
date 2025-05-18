package com.example.virtualShop.servicios;

import com.example.virtualShop.dto.ItemCarritoDto;
import com.example.virtualShop.dto.ProductoDto;
import com.example.virtualShop.entidades.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @Transactional
    public void agregarProducto(Long usuarioId, Long productoId) {
        System.out.println("Iniciando agregarProducto: usuarioId=" + usuarioId + ", productoId=" + productoId);

        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        System.out.println("Usuario encontrado: " + usuario.getNombre());

        Producto producto = productoRepositorio.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        System.out.println("Producto encontrado: " + producto.getNombre());

        Carrito carrito = usuario.getCarrito();
        if (carrito == null) {
            carrito = new Carrito();
            carrito.setUsuario(usuario);
            carrito.setFechaCreacion(LocalDateTime.now());
            carrito.setEstado(EstadoCarrito.ACTIVO);
            carrito.setCantidadGeneralProduc(0);
            carrito.setCostoGenearl(0);
            carrito = carritoRepositorio.save(carrito);
            System.out.println("Carrito creado para el usuario.");
        } else {
            System.out.println("Carrito existente encontrado: id=" + carrito.getId());
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

// Forzar la recarga del carrito y sus items
        entityManager.refresh(carrito);

// Ahora calcula los totales correctamente
        int total = carrito.getItems().stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
        carrito.setCantidadGeneralProduc(total);

        int costoTotal = carrito.getItems().stream()
                .mapToInt(i -> i.getCantidad() * i.getProducto().getPrecio().intValue())
                .sum();
        carrito.setCostoGenearl(costoTotal);

        carritoRepositorio.save(carrito);

        System.out.println("Totales actualizados: cantidadGeneralProduc=" + total + ", costoGenearl=" + costoTotal);

        System.out.println("Totales actualizados: cantidadGeneralProduc=" + total + ", costoGenearl=" + costoTotal);
    }

    @Transactional
    public void eliminarProducto(Long usuarioId, Long productoId) {
        System.out.println("Iniciando eliminarProducto: usuarioId=" + usuarioId + ", productoId=" + productoId);

        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Carrito carrito = usuario.getCarrito();
        if (carrito == null) {
            System.out.println("El carrito no existe.");
            throw new RuntimeException("El carrito no existe");
        }

        ItemCarrito item = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(productoId))
                .findFirst()
                .orElse(null);

        if (item == null) {
            System.out.println("El producto no se encuentra en el carrito.");
            throw new RuntimeException("El producto no se encuentra en el carrito");
        }

        if (item.getCantidad() > 1) {
            item.setCantidad(item.getCantidad() - 1);
            itemCarritoRepositorio.save(item);
            System.out.println("Cantidad del producto decrementada. Nueva cantidad: " + item.getCantidad());
        } else {
            carrito.getItems().remove(item); // <-- ¡Elimina la referencia!
            itemCarritoRepositorio.delete(item);
            System.out.println("Item eliminado del carrito porque era el último.");
        }

        // Ahora calcula los totales con la lista ya actualizada
        int total = carrito.getItems().stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
        carrito.setCantidadGeneralProduc(total);

        int costoTotal = carrito.getItems().stream()
                .mapToInt(i -> i.getCantidad() * i.getProducto().getPrecio().intValue())
                .sum();
        carrito.setCostoGenearl(costoTotal);

        carritoRepositorio.save(carrito);

        System.out.println("Totales actualizados tras eliminar: cantidadGeneralProduc=" + total + ", costoGenearl=" + costoTotal);
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
