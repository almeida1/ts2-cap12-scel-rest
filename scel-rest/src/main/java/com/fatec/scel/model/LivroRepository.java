package com.fatec.scel.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends JpaRepository <Livro, Long>{
	
	public Livro findByIsbn(@Param("isbn") String isbn);
	//public void deleteByIsbn (@Param("isbn") String isbn);
	List<Livro> findAllByTituloIgnoreCaseContaining(String titulo);
	
}