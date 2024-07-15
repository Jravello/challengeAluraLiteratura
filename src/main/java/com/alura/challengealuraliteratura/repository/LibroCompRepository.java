package com.alura.challengealuraliteratura.repository;

import com.alura.challengealuraliteratura.model.LibroComp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface LibroCompRepository extends JpaRepository<LibroComp, Long> {
    @Query(value = "SELECT lib.id, lib.titulo, lib.numero_descargas, au.nombre, au.fecha_nacimiento, " +
            "idi.idioma FROM Libro lib left join Autor au on au.id = lib.id_autor " +
            "left join Idioma idi on idi.id = lib.id_idioma", nativeQuery = true)
    List<LibroComp> findAllWithAutorAndIdioma();

    @Query(value = "SELECT lib.id, lib.titulo, lib.numero_descargas, au.nombre, au.fecha_nacimiento, " +
                "idi.idioma FROM Libro lib left join Autor au on au.id = lib.id_autor " +
                "left join Idioma idi on idi.id = lib.id_idioma where idi.idioma = ?1", nativeQuery = true)
    List<LibroComp> findByIdWithAutorAndIdioma(String idioma);


}
