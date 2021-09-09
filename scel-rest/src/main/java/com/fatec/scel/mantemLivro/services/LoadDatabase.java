package com.fatec.scel.mantemLivro.services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fatec.scel.mantemLivro.model.Livro;
import com.fatec.scel.mantemLivro.ports.LivroRepository;

@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(LivroRepository repository) {

    return args -> {
      log.info("Preloading " + repository.save(new Livro("1111", "Engenharia de Software","Pressman")));
      log.info("Preloading " + repository.save(new Livro("2222", "Introdução ao Teste de Software", "Delamaro")));
    };
  }
}