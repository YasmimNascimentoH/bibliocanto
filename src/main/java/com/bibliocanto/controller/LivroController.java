package com.bibliocanto.controller;

import com.bibliocanto.model.Livro;
import com.bibliocanto.service.LivroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService service;

    public LivroController(LivroService service) {
        this.service = service;
    }

    @GetMapping
    public List<Livro> getAll() {
        return service.buscarTodos();
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Livro> getById(@PathVariable Long isbn) {
        // Tratamento de exceção simplificado pelo ServiceThrowing
        return ResponseEntity.ok(service.buscarPorIsbn(isbn));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Livro livro) {
        service.criar(livro);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<Livro> update(@PathVariable Long isbn, @RequestBody Livro livro) {
        return ResponseEntity.ok(service.atualizar(isbn, livro));
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> delete(@PathVariable Long isbn) {
        service.deletar(isbn);
        return ResponseEntity.noContent().build();
    }
}
