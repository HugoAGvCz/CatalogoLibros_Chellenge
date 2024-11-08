package org.hagc.catalogolibrosapi.repository;

import org.hagc.catalogolibrosapi.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    @Query(value = "SELECT l FROM Libro l WHERE l.idioma = :idioma")
    List<Libro> encuentraLibrosPorIdioma(String idioma);
}
