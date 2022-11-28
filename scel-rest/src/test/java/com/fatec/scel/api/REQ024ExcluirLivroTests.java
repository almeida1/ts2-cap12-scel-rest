package com.fatec.scel.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class REQ024ExcluirLivroTests {

	String dom = "https://ts-scel-rest-livro.herokuapp.com";
	String urlBase = "/api/v1/livros/";
	@Autowired
	TestRestTemplate testRestTemplate;

	@Test
	void ct01_quando_exclui_pelo_id_consulta_por_isbn_deve_retorna_null() {
		// Dado – que o id está cadastrado
		Long id = 1L;

		// Quando – o usuário solicita exclusao
		ResponseEntity<String> resposta = testRestTemplate.exchange(urlBase + id, HttpMethod.DELETE, null,
				String.class);
		// Então – o resultado obtido da consulta deve ser null
		assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());

	}
	@Test
	void ct02_quando_exclui_por_id_invalido_retorna_bad_request() {
		// Dado – que o id não está cadastrado repository.deleteAll();
		// Quando – o usuário solicita exclusao
		ResponseEntity<String> resposta = testRestTemplate.exchange(urlBase + "99", HttpMethod.DELETE, null,
				String.class);
		// Então – o resultado obtido da consulta deve ser null
		assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());

	}
}
