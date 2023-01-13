package com.fatec.scel.mantemLivro.ports;
import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.fatec.scel.mantemLivro.model.Livro;
public interface LivroServico {
	
	List<Livro> consultaTodos();
	Optional<Livro> consultaPorIsbn(String isbn);
	Optional<Livro> consultaPorId(Long id);
	Livro save(Livro Livro);
	void delete (Long id);
	Optional<Livro> update (Livro livro);
	
}