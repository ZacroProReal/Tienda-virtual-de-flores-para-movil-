package com.example.virtualShop.controlador;

import com.example.virtualShop.dto.LoginDto;
import com.example.virtualShop.servicios.AutenticacionServicio;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
@RestController
@RequestMapping("/autenticacion")
@CrossOrigin(origins = "*")
@Validated
public class AutenticacionControlador {

    private final AutenticacionServicio autenticacionServicio;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginDto loginDto) {
        String token = autenticacionServicio.autenticarYGenerarToken(loginDto.correo(), loginDto.contrasena());

        Map<String, String> respuesta = new HashMap<>();
        if (token != null) {
            respuesta.put("mensaje", "Inicio de sesión exitoso");
            respuesta.put("token", token);
            return ResponseEntity.ok(respuesta);
        } else {
            respuesta.put("mensaje", "Credenciales inválidas o usuario no autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        }
    }
    @PostMapping("/solicitar-restauracion")
    public ResponseEntity<Map<String, String>> solicitarRestauracion(@RequestBody Map<String, String> body) {
        String correo = body.get("correo");
        String token = autenticacionServicio.solicitarTokenRestauracion(correo);

        // Enviar el link con token por email (aquí solo devuelvo el link para pruebas)
        String link = "http://127.0.0.1:8862/autenticacion/restaurar?token=" + token;

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Se ha enviado un correo para restaurar la cuenta");
        respuesta.put("link", link);  // En producción no envíes esto, solo el email

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/restaurar")
    public ResponseEntity<Map<String, String>> restaurarCuenta(@RequestParam String token) {
        autenticacionServicio.restaurarCuentaConToken(token);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Cuenta restaurada exitosamente");
        return ResponseEntity.ok(respuesta);
    }

}

