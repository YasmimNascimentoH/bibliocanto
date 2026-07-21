package com.bibliocanto.controller;

import com.bibliocanto.dto.ExemplarLivroDTO;
import com.bibliocanto.model.SituacaoLivro;
import com.bibliocanto.service.ExemplarLivroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exemplares")
public class ExemplarLivroController {

    private final ExemplarLivroService service;

    public ExemplarLivroController(ExemplarLivroService service) {
        this.service = service;
    }

    @GetMapping
    public List<ExemplarLivroDTO> getAll() {
        return service.buscarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExemplarLivroDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ExemplarLivroDTO> create(@RequestBody @Valid ExemplarLivroDTO dto) {
        return new ResponseEntity<>(service.criar(dto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/situacao")
    public ResponseEntity<ExemplarLivroDTO> alterarSituacao(
            @PathVariable UUID id,
            @RequestParam SituacaoLivro situacao) {

        return ResponseEntity.ok(service.atualizarSituacao(id, situacao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
