package com.fatec.scel.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import com.fatec.scel.model.Livro;
import com.fatec.scel.model.LivroRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class REQ01CadastarLivroControllerTests {

	@Autowired
	TestRestTemplate testRestTemplate;
	@Autowired
	LivroRepository repository;

	@Test
	void ct01_quando_seleciona_cadastrar_livro_retorna_200() {
		// Dado - que o servico está disponivel e o livro nao esta cadastrado
		// Quando - o usuario faz uma requisicao POST para cadastrar livro
		Livro livro = new Livro("3333", "User Stories", "Cohn");
		HttpEntity<Livro> httpEntity = new HttpEntity<>(livro);
		ResponseEntity<String> resposta = testRestTemplate.exchange("/api/v1/livros", HttpMethod.POST, httpEntity, String.class);
		// Entao - retorna HTTP201
		assertEquals("200 OK", resposta.getStatusCode().toString());
	}

}
