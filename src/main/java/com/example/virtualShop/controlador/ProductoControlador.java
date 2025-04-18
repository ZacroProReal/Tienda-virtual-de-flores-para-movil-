package com.example.virtualShop.controlador;

import com.example.virtualShop.dto.ProductoDto;
import com.example.virtualShop.entidades.Producto;
import com.example.virtualShop.servicios.ProductoServicio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*")

@Controller
public class ProductoControlador {
    ProductoServicio productoServicio;

    @PostMapping("/")
    public ProductoDto crear(@RequestBody ProductoDto docente){
        return productoServicio.crear(docente);
    }
    @GetMapping("/")
    public List<Producto> obtenerTodos(){
        return productoServicio.obtenerTodos();
    }

}


