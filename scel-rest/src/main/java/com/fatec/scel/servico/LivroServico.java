package com.fatec.scel.servico;
import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.fatec.scel.model.Livro;
public interface LivroServico {
	
	List<Livro> consultaTodos();
	Livro consultaPorIsbn(String isbn);
	Optional<Livro> consultaPorId(Long id);
	Livro save(Livro Livro);
	void delete (Long id);
	ResponseEntity<?> update (Long id, Livro livro, BindingResult result);


}