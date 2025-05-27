package com.example.virtualShop.controlador;

import com.example.virtualShop.dto.ItemCarritoDto;
import com.example.virtualShop.servicios.CarritoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/carrito")
@CrossOrigin(origins = "*")
public class CarritoControlador {

    @Autowired
    private CarritoServicio carritoServicio;

    @PostMapping("/agregar-producto/{productoId}")
    public ResponseEntity<String> agregarProductoAlCarrito(
            @RequestHeader("Authorization") String token,
            @PathVariable Long productoId
    ) {
        carritoServicio.agregarProducto(token, productoId);
        return ResponseEntity.ok("Producto agregado correctamente al carrito");
    }

    @GetMapping("/productos")
    public ResponseEntity<List<ItemCarritoDto>> obtenerProductos(@RequestHeader("Authorization") String token) {
        List<ItemCarritoDto> itemCarrito = carritoServicio.obtenerProductosDelCarrito(token);
        return ResponseEntity.ok(itemCarrito);
    }

    @DeleteMapping("/eliminar-producto/{productoId}")
    public ResponseEntity<String> eliminarProductoDelCarrito(
            @RequestHeader("Authorization") String token,
            @PathVariable Long productoId
    ) {
        carritoServicio.eliminarProducto(token, productoId);
        return ResponseEntity.ok("Producto eliminado correctamente del carrito");
    }
}
