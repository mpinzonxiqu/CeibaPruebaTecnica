package com.ceiba.biblioteca.repository;

import com.ceiba.biblioteca.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    Optional<Prestamo> findByIdentificacionUsuario(String identificacionUsuario);
}
