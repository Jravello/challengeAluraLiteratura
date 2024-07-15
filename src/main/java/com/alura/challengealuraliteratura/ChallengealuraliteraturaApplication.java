package com.alura.challengealuraliteratura;

import com.alura.challengealuraliteratura.main.Principal;
import com.alura.challengealuraliteratura.repository.AutorRepository;
import com.alura.challengealuraliteratura.repository.IdiomaRepository;
import com.alura.challengealuraliteratura.repository.LibroCompRepository;
import com.alura.challengealuraliteratura.repository.LibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class ChallengealuraliteraturaApplication implements CommandLineRunner {

    @Autowired
    LibrosRepository librosRepository;
    @Autowired
    AutorRepository autorRepository;
    @Autowired
    IdiomaRepository idiomaRepository;
    @Autowired
    LibroCompRepository libroCompRepository;


    public static void main(String[] args) {
        SpringApplication.run(ChallengealuraliteraturaApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(librosRepository, autorRepository, idiomaRepository,libroCompRepository);
        principal.start();
    }
}
