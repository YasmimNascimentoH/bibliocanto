package com.bibliocanto.dao;

import com.bibliocanto.model.Livro;
import java.util.List;
import java.util.Optional;

public interface LivroDAO {
    List<Livro> findAll();
    Optional<Livro> findById(Long isbn);
    void save(Livro livro);
    Livro update(Livro livro);
    void delete(Livro livro);
}
