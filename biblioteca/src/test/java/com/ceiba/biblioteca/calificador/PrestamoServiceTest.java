package com.ceiba.biblioteca.calificador;

import com.ceiba.biblioteca.model.Prestamo;
import com.ceiba.biblioteca.service.PrestamoService;
import com.ceiba.biblioteca.repository.PrestamoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class PrestamoServiceTest {

    @Mock
    private PrestamoRepository prestamoRepository;

    @InjectMocks
    private PrestamoService prestamoService;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testCrearPrestamo() {

        Prestamo prestamo = new Prestamo();
        prestamo.setIsbn("ASDA7884");
        prestamo.setIdentificacionUsuario("974148");
        prestamo.setTipoUsuario(1); // Tipo de usuario 1


        when(prestamoRepository.save(any(Prestamo.class))).thenReturn(prestamo);


        Prestamo resultado = prestamoService.crearPrestamo(prestamo);

        assertNotNull(resultado);
        assertEquals("ASDA7884", resultado.getIsbn());
        assertEquals("974148", resultado.getIdentificacionUsuario());
        assertEquals(1, resultado.getTipoUsuario());

        LocalDate fechaEsperada = LocalDate.now().plusDays(10);  // Tipo de usuario 1 -> 10 días
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaEsperadaString = fechaEsperada.format(formatter);

        assertEquals(fechaEsperadaString, resultado.getFechaMaximaDevolucion().format(formatter));

        verify(prestamoRepository, times(1)).save(prestamo);
    }

    @Test
    public void testCrearPrestamoUsuarioConPrestamoExistente() {

        Prestamo prestamoExistente = new Prestamo();
        prestamoExistente.setIsbn("ASDA7884");
        prestamoExistente.setIdentificacionUsuario("974148");
        prestamoExistente.setTipoUsuario(3);

        when(prestamoRepository.findByIdentificacionUsuario("974148")).thenReturn(Optional.of(prestamoExistente));


        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setIsbn("ASDA7884");
        nuevoPrestamo.setIdentificacionUsuario("974148");
        nuevoPrestamo.setTipoUsuario(3); // Tipo de usuario 3


        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.crearPrestamo(nuevoPrestamo);
        });


        verify(prestamoRepository, times(0)).save(any(Prestamo.class));
    }
}
    