package com.example.virtualShop.controlador;

import com.example.virtualShop.dto.ProductoDto;
import com.example.virtualShop.entidades.Producto;
import com.example.virtualShop.servicios.ProductoServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*")

public class ProductoControlador {
    ProductoServicio productoServicio;

    @PostMapping("/")
    public ProductoDto crear(@RequestBody ProductoDto producto) throws IOException {
        return productoServicio.crear(producto);
    }
    @GetMapping("/")
    public List<Producto> obtenerTodos(){
        return productoServicio.obtenerTodos();
    }

    @PutMapping ("/modificar/{id}")
    public ResponseEntity<Producto> modificarProducto(
            @PathVariable Long id,
            @RequestBody Producto productoActualizado
    ) {
        Producto productoModificado = productoServicio.modificarProducto(id, productoActualizado);
        return ResponseEntity.ok(productoModificado);
    }
    @GetMapping("/buscar/{nombre}")
    public Producto buscarNombre(@PathVariable String nombre){
        return productoServicio.buscarNombre(nombre);
    }

}



