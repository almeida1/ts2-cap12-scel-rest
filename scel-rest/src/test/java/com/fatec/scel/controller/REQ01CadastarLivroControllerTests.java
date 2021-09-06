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
import org.springframework.web.client.RestClientException;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class REQ01CadastarLivroControllerTests {
	String urlBase = "/api/v1/livros";
	// String urlBase = "https://ts-scel-rest-livro.herokuapp.com/api/v1/livros";
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
		ResponseEntity<String> resposta = testRestTemplate.exchange(urlBase, HttpMethod.POST, httpEntity, String.class);
		// Entao - retorna HTTP200
		assertEquals("200 OK", resposta.getStatusCode().toString());
		String bodyEsperado = "{\"id\":3,\"isbn\":\"3333\",\"titulo\":\"User Stories\",\"autor\":\"Cohn\"}";
	// {"id" : 3,"isbn": "3333", "titulo" : "User Stories","autor" : "Cohn" }
		assertEquals(bodyEsperado, resposta.getBody());

	}
	
	@Test
	void ct02_quando_endpoint_invalido_retorna_404() {
		// Dado - que o servico está disponivel e o livro não esta cadastrado
		// Quando - o usuario faz uma requisicao POST para cadastrar livro
		Livro livro = new Livro("4444", "Codigo Limpo", "Martin");
		HttpEntity<Livro> httpEntity = new HttpEntity<>(livro);
		ResponseEntity<String> resposta = testRestTemplate.exchange("/urlInvalida", HttpMethod.POST, httpEntity, String.class);
		// Entao - retorna HTTP200
		assertEquals("404 NOT_FOUND", resposta.getStatusCode().toString());

	}
	@Test
	void ct03_quando_endpoint_valido_e_metodo_http_invalido_retorna_405() {
		// Dado - que o servico está disponivel e o livro não esta cadastrado
		// Quando - o usuario faz uma requisicao POST para cadastrar livro
		Livro livro = new Livro("4444", "Código Limpo", "Martin");
		HttpEntity<Livro> httpEntity = new HttpEntity<>(livro);
		ResponseEntity<String> resposta = testRestTemplate.exchange(urlBase, HttpMethod.PUT, httpEntity, String.class);
		// Entao - retorna HTTP200
		assertEquals("405 METHOD_NOT_ALLOWED", resposta.getStatusCode().toString());

	}

	@Test
	void ct04_quando_livro_ja_cadastrado_retorna_400() {
		// Dado - que o servico está disponivel e o livro ja esta cadastrado
		// Quando - o usuario faz uma requisicao POST para cadastrar livro
		Livro livro = new Livro("3333", "User Stories", "Cohn");
		HttpEntity<Livro> httpEntity = new HttpEntity<>(livro);
		ResponseEntity<String> resposta = testRestTemplate.exchange(urlBase, HttpMethod.POST, httpEntity, String.class);
		// Entao - retorna HTTP200
		assertEquals("400 BAD_REQUEST", resposta.getStatusCode().toString());

	}
	@Test
	void ct05_quando_dados_invalidos_retorna_400() {
		// Dado - que o servico está disponivel e o isbn eh maior do que quatro caracteres
		// Quando - o usuario faz uma requisicao POST para cadastrar livro
		Livro livro = new Livro("44445", "Código Limpo", "Martin");
		HttpEntity<Livro> httpEntity = new HttpEntity<>(livro);
		ResponseEntity<String> resposta = testRestTemplate.exchange(urlBase, HttpMethod.POST, httpEntity, String.class);
		// Entao - retorna HTTP200
		assertEquals("400 BAD_REQUEST", resposta.getStatusCode().toString());

	}
	@Test
	void ct06_quando_dados_invalidos_retorna_400() {
		// Dado - que o servico está disponivel e o titulo está em branco
		// Quando - o usuario faz uma requisicao POST para cadastrar livro
		Livro livro = new Livro("4444", "", "Martin");
		HttpEntity<Livro> httpEntity = new HttpEntity<>(livro);
		ResponseEntity<String> resposta = testRestTemplate.exchange(urlBase, HttpMethod.POST, httpEntity, String.class);
		// Entao - retorna HTTP200
		assertEquals("400 BAD_REQUEST", resposta.getStatusCode().toString());

	}
	@Test
	void ct07_quando_dados_invalidos_retorna_400() {
		// Dado - que o servico está disponivel e o autor está em branco
		// Quando - o usuario faz uma requisicao POST para cadastrar livro
		Livro livro = new Livro("44445", "Código Limpo", "");
		HttpEntity<Livro> httpEntity = new HttpEntity<>(livro);
		ResponseEntity<String> resposta = testRestTemplate.exchange(urlBase, HttpMethod.POST, httpEntity, String.class);
		// Entao - retorna HTTP200
		assertEquals("400 BAD_REQUEST", resposta.getStatusCode().toString());

	}
	/*
	 * retorno esperado objeto Java
	 */
	@Test
	void ct08_quando_seleciona_cadastrar_livro_retorna_200() {
		// Dado - que o servico está disponivel e o livro nao esta cadastrado
		// Quando - o usuario faz uma requisicao POST para cadastrar livro
		Livro livro = new Livro("5555", "User Stories", "Cohn");
		HttpEntity<Livro> httpEntity = new HttpEntity<>(livro);
		try {
			ResponseEntity<Livro> resposta = testRestTemplate.exchange(urlBase, HttpMethod.POST, httpEntity,Livro.class);
			// Entao - retorna livro com id
			Livro umLivro = resposta.getBody();
			livro.setId(umLivro.getId());
			assertTrue(umLivro.equals(livro));
		} catch (RestClientException e) {
			fail(">>>>>> erro =>" + e.getMessage());
		}

	}

}
