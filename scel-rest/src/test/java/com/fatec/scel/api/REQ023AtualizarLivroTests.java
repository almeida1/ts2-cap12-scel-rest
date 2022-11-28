package com.fatec.scel.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.fatec.scel.mantemLivro.model.Livro;
import com.google.gson.Gson;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class REQ023AtualizarLivroTests {

	String dom = "https://ts-scel-rest-livro.herokuapp.com";
	String urlBase = "/api/v1/livros/";
	@Autowired
	TestRestTemplate testRestTemplate;
	@Test
	void ct01_quando_atualiza_com_dados_validos_retorna_atualizacao_ok() {
		// Dado – que o id e o isbn está cadastrado
		Livro livro = new Livro("2222", "User Stories", "Cohn");
		livro.setId(2L);
		Long id = 2L;
		HttpEntity<Livro> httpEntity = new HttpEntity<>(livro);
		ResponseEntity<String> resposta = testRestTemplate.exchange(urlBase + id , HttpMethod.PUT, httpEntity, String.class);
		assertEquals("200 OK", resposta.getStatusCode().toString());
		assertTrue(resposta.hasBody());
		Gson respostaDaMensagem = new Gson();
		Livro ro= respostaDaMensagem.fromJson(resposta.getBody(), Livro.class);
		assertNotNull(ro);
	}
	@Test
	void ct02_quando_atualiza_com_dados_invalidos_retorna_dados_invalidos() {
		// Dado – que o id e o isbn está cadastrado
		Livro livro = new Livro("2222", "User Stories", "");
		livro.setId(2L);
		Long id = 2L;
		HttpEntity<Livro> httpEntity = new HttpEntity<>(livro);
		//Quando atualiza com autor invalido
		ResponseEntity<String> resposta = testRestTemplate.exchange(urlBase + id , HttpMethod.PUT, httpEntity, String.class);
		//Entao retorna dados invalidos
		assertEquals("400 BAD_REQUEST", resposta.getStatusCode().toString());
		assertTrue(resposta.hasBody());
		assertEquals("Dados inválidos.", resposta.getBody());
		
	}
	//isbn invalido retorna 500
	@Test
	void ct03_quando_atualiza_com_id_nao_cadastrado_retorna_nao_encontrado() {
		// Dado – que o id e o isbn está cadastrado
		Livro livro = new Livro("2222", "User Stories", "Cohen");
		livro.setId(9L);
		Long id = 9L;
		HttpEntity<Livro> httpEntity = new HttpEntity<>(livro);
		//Quando atualiza com autor invalido
		ResponseEntity<String> resposta = testRestTemplate.exchange(urlBase + id , HttpMethod.PUT, httpEntity, String.class);
		//Entao retorna dados invalidos
		assertEquals("404 NOT_FOUND", resposta.getStatusCode().toString());
	}
}
