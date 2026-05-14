package com.bibliocanto.dao;

import com.bibliocanto.model.ExemplarLivro;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExemplarLivroDAO {
    List<ExemplarLivro> findAll();
    Optional<ExemplarLivro> findById(UUID id);
    void save(ExemplarLivro exemplar);
    ExemplarLivro update(ExemplarLivro exemplar);
    void delete(ExemplarLivro exemplar);
}
