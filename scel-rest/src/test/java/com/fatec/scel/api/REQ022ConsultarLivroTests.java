package com.fatec.scel.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.fatec.scel.mantemLivro.model.Livro;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class REQ022ConsultarLivroTests {
	String dom = "https://ts-scel-rest-livro.herokuapp.com";
	String urlBase = "/api/v1/livros/";
	@Autowired
	TestRestTemplate testRestTemplate;
	@Test
	void ct01_quando_cosulta_todos_retorna_lista_de_livros_cadastrados() {
		// Dado - que existem livros cadastrados
		// Quando - consulta todos
		ParameterizedTypeReference<List<Livro>> tipoRetorno = new ParameterizedTypeReference<List<Livro>>() {
		};
		ResponseEntity<List<Livro>> resposta = testRestTemplate.exchange(urlBase, HttpMethod.GET, null, tipoRetorno);
		// Entao - retorna lista de livros cadastrados
		assertEquals("200 OK", resposta.getStatusCode().toString());
		assertTrue(resposta.hasBody());
		List<Livro> lista = resposta.getBody();
		assertNotNull(lista);
		assertTrue(lista.size() > 0);
	}
	@Test
	public void ct02_quando_consulta_pelo_isbn_entao_retorna_os_detalhes_do_livro() throws Exception {
		// Dado - que o isbn esta cadastrado
		String isbn = "1111";
		// Quando - o usuario consulta pelo isbn
		ResponseEntity<Livro> resposta = testRestTemplate.getForEntity(urlBase + isbn, Livro.class);
		// Entao - retorna os detalhes do livro
		assertEquals("200 OK", resposta.getStatusCode().toString());
		assertTrue(resposta.hasBody());
		Livro ro = resposta.getBody();
		assertNotNull(ro);
	}

	@Test
	public void ct03_quando_consulta_isbn_nao_cadastrado_retorna_not_found() throws Exception {
		// Dado - que o isbn 3333 nao esta cadastrado
		String isbn = "3333";
		// Quando - o usuario consulta o isbn
		ResponseEntity<String> resposta = testRestTemplate.getForEntity(urlBase + isbn, String.class);
		// Entao - retorna not found
		assertEquals("400 BAD_REQUEST", resposta.getStatusCode().toString());
		assertEquals("ISBN não localizado", resposta.getBody());
	}
}
