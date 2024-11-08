package org.hagc.catalogolibrosapi.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String anioNacimiento;
    private String anioMuerte;
    @OneToMany(mappedBy = "autorLibro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.anioNacimiento = String.valueOf(datosAutor.anioNacimiento());
        this.anioMuerte = String.valueOf(datosAutor.anioMuerte());
    }

    public Autor() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(String anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public String getAnioMuerte() {
        return anioMuerte;
    }

    public void setAnioMuerte(String anioMuerte) {
        this.anioMuerte = anioMuerte;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        libros.forEach(libro -> libro.setAutorLibro(this));
        this.libros = libros;
    }

    @Override
    public String toString() {
        List<String> librosAutor = libros.stream()
                .map(libros -> libros.getTitulo())
                .collect(Collectors.toList());

        return "Autor: " + nombre + '\n' +
                "Fecha de nacimiento: " + anioNacimiento + '\n' +
                "Fecha de fallecimiento: " + anioMuerte + '\n' +
                "Libros: " + librosAutor + '\n';
    }
}
