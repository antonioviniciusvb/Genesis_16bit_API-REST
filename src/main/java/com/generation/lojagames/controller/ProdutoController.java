package com.generation.lojagames.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.lojagames.model.Produto;
import com.generation.lojagames.repository.CategoriaRepository;
import com.generation.lojagames.repository.ProdutoRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping
	public ResponseEntity<List<Produto>> getAll() {

		return ResponseEntity.ok(produtoRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id) {

		return produtoRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Produto>> getByTitulo(@PathVariable String nome) {

		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
	}

	@GetMapping("/preco/maior/{preco}")
	public ResponseEntity<List<Produto>> getByPrecoGreaterThan(@PathVariable BigDecimal preco) {

		return ResponseEntity.ok(produtoRepository.findAllByPrecoGreaterThan(preco));
	}

	@GetMapping("/preco/menor/{preco}")
	public ResponseEntity<List<Produto>> getByPrecoLessThan(@PathVariable BigDecimal preco) {

		return ResponseEntity.ok(produtoRepository.findAllByPrecoLessThan(preco));
	}

	@PostMapping
	public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto) {

		if (categoriaRepository.existsById(produto.getCategoria().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe", null);
	}

	@PutMapping
	public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {

		if (produtoRepository.existsById(produto.getId())) {

			if (categoriaRepository.existsById(produto.getCategoria().getId()))
				return ResponseEntity.status(HttpStatus.OK).body(produtoRepository.save(produto));

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe", null);
		}

		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não existe");
	}
	
	@PostMapping("/insert")

	public ResponseEntity<List<Produto>> post(@RequestBody  List<@Valid Produto> produtos) {

		for (var produto : produtos) {

			System.out.println("todos passaram");
			
			if (!(categoriaRepository.existsById(produto.getCategoria().getId()))) {

				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe", null);
			}
		}
		
		System.out.println("todos passaram");

		for (var produto : produtos) {

//			System.out.println(String.format("Produt: {0}" , produto.getNome()));
			produtoRepository.save(produto);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(produtos);

	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<String> handle(ConstraintViolationException constraintViolationException) {
	    Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
	    String errorMessage = "";
	    if (!violations.isEmpty()) {
	        StringBuilder builder = new StringBuilder();
	        violations.forEach(violation -> builder.append(String.format("%s:\n%s\n\n", violation.getPropertyPath(), violation.getMessage())));
	        errorMessage = builder.toString();
	    } else {
	        errorMessage = "ConstraintViolationException occured.";
	    }
	    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	 }

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {

		if (produtoRepository.existsById(id))
			produtoRepository.deleteById(id);
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}
}
