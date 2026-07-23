package com.bibliocanto.controller;

import com.bibliocanto.dto.EmprestimoDTO;
import com.bibliocanto.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private final EmprestimoService service;

    public EmprestimoController(EmprestimoService service) {
        this.service = service;
    }

    @GetMapping
    public List<EmprestimoDTO> getAll() {
        return service.buscarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EmprestimoDTO> create(@RequestBody @Valid EmprestimoDTO dto) {
        return new ResponseEntity<>(service.criar(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmprestimoDTO> update(@PathVariable UUID id, @RequestBody @Valid EmprestimoDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/devolucao")
    public long devolucao(@PathVariable UUID id) {
        return service.devolucaoEmprestimo(id);
    }

    @GetMapping("/{id}/usuario")
    public List<EmprestimoDTO> getByIdUser(@PathVariable UUID id) {
        return service.emprestimosEsuario(id);
    }
}
