package com.fatec.scel.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.scel.model.Livro;

import com.fatec.scel.servico.LivroServico;

@RestController
@RequestMapping("/api")
public class LivroController {
	@Autowired
	LivroServico servico;

	Logger logger = LogManager.getLogger(LivroController.class);

	@PostMapping("/v1/livros")
	public ResponseEntity<Object> create(@RequestBody @Valid Livro livro, BindingResult result) {
		ResponseEntity<Object> response = null;
		if (result.hasErrors()) {
			logger.info(">>>>>> 1. controller chamou servico save - erro detectado no bean");
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			logger.info(">>>>>> 1. controller chamou servico save sem erro no bean validation");
			Optional<Livro> umLivro = servico.consultaPorIsbn(livro.getIsbn());
			if (umLivro.isPresent()) {

				response = ResponseEntity.badRequest().body("ja cadastrado");
			} else {

				servico.save(livro);
				response = new ResponseEntity<>(HttpStatus.CREATED);
			}

		}

		return response;
	}

	@CrossOrigin // desabilita o cors do spring security
	@GetMapping("/v1/livros")
	public ResponseEntity<List<Livro>> consultaTodos() {
		return ResponseEntity.ok().body(servico.consultaTodos());
	}

	@GetMapping("v1/livros/{isbn}")
	public ResponseEntity<Livro> findByIsbn(@PathVariable String isbn) {
		logger.info(">>>>>> 1. controller chamou servico consulta por isbn => " + isbn);

		return servico.consultaPorIsbn(isbn).map(record -> ResponseEntity.ok().body(record))
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("v1/livros/{isbn}")
	public ResponseEntity<?> delete(@PathVariable String isbn) {
		Optional<Livro> umLivro = servico.consultaPorIsbn(isbn);
		if (umLivro.isPresent()) {
			logger.info(">>>>>> 1. controller chamou servico delete por isbn => " + isbn);
			servico.delete(umLivro.get().getId());
			return ResponseEntity.ok().build();
		} else {
			logger.info(">>>>>> 3. controller chamou servico delete isbn nao localizado => " + isbn);
			return ResponseEntity.notFound().build();
		}

	}

	@PutMapping("v1/livros")
	public ResponseEntity<Livro> replaceLivro(@RequestBody Livro livro) {
		Optional<Livro> umLivro = servico.consultaPorIsbn(livro.getIsbn());
		if (umLivro.isPresent()) {
			Livro l = umLivro.get();
			l.setAutor(livro.getAutor());
			l.setTitulo(livro.getTitulo());
			servico.save(l);
			return ResponseEntity.ok(l);
		} else {
			return ResponseEntity.notFound().build();
		}

	}
}