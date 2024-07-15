package com.alura.challengealuraliteratura.repository;

import com.alura.challengealuraliteratura.model.Idioma;
import com.alura.challengealuraliteratura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IdiomaRepository extends JpaRepository<Idioma, Long> {

    Idioma findByIdioma(String idioma);
}
