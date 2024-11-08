package org.hagc.catalogolibrosapi.repository;

import org.hagc.catalogolibrosapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    @Query(value = "SELECT a FROM Autor a WHERE a.anioNacimiento <= :anio and a.anioMuerte >= :anio")
    List<Autor> encuentraAutoresPorAnioNacimiento(String anio);
}
