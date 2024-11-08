package org.hagc.catalogolibrosapi.main;

import org.hagc.catalogolibrosapi.model.Autor;
import org.hagc.catalogolibrosapi.model.Datos;
import org.hagc.catalogolibrosapi.model.DatosLibro;
import org.hagc.catalogolibrosapi.model.Libro;
import org.hagc.catalogolibrosapi.repository.AutorRepository;
import org.hagc.catalogolibrosapi.repository.LibroRepository;
import org.hagc.catalogolibrosapi.service.ConsumoAPI;
import org.hagc.catalogolibrosapi.service.ConvierteDatos;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos convertidor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/";
    private List<Libro> libros;
    private List<Autor> autores;
    private String titulo;

    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Main(LibroRepository repository, AutorRepository autorRepository) {
        this.libroRepository = repository;
        this.autorRepository = autorRepository;
    }

    public void menu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores regitrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    librosRegistrados();
                    break;
                case 3:
                    autoresRegistrados();
                    break;
                case 4:
                    autoresVivosEnAnio();
                    break;
                case 5:
                    librosPorIdioma();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no valida");
            }

        }
    }

    private Datos getDatos() {
        System.out.println("Ingrese el nombre del libro que deseas buscar: ");
        titulo = scanner.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "/?search=" + titulo.replace(" ", "+"));
        //System.out.println(json);
        Datos datosBusqueda = convertidor.obtenerDatos(json, Datos.class);
        return datosBusqueda;
    }

    private void buscarLibroPorTitulo() {
        Datos datosBusqueda = getDatos();
        Optional<DatosLibro> libroBuscado = datosBusqueda.librosList().stream()
                .filter(libro -> libro.titulo().toUpperCase().contains(titulo.toUpperCase()))
                .findFirst();
        if (libroBuscado.isPresent()) {
            Libro libro = new Libro(libroBuscado.get());
            Autor autor = new Autor(libroBuscado.get().autor().get(0));
            // Se establece la relación bidireccional
            libro.setAutorLibro(autor);

            autorRepository.save(autor);
            libroRepository.save(libro);

            System.out.println("Libro \"" + libroBuscado.get().titulo() + "\" encontrado");
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void librosRegistrados() {
        libros = libroRepository.findAll();
        listarLibros(libros);
    }

    private void autoresRegistrados() {
        autores = autorRepository.findAll();
        listarAutores(autores);
    }

    private void autoresVivosEnAnio() {
        System.out.println("Ingresa el año aproximado de nacimiento de autor(es) a buscar: ");
        var anio = scanner.nextLine();
        autores = autorRepository.encuentraAutoresPorAnioNacimiento(anio);
        listarAutores(autores);
    }

    private void librosPorIdioma() {
        String opcIdioma = """
                Escribe el idioma para buscar los libros:
                es - Español
                en - Inglés
                """;
        System.out.println(opcIdioma);
        var idioma = scanner.nextLine();

        libros = libroRepository.encuentraLibrosPorIdioma(idioma);
        listarLibros(libros);
    }

    private void listarLibros(List<Libro> libros) {
        libros.stream()
                .sorted(Comparator.comparing(Libro::getNumeroDescargas).reversed())
                .forEach(System.out::println);
    }

    public void listarAutores(List<Autor> autores) {
        autores.stream()
                .sorted(Comparator.comparing(Autor::getAnioNacimiento))
                .forEach(System.out::println);
    }
}
