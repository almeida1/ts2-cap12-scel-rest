package com.fatec.scel.servico;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

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
	public Livro save(Livro livro) {
		logger.info(">>>>>> 2. servico save chamado");
		return repository.save(livro);

	}

	@Override
	public Optional<Livro> consultaPorId(Long id) {
		logger.info(">>>>>> 2. servico consulta por id chamado");
		return repository.findById(id);
	}

	@Override
	public Optional<Livro> consultaPorIsbn(String isbn) {
		logger.info(">>>>>> 2. servico consulta por isbn chamado");
		return Optional.ofNullable(repository.findByIsbn(isbn));

	}

	@Override
	public void delete(Long id) {
		logger.info(">>>>>> 2. servico delete por id chamado");
		repository.deleteById(id);
	}

}