package com.fatec.scel.servico;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.fatec.scel.model.Livro;
import com.fatec.scel.model.LivroRepository;

@Service
public class LivroServicoI implements LivroServico {
	Logger logger = LogManager.getLogger(this.getClass());
	@Autowired
	LivroRepository repository;

	@Override
	public List<Livro> consultaTodos() {
		return repository.findAll();

	}

	@Override
	public ResponseEntity<?> update(Long id, @Valid Livro livro, BindingResult result) {
		logger.info(">>>>>> servico update chamado");

		if (result.hasErrors()) {
			logger.info(">>>>>> servico save - dados inválidos => " + livro.getIsbn());
			return ResponseEntity.badRequest().body("Dados inválidos.");
		} else {
			return repository.findById(id).map(record -> {
				record.setAutor(livro.getAutor());
				record.setTitulo(livro.getTitulo());
				Livro atualizado = repository.save(livro);
				return ResponseEntity.ok().body(atualizado);
			}).orElse(ResponseEntity.notFound().build());

		}

	}

	@Override
	public Livro save(Livro livro) {
		logger.info(">>>>>> servico save - cadastro de livro ");
		return repository.save(livro); // retorna o livro com id

	}

	
	@Override
	public Optional<Livro> consultaPorId(Long id) {
		logger.info(">>>>>> servico consulta por id chamado");
		return repository.findById(id);
	}

	@Override
	public Livro consultaPorIsbn(String isbn) {
		logger.info(">>>>>> servico consulta por isbn chamado");
		return repository.findByIsbn(isbn);

	}

	@Override
	public void delete(Long id) {
		logger.info(">>>>>> servico delete por id chamado");
		repository.deleteById(id);
	}

}