package com.alura.challengealuraliteratura.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class LibroComp {

    @Id
    private Long id;
    private String titulo;
    private int numeroDescargas;
    private String nombre;
    private int fechaNacimiento;
    private String idioma;


    public LibroComp(Long id,String titulo, int numeroDescargas, String nombre, int fechaNacimiento, String idioma) {
        this.id = id;
        this.titulo = titulo;
        this.numeroDescargas = numeroDescargas;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.idioma = idioma;
    }

    public LibroComp() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(int numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(int fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
}
