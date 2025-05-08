package com.ceiba.biblioteca.service;

import com.ceiba.biblioteca.model.Prestamo;
import com.ceiba.biblioteca.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;

    @Autowired
    public PrestamoService(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }


    public Prestamo crearPrestamo(Prestamo prestamo) {
        if (prestamo == null) {
            throw new IllegalArgumentException("El préstamo no puede ser nulo");
        }

        int tipoUsuario = prestamo.getTipoUsuario();
        if (tipoUsuario < 1 || tipoUsuario > 3) {
            throw new IllegalArgumentException("Tipo de usuario no permitido en la biblioteca");
        }

        if (tipoUsuario == 3) {
            Optional<Prestamo> existente = prestamoRepository.findByIdentificacionUsuario(prestamo.getIdentificacionUsuario());
            if (existente.isPresent()) {
                throw new IllegalArgumentException(
                        "El usuario con identificación " + prestamo.getIdentificacionUsuario() +
                                " ya tiene un libro prestado, por lo cual no se le puede realizar otro préstamo."
                );
            }
        }

        LocalDate fechaDevolucion = calcularFechaMaximaDevolucion(tipoUsuario);
        prestamo.setFechaMaximaDevolucion(fechaDevolucion);

        if (prestamo.getIsbn() == null || prestamo.getIdentificacionUsuario() == null) {
            throw new IllegalArgumentException("El ISBN y la identificación del usuario son obligatorios");
        }

        return prestamoRepository.save(prestamo);
    }


    public Prestamo obtenerPrestamoPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        Optional<Prestamo> prestamoOptional = prestamoRepository.findById(id);

        if (!prestamoOptional.isPresent()) {
            throw new IllegalArgumentException("Préstamo no encontrado con ID: " + id);
        }

        return prestamoOptional.get(); // Devolver el préstamo encontrado
    }

    private LocalDate calcularFechaMaximaDevolucion(int tipoUsuario) {
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaDevolucion = fechaActual;

        int diasHabiles;

        switch (tipoUsuario) {
            case 1:
                diasHabiles = 10; // Afiliado
                break;
            case 2:
                diasHabiles = 8;  // Empleado
                break;
            case 3:
                diasHabiles = 7;  // Invitado
                break;
            default:
                throw new IllegalArgumentException("Tipo de usuario no permitido en la biblioteca");
        }

        // Sumamos los días hábiles, ignorando los fines de semana (sábado y domingo)
        while (diasHabiles > 0) {
            fechaDevolucion = fechaDevolucion.plusDays(1);

            // Verificamos si el día es un fin de semana (sábado o domingo)
            if (fechaDevolucion.getDayOfWeek() != DayOfWeek.SATURDAY && fechaDevolucion.getDayOfWeek() != DayOfWeek.SUNDAY) {
                diasHabiles--; // Solo descontamos días hábiles
            }
        }

        return fechaDevolucion;
    }

}
