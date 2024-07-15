package com.alura.challengealuraliteratura.repository;

import com.alura.challengealuraliteratura.model.Libro;
import com.alura.challengealuraliteratura.model.LibroComp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibrosRepository extends JpaRepository<Libro, Long> {
    Libro findByTitulo(String titulo);


}
