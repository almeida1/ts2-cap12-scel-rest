package com.fatec.scel.adapters;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.scel.mantemLivro.model.Livro;
import com.fatec.scel.mantemLivro.ports.LivroServico;
import com.google.gson.Gson;
@RestController
@RequestMapping("/api/v1/livros")
public class APILivroController {
	@Autowired
	LivroServico servico; //controller nao conhece a implementacao 
	Logger logger = LogManager.getLogger(APILivroController.class);
	@CrossOrigin // desabilita o cors do spring security
	@PostMapping (consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> create(@RequestBody @Valid Livro livro, BindingResult result) {
		ResponseEntity<?> response = null;
		if (result.hasErrors()) {
			logger.info(">>>>>> controller create - dados inválidos ");
			response = ResponseEntity.badRequest().body("Dados inválidos.");
			//the server cannot or will not process the request due to something that is perceived to be a client error
		} else {

			Optional<Livro> umLivro = servico.consultaPorIsbn(livro.getIsbn());
			if (umLivro.isPresent()) {
				logger.info(">>>>>> controller create - livro já cadastrado");
				response = ResponseEntity.badRequest().body("Livro já cadastrado");
			} else {
				response = ResponseEntity.status(HttpStatus.CREATED).body(servico.save(livro));
				logger.info(">>>>>> controller create - cadastro realizado com sucesso");
			}
		}
		return response;
	}

	@CrossOrigin // desabilita o cors do spring security
	@GetMapping
	public ResponseEntity<List<Livro>> consultaTodos() {
		logger.info(">>>>>> controller chamou servico consulta todos");
		return ResponseEntity.ok().body(servico.consultaTodos());
	}
	@CrossOrigin // desabilita o cors do spring security
	@GetMapping("/{isbn}")
	public ResponseEntity<Livro> findByIsbn(@PathVariable String isbn) {
		logger.info(">>>>>> controller chamou servico consulta por isbn => " + isbn);
//		return Optional.ofNullable(servico.consultaPorIsbn(isbn)).map(record -> ResponseEntity.ok().body(record) ).orElse(ResponseEntity.notFound().build());
		Optional<Livro> umLivro = servico.consultaPorIsbn(isbn);
		ResponseEntity<Livro> resposta = null;
		if (umLivro.isPresent())
			resposta = ResponseEntity.status(HttpStatus.OK).body(umLivro.get());
		else
			resposta = ResponseEntity.notFound().build();
		return resposta;
	}
	@CrossOrigin // desabilita o cors do spring security
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		logger.info(">>>>>> controller chamou servico delete por id => " + id);
		Optional<Livro> umLivro = servico.consultaPorId(id);
		if (umLivro.isPresent()) {
			servico.delete(umLivro.get().getId());
			return ResponseEntity.ok().build();
			//return ResponseEntity.noContent().build();
		} else {
			logger.info(">>>>>> controller chamou servico delete id nao localizado => " + id);
			return ResponseEntity.notFound().build();
		}
		
	}
	/**
	 * atualiza as informacoes do livro
	 * @param id - identificador do livro se nao for encotrado retorna not found
	 * @param livro - livro com informacoes atualizadas
	 * @param result - resultado da validação das informacoes envidadas pelo usuario
	 * @return - o codigo http da operação e o body
	 */
	@CrossOrigin // desabilita o cors do spring security
	@PutMapping("/{id}")
	public ResponseEntity<String> replace(@PathVariable("id") long id, @RequestBody @Valid Livro livro, BindingResult result) {
		logger.info(">>>>>> controller chamou servico update por id ");
		if (result.hasErrors()) {
			logger.info(">>>>>> servico save - dados inválidos => " + result.getFieldErrors());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados inválidos.");
		} else {
			Optional<Livro> umLivro = servico.update(livro);
			
			if (umLivro.isPresent()) {
				Gson resposta = new Gson();
				String livrojson = resposta.toJson(umLivro.get(), Livro.class);
				return ResponseEntity.status(HttpStatus.OK).body(livrojson);
			}
			else 
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	@CrossOrigin // desabilita o cors do spring security
	@GetMapping("/id/{id}")
	ResponseEntity<?> findById(@PathVariable Long id) {
		logger.info(">>>>>> controller chamou servico consulta por id " + id);
        Optional<Livro> livro = servico.consultaPorId(id);
        return livro.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}