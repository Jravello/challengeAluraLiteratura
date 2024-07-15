package com.alura.challengealuraliteratura.repository;

import com.alura.challengealuraliteratura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByNombre(String nombre);
    List<Autor> findAllByFechaNacimiento(int fechaNacimiento);
}
