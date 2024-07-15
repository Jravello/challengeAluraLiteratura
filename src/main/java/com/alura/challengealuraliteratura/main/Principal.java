package com.alura.challengealuraliteratura.main;

import com.alura.challengealuraliteratura.model.*;
import com.alura.challengealuraliteratura.repository.AutorRepository;
import com.alura.challengealuraliteratura.repository.IdiomaRepository;
import com.alura.challengealuraliteratura.repository.LibroCompRepository;
import com.alura.challengealuraliteratura.repository.LibrosRepository;
import com.alura.challengealuraliteratura.service.ConsumoAPI;
import com.alura.challengealuraliteratura.service.ConvierteDatos;

import java.text.SimpleDateFormat;
import java.util.*;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private Scanner scanner = new Scanner(System.in);
    private LibrosRepository librosRepository;
    private AutorRepository autorRepository;
    private IdiomaRepository idiomaRepository;
    private LibroCompRepository libroCompRepository;

    public Principal(LibrosRepository librosRepository, AutorRepository autorRepository, IdiomaRepository idiomaRepository, LibroCompRepository libroCompRepository) {
        this.librosRepository = librosRepository;
        this.autorRepository = autorRepository;
        this.idiomaRepository = idiomaRepository;
        this.libroCompRepository = libroCompRepository;
    }

    public Datos searchData(String params) {
        String url = URL_BASE + params;
        var json = consumoAPI.obtenerDatos(url);
        return convierteDatos.obtenerDatos(json, Datos.class);
    }

    public Long validateAutores(DatosLibros search) {
        if (search.autor() == null || search.autor().isEmpty()) {
            throw new IllegalArgumentException("El libro no tiene autores");
        }
        Long idAutor = null;
        for (var autor : search.autor()) {
            Optional<Autor> autorOptional = Optional.ofNullable(autorRepository.findByNombre(autor.nombre()));
            if (autorOptional.isPresent()) {
                idAutor = autorOptional.get().getId();
            } else {
                Autor autorNuevo = new Autor(autor);
                autorRepository.save(autorNuevo);
                idAutor = autorNuevo.getId();
            }
        }
        return idAutor;
    }

    public Long validateIdiomas(DatosLibros search) {
        if (search.idiomas() == null || search.idiomas().isEmpty()) {
            throw new IllegalArgumentException("El libro no tiene idiomas");
        }
        Long idIdioma = null;
        for (var idioma : search.idiomas()) {
            Optional<Idioma> idiomaOptional = Optional.ofNullable(idiomaRepository.findByIdioma(idioma));
            if (idiomaOptional.isPresent()) {
                idIdioma = idiomaOptional.get().getId();
            } else {
                Idioma idiomaNuevo = new Idioma(idioma);
                idiomaRepository.save(idiomaNuevo);
                idIdioma = idiomaNuevo.getId();
            }
        }
        return idIdioma;
    }

    public DatosLibros searchDataByTitle(String title) {
        String params = "?search=" + title.replace(" ", "+");
        System.out.println("Buscando por titulo: " + title);
        var data = searchData(params);
        if (data == null) {
            System.out.println("============================================");
            System.out.println("No se encontraron resultados en la busqueda");
            System.out.println("============================================");
            return null;
        }
        Optional<DatosLibros> optionalSearch = data.resultados().stream().filter(d -> d.titulo().toUpperCase().contains(title.toUpperCase())).findFirst();

        var search = optionalSearch.orElse(null);
        if (search == null) {
            System.out.println("============================================");
            System.out.println("No se encontraron resultados al filtrar");
            System.out.println("============================================");
            return null;
        }
        Libro libro = new Libro(search);

        try {
            Optional<Libro> libroOptional = Optional.ofNullable(librosRepository.findByTitulo(search.titulo()));
            if (libroOptional.isPresent()) {
                System.out.println("============================================");
                System.out.println("El libro ya existe en la base de datos");
                System.out.println("============================================");
            } else {
                libro.setIdAutor(validateAutores(search));
                libro.setIdIdioma(validateIdiomas(search));
                librosRepository.save(libro);
                System.out.println("============================================");
                System.out.println("Libro guardado en la base de datos");
                System.out.println("============================================");
            }
        } catch (Exception e) {
            System.out.println("Error al guardar el libro en la base de datos");
            e.printStackTrace();
        }
        return search;
    }

    public void validators(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("El titulo no puede ser nulo o vacio");
        }
    }

    public void validatorsYear(int year) {
        if (year < 0) {
            throw new IllegalArgumentException("El año no puede ser negativo");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        int yearNow = Integer.parseInt(df.format(new Date()));

        if (year > yearNow) {
            throw new IllegalArgumentException("El año no puede ser mayor al año actual");
        }
    }

    public void start() {
        var valid = -1;
        while (valid != 0) {
            System.out.println("============================================");
            System.out.println("Seleccione una opcion");
            System.out.println("1. Buscar libro por titulo");
            System.out.println("2. Buscar libros registrados");
            System.out.println("3. Buscar autores registrados");
            System.out.println("4. Buscar autores registrados por año");
            System.out.println("5. Buscar libros por idioma");
            System.out.println("0. Salir");
            System.out.println("============================================");
            valid = scanner.nextInt();
            switch (valid) {
                case 1 -> {
                    System.out.println("Ingrese el titulo del libro");
                    scanner.nextLine();
                    var title = scanner.nextLine();
                    validators(title);
                    var libro = searchDataByTitle(title);
                    if (libro != null) {
                        System.out.println("=================================");
                        System.out.println("Titulo: " + libro.titulo());
                        System.out.println("Autores: " + libro.autor());
                        System.out.println("Idiomas: " + libro.idiomas());
                        System.out.println("Numero de Descargas: " + libro.numeroDeDescargas());
                        System.out.println("=================================");
                    }
                }
                case 2 -> {
                    System.out.println("Buscando libros registrados");
                    var libros = libroCompRepository.findAllWithAutorAndIdioma();
                    libros.forEach(libro -> {
                        System.out.println("=================================");
                        System.out.println("Titulo: " + libro.getTitulo());
                        System.out.println("Autores: " + libro.getNombre());
                        System.out.println("Idiomas: " + libro.getIdioma());
                        System.out.println("Numero de Descargas: " + libro.getNumeroDescargas());
                        System.out.println("=================================");
                    });
                    if (libros.isEmpty()) {
                        System.out.println("No se encontraron libros registrados");
                    } else {
                        System.out.println("Total de libros registrados: " + libros.size());
                    }
                }
                case 3 -> {
                    System.out.println("Buscando autores registrados");
                    var autores = autorRepository.findAll();
                    autores.forEach(autor -> {
                        System.out.println("=================================");
                        System.out.println("Nombre: " + autor.getNombre());
                        System.out.println("Fecha de Nacimiento: " + autor.getFechaNacimiento());
                        System.out.println("=================================");
                    });
                    if (autores.isEmpty()) {
                        System.out.println("No se encontraron autores registrados");
                    } else {
                        System.out.println("Total de autores registrados: " + autores.size());
                    }
                }
                case 4 -> {
                    System.out.println("Ingrese el año del autor");
                    var year = scanner.nextInt();
                    validatorsYear(year);
                    System.out.println("Buscando autores registrados por año: " + year);
                    var autores = autorRepository.findAllByFechaNacimiento(year);
                    autores.forEach(autor -> {
                        System.out.println("=================================");
                        System.out.println("Nombre: " + autor.getNombre());
                        System.out.println("Fecha de Nacimiento: " + autor.getFechaNacimiento());
                        System.out.println("=================================");
                    });
                    if (autores.isEmpty()) {
                        System.out.println("No se encontraron autores registrados en el año: " + year);
                    } else {
                        System.out.println("Total de autores registrados en el año: " + autores.size());
                    }
                }
                //autorRepository.findByAnioPublicacion(year).forEach(System.out::println);
                case 5 -> {
                    System.out.println("Ingrese el idioma");
                    scanner.nextLine();
                    var idioma = scanner.nextLine();
                    System.out.println("Buscando libros por idioma: " + idioma);
                    var libros = libroCompRepository.findByIdWithAutorAndIdioma(idioma);
                    libros.forEach(libro -> {
                        System.out.println("=================================");
                        System.out.println("Titulo: " + libro.getTitulo());
                        System.out.println("Autores: " + libro.getNombre());
                        System.out.println("Idiomas: " + libro.getIdioma());
                        System.out.println("Numero de Descargas: " + libro.getNumeroDescargas());
                        System.out.println("=================================");
                    });
                    if (libros.isEmpty()) {
                        System.out.println("No se encontraron libros con el idioma: " + idioma);
                    } else {
                        System.out.println("Total de libros registrados por idioma: " + libros.size());
                    }
                }
                //idiomaRepository.findByLibrosIdioma(idioma).forEach(System.out::println);
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opcion no valida, intente de nuevo");
            }

        }

    }

}
