package com.example.virtualShop.controlador;

import com.example.virtualShop.dto.LoginDto;
import com.example.virtualShop.servicios.AutenticacionServicio;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/autenticacion")
@CrossOrigin(origins = "*")
@Validated
public class AutenticacionControlador {

    private final AutenticacionServicio autenticacionServicio;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto) {
        boolean autenticado = autenticacionServicio.autenticar(loginDto.correo(), loginDto.contrasena());
        if (autenticado) {
            return ResponseEntity.ok("Login exitoso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
}
