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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fatec.scel.mantemLivro.model.Livro;
import com.google.gson.Gson;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class REQ021CadastrarLivroTests {
	String dom = "https://ts-scel-rest-livro.herokuapp.com";
	String urlBase = "/api/v1/livros";
	@Autowired
	TestRestTemplate testRestTemplate;
	@Test
	void ct01_cadastrar_livro_com_sucesso() {
		// Dado – que o atendente tem um livro não cadastrado
		Livro livro1 = new Livro("5555", "User Stories", "Cohn");
		Gson entradaDeDados = new Gson();
		String body = entradaDeDados.toJson(livro1);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> httpEntity = new HttpEntity<String>(body, headers);
		// Quando – o atendente cadastra um livro com informações válidas
		ResponseEntity<String> resposta = testRestTemplate.exchange(urlBase, HttpMethod.POST, httpEntity, String.class);
		// Então – o sistema valida os dados e permite a consulta do livro
		assertEquals("201 CREATED", resposta.getStatusCode().toString());
		Gson respostaDaMensagem = new Gson();
		assertTrue(resposta.hasBody());
		Livro livro2= respostaDaMensagem.fromJson(resposta.getBody(), Livro.class);
		assertNotNull(livro2);
		// String bodyEsperado = "{\"id\":1,\"isbn\":\"3333\",\"titulo\":\"User Stories\",\"autor\":\"Cohn\"}";
		// assertEquals(bodyEsperado, resposta.getBody());
	}
	@Test
	public void ct02_quando_livro_ja_cadastrado_retorna_msg_ja__cadastrado() {
		// Dado - que o livro ja esta cadastrado
		Livro livro = new Livro("4444", "User Stories", "Cohn");
		HttpEntity<Livro> httpEntity = new HttpEntity<>(livro);
		ResponseEntity<String> resposta = testRestTemplate.exchange(urlBase, HttpMethod.POST, httpEntity, String.class);
		// Quando - o usuario faz uma requisicao POST para cadastrar livro
		resposta = testRestTemplate.exchange(urlBase, HttpMethod.POST, httpEntity, String.class);
		// Entao - retorna HTTP200
		assertEquals("400 BAD_REQUEST", resposta.getStatusCode().toString());
		assertEquals("Livro já cadastrado", resposta.getBody());
	}
	@Test
	public void ct03_quando_nao_informa_titulo_retorna_dados_invalidos() {
		// Dado - que o livro ja esta cadastrado
		Livro livro = new Livro("5555", "", "Cohn");
		HttpEntity<Livro> httpEntity = new HttpEntity<>(livro);
		ResponseEntity<String> resposta = testRestTemplate.exchange(urlBase, HttpMethod.POST, httpEntity, String.class);
		// Quando - o usuario faz uma requisicao POST para cadastrar livro
		resposta = testRestTemplate.exchange(urlBase, HttpMethod.POST, httpEntity, String.class);
		// Entao - retorna HTTP200
		assertEquals("400 BAD_REQUEST", resposta.getStatusCode().toString());
		assertEquals("Dados inválidos.", resposta.getBody());
	}
	@Test
	public void ct04_quando_metodo_http_nao_disponivel_retorna_http_405() throws Exception {
		// Dado – que o servico está disponivel e o atendente tem um livro não
		// cadastrado
		Livro livro = new Livro("1111", "Teste de Software", "Delamaro");
		// Quando o cadastro é realizado para um método não disponivel
		HttpEntity<Livro> httpEntity3 = new HttpEntity<>(livro);
		ResponseEntity<String> resposta2 = testRestTemplate.exchange(urlBase, HttpMethod.PUT, httpEntity3,
				String.class);
		// Retorna http 405
		assertEquals("405 METHOD_NOT_ALLOWED", resposta2.getStatusCode().toString());
	}
}
