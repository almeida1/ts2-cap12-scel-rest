package com.fatec.scel.adapters;

import java.util.Optional;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fatec.scel.mantemLivro.model.Livro;
import com.fatec.scel.mantemLivro.ports.LivroServico;

@Controller
@RequestMapping(path = "/gui")
public class GUILivroController {
	Logger logger = LogManager.getLogger(GUILivroController.class);
	@Autowired
	LivroServico servico;

	@GetMapping("/livros")
	public ModelAndView retornaFormDeConsultaTodosLivros() {
		ModelAndView modelAndView = new ModelAndView("consultarLivro");
		modelAndView.addObject("livros", servico.consultaTodos());
		return modelAndView;
	}

	@GetMapping("/livro")
	public ModelAndView retornaFormDeCadastroDe(Livro livro) {
		ModelAndView mv = new ModelAndView("cadastrarLivro");
		mv.addObject("livro", livro);
		return mv;
	}

	@GetMapping("/livros/{isbn}") // diz ao metodo que ira responder a uma requisicao do tipo get
	public ModelAndView retornaFormParaEditarLivro(@PathVariable("isbn") String isbn) {
		ModelAndView modelAndView = new ModelAndView("atualizarLivro");
		modelAndView.addObject("livro", servico.consultaPorIsbn(isbn)); // o repositorio e injetado no controller
		return modelAndView; // addObject adiciona objetos para view
	}

	@GetMapping("/livro/{id}")
	public ModelAndView excluiNoFormDeConsultaLivro(@PathVariable("id") Long id) {
		servico.delete(id);
		ModelAndView modelAndView = new ModelAndView("consultarLivro");
		modelAndView.addObject("livros", servico.consultaTodos());
		return modelAndView;
	}

	@PostMapping("/livros")
	public ModelAndView save(@Valid Livro livro, BindingResult result) {
		ModelAndView modelAndView = new ModelAndView("consultarLivro");
		if (result.hasErrors()) {
			modelAndView.setViewName("cadastrarLivro");
		} else {
			try {
				servico.save(livro);
				modelAndView.addObject("livros", servico.consultaTodos());
			} catch (Exception e) { // captura validacoes na camada de persistencia
				modelAndView.setViewName("cadastrarLivro");
				modelAndView.addObject("message", "Livro ja cadastrado");
				logger.error("erro na camada de persistencia ==> " + e.getMessage());
			}
		}
		return modelAndView;
	}

	@PostMapping("/livros/{id}")
	public ModelAndView atualizaLivro(@PathVariable("id") Long id, @Valid Livro livro, BindingResult result) {
		ModelAndView mv = new ModelAndView("atualizarLivro");
		if (result.hasErrors()) {
			livro.setId(id);
		}
		else {
			Optional<Livro> umLivro = servico.consultaPorId(id);
			if (umLivro.isPresent()) {
				Livro atualizado = umLivro.get();
				atualizado.setAutor(livro.getAutor());
				atualizado.setIsbn(livro.getIsbn());
				atualizado.setTitulo(livro.getTitulo());
				servico.save(atualizado);
				mv.setViewName("consultarLivro");
				mv.addObject("livros", servico.consultaTodos());
			} 
		} 
		return mv;
	}
}
