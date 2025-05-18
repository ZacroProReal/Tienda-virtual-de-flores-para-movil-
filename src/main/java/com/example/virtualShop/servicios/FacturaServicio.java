package com.example.virtualShop.servicios;

import com.example.virtualShop.entidades.*;
import com.example.virtualShop.repositorios.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FacturaServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private CarritoRepositorio carritoRepositorio;

    @Autowired
    private FacturaRepositorio facturaRepositorio;

    @Autowired
    private ItemFacturaRepositorio itemFacturaRepositorio;

    @Transactional
    public Factura generarFacturaDesdeCarrito(Long usuarioId, Long carritoId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carrito = carritoRepositorio.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío, no se puede generar la factura.");
        }

        // Crear la factura
        Factura factura = new Factura();
        factura.setEmisorNombre("Florezza \uD83D\uDC90 S.A.");
        factura.setEmisorNif("900123456-7");
        factura.setEmisorDireccion("Cra 12 # 45-67, Bogotá D.C.");

        factura.setReceptor(usuario);
        factura.setFechaEmision(LocalDateTime.now());
        factura.setNumeroFactura(UUID.randomUUID().toString());

        // Subtotal desde el carrito (asumiendo que es BigDecimal)
        BigDecimal subtotal = BigDecimal.valueOf(carrito.getCostoGenearl());
        factura.setSubtotal(subtotal);

        // Impuesto del 19%
        BigDecimal impuestos = subtotal.multiply(BigDecimal.valueOf(0.19));
        factura.setImpuestos(impuestos);

        // Total
        BigDecimal total = subtotal.add(impuestos);
        factura.setTotal(total);

        factura.setFormaDePago("Pago en línea");
        factura.setCondicionesVenta("Contado");

        // Crear items factura
        List<ItemFactura> items = carrito.getItems().stream()
                .map(itemCarrito -> {
                    Producto producto = itemCarrito.getProducto();
                    BigDecimal precioUnitario = producto.getPrecio();
                    int cantidad = itemCarrito.getCantidad();
                    BigDecimal totalItem = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

                    return ItemFactura.builder()
                            .factura(factura)
                            .producto(producto)
                            .descripcionProducto(producto.getNombre())
                            .cantidad(cantidad)
                            .precioUnitario(precioUnitario)
                            .total(totalItem)
                            .build();
                }).collect(Collectors.toList());

        factura.setItems(items);

        return facturaRepositorio.save(factura);
    }
}