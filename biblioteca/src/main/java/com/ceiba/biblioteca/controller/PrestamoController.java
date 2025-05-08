package com.ceiba.biblioteca.controller;

import com.ceiba.biblioteca.model.Prestamo;
import com.ceiba.biblioteca.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/prestamo")
public class PrestamoController {
    // Moises Pinzon
    @Autowired
    private PrestamoService prestamoService;


    @PostMapping
    public ResponseEntity<?> crearPrestamo(@RequestBody Prestamo prestamo) {
        try {

            Prestamo creado = prestamoService.crearPrestamo(prestamo);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("id", creado.getId());
            respuesta.put("fechaMaximaDevolucion", creado.getFechaMaximaDevolucion()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException e) {

            Map<String, String> error = new HashMap<>();
            error.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPrestamoPorId(@PathVariable Long id) {
        try {

            Prestamo prestamo = prestamoService.obtenerPrestamoPorId(id);
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("id", prestamo.getId());
            respuesta.put("isbn", prestamo.getIsbn());
            respuesta.put("identificacionUsuario", prestamo.getIdentificacionUsuario());
            respuesta.put("tipoUsuario", prestamo.getTipoUsuario());
            respuesta.put("fechaMaximaDevolucion", prestamo.getFechaMaximaDevolucion()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException e) {

            Map<String, String> error = new HashMap<>();
            error.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
