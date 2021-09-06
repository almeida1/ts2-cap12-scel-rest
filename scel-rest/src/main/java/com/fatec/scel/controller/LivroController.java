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
@RequestMapping("/api/v1/livros")
public class LivroController {
	@Autowired
	LivroServico servico;

	Logger logger = LogManager.getLogger(LivroController.class);

	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Valid Livro livro, BindingResult result) {
		return servico.save(livro, result);
	}

	@CrossOrigin // desabilita o cors do spring security
	@GetMapping
	public ResponseEntity<List<Livro>> consultaTodos() {
		logger.info(">>>>>> controller chamou servico consulta todos");
		return ResponseEntity.ok().body(servico.consultaTodos());
	}

	@GetMapping("/{isbn}")
	public ResponseEntity<?> findByIsbn(@PathVariable String isbn) {
		logger.info(">>>>>> controller chamou servico consulta por isbn => " + isbn);
//		return Optional.ofNullable(servico.consultaPorIsbn(isbn)).map(record -> ResponseEntity.ok().body(record) ).orElse(ResponseEntity.notFound().build());
		Optional<Livro> umLivro = Optional.ofNullable(servico.consultaPorIsbn(isbn));
		ResponseEntity<?> resposta = null;
		if (umLivro.isPresent())
			resposta = new ResponseEntity<Livro>(umLivro.get(), HttpStatus.OK);
		else
			resposta = new ResponseEntity<String>("ISBN não localizado",HttpStatus.BAD_REQUEST);
		return resposta;
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Optional<Livro> umLivro = servico.consultaPorId(id);
		if (umLivro.isPresent()) {
			logger.info(">>>>>> controller chamou servico delete por id => " + id);
			servico.delete(umLivro.get().getId());
			return ResponseEntity.noContent().build();
		} else {
			logger.info(">>>>>> controller chamou servico delete id nao localizado => " + id);
			return ResponseEntity.notFound().build();
		}

	}
	/**
	 * atualiza as informacoes do livro
	 * @param id
	 * @param livro
	 * @param result
	 * @return
	 */

	@PutMapping("/{id}")
	public ResponseEntity<?> replace(@PathVariable("id") long id, @RequestBody @Valid Livro livro, BindingResult result) {
		logger.info(">>>>>> controller chamou servico update por id ");
		return servico.update(id, livro, result);
	}
}