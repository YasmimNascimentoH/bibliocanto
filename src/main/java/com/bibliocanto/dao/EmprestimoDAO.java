package com.bibliocanto.dao;

import com.bibliocanto.model.Emprestimo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface EmprestimoDAO {
    List<Emprestimo> findAll();
    Optional<Emprestimo> findById(UUID id);
    void save(Emprestimo emprestimo);
    Emprestimo update(Emprestimo emprestimo);
    void delete(Emprestimo emprestimo);
}
