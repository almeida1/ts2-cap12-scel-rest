package com.fatec.scel.servico;
import java.util.*;



import com.fatec.scel.model.Livro;
public interface LivroServico {
	
	List<Livro> consultaTodos();
	Optional<Livro> consultaPorIsbn(String isbn);
	Optional<Livro> consultaPorId(Long id);
	Livro save(Livro Livro);
	void delete (Long id);


}