package com.example.virtualShop.controlador;


import com.example.virtualShop.dto.ItemCarritoDto;
import com.example.virtualShop.servicios.CarritoServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("/carrito")
@CrossOrigin(origins = "*")

public class CarritoControlador {

    @Autowired
    private CarritoServicio carritoServicio;

    @PostMapping("/agregar-producto/{usuarioId}/{productoId}")
    public ResponseEntity<String> agregarProductoAlCarrito(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId
    ) {
        carritoServicio.agregarProducto(usuarioId, productoId);
        return ResponseEntity.ok("Producto agregado correctamente al carrito");
    }

    @GetMapping("/productos/{usuarioId}")
    public ResponseEntity<List<ItemCarritoDto>> obtenerProductos(@PathVariable Long usuarioId) {
        List<ItemCarritoDto> itemCarrito = carritoServicio.obtenerProductosDelCarrito(usuarioId);
        return ResponseEntity.ok(itemCarrito);
    }

}


